package com.example.splitter.model

import com.example.splitter.database.Expense
import com.example.splitter.database.Person

class ExpenseDetails(val expense: Expense, val payer: Person?, val participants: List<Person?>)
