package com.example.splitter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.splitter.model.Dependencies
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expenses(appNavController: NavHostController, dependencies: Dependencies) {
    val details by dependencies.repository.getExpenseDetails().collectAsState(null)
    val immutableDetails = details
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    var deletedExpenseID by remember { mutableStateOf<Long?>(null) }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar(title = { Text("Expenses") }, scrollBehavior = scrollBehavior) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add expense") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = { appNavController.navigate(AppDestination.AddExpense.route) },
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { padding ->
        when {
            immutableDetails == null -> return@Scaffold
            immutableDetails.isEmpty() ->
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Text(
                        text = "No expenses yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            else ->
                LazyColumn(contentPadding = padding) {
                    items(items = immutableDetails, key = { details -> details.expense.id }) {
                        details ->
                        ExpenseListItem(
                            details = details,
                            onDelete = { deletedExpenseID = details.expense.id },
                        )
                    }
                    item { Spacer(Modifier.height(88.dp)) }
                }
        }
    }
    deletedExpenseID?.let { id ->
        DeleteDialog(
            text = "Delete expense?",
            onDismiss = { deletedExpenseID = null },
            onDelete = { scope.launch { dependencies.repository.deleteExpense(id) } },
        )
    }
}
