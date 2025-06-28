package com.example.splitter.model

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.splitter.database.Database
import com.example.splitter.database.ExpenseParticipant
import com.example.splitter.database.Person
import kotlin.math.abs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class Repository(private val database: Database) {
    fun getFriends() = database.personQueries.selectFriends().asFlow().mapToList(Dispatchers.IO)

    fun getPersonByID(id: Long) = database.personQueries.selectByID(id).executeAsOne()

    suspend fun addPerson(name: String) {
        withContext(Dispatchers.IO) { database.personQueries.insert(name) }
    }

    suspend fun deletePerson(id: Long) {
        withContext(Dispatchers.IO) { database.personQueries.delete(id) }
    }

    suspend fun renamePerson(id: Long, name: String) {
        withContext(Dispatchers.IO) { database.personQueries.rename(name, id) }
    }

    suspend fun addExpense(
        name: String,
        amount: Double,
        payerID: Long,
        participantIDs: List<Long>,
    ) {
        withContext(Dispatchers.IO) {
            database.transaction {
                database.expenseQueries.insert(name, amount, payerID)
                val expenseID = database.expenseQueries.selectLastInsertedID().executeAsOne()
                participantIDs.forEach { participantID ->
                    database.expenseParticipantQueries.insert(expenseID, participantID)
                }
            }
        }
    }

    fun getExpenseDetails() =
        combine(
                database.expenseQueries.selectAll().asFlow().mapToList(Dispatchers.IO),
                database.expenseParticipantQueries.selectAll().asFlow().mapToList(Dispatchers.IO),
                database.personQueries.selectAll().asFlow().mapToList(Dispatchers.IO),
            ) { expenses, participants, people ->
                val groupedParticipants = participants.groupBy(ExpenseParticipant::expenseID)
                val associatedPeople = people.associateBy(Person::id)
                expenses.map { expense ->
                    ExpenseDetails(
                        expense = expense,
                        payer = associatedPeople[expense.payerID],
                        participants =
                            groupedParticipants.getValue(expense.id).map { participant ->
                                associatedPeople[participant.participantID]
                            },
                    )
                }
            }
            .flowOn(Dispatchers.IO)

    suspend fun deleteExpense(id: Long) {
        withContext(Dispatchers.IO) { database.expenseQueries.delete(id) }
    }

    fun getTransfersWithFriendNames() =
        database.transferQueries.selectAllWithFriendNames().asFlow().mapToList(Dispatchers.IO)

    suspend fun addTransfer(amount: Double, friendID: Long) {
        withContext(Dispatchers.IO) { database.transferQueries.insert(amount, friendID) }
    }

    suspend fun deleteTransfer(id: Long) {
        withContext(Dispatchers.IO) { database.transferQueries.delete(id) }
    }

    suspend fun getBalances() =
        withContext(Dispatchers.IO) {
            val friends =
                database.personQueries.selectFriends().executeAsList().associateBy(Person::id)
            val expenses = database.expenseQueries.selectAll().executeAsList()
            val participants =
                database.expenseParticipantQueries
                    .selectAll()
                    .executeAsList()
                    .groupBy(ExpenseParticipant::expenseID)
            val transfers = database.transferQueries.selectAll().executeAsList()
            val balances = mutableMapOf<Long, Double>()
            for (expense in expenses) {
                val participantIDs =
                    participants.getValue(expense.id).map(ExpenseParticipant::participantID)
                val share = expense.amount / participantIDs.size
                for (id in participantIDs) {
                    when {
                        id == expense.payerID -> continue
                        id == 0L ->
                            balances[expense.payerID] = (balances[expense.payerID] ?: 0.0) - share
                        expense.payerID == 0L -> balances[id] = (balances[id] ?: 0.0) + share
                    }
                }
            }
            for (transfer in transfers) {
                balances[transfer.friendID] = (balances[transfer.friendID] ?: 0.0) - transfer.amount
            }
            balances
                .filterValues { abs(it) > 0.01 }
                .mapNotNull { (friendID, amount) ->
                    Balance(friends[friendID] ?: return@mapNotNull null, amount)
                }
                .partition { it.amount < 0 }
                .let { (negative, positive) ->
                    negative.sortedBy { balance -> balance.amount } +
                        positive.sortedByDescending { balance -> balance.amount }
                }
        }
}
