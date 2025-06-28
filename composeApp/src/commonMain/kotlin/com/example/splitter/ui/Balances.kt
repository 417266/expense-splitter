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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.splitter.model.Balance
import com.example.splitter.model.Dependencies

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Balances(dependencies: Dependencies) {
    var balances by remember { mutableStateOf<List<Balance>?>(null) }
    val immutableBalances = balances
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    LaunchedEffect(Unit) { balances = dependencies.repository.getBalances() }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar(title = { Text("Balances") }, scrollBehavior = scrollBehavior) },
        contentWindowInsets = WindowInsets.statusBars,
    ) { padding ->
        when {
            immutableBalances == null -> return@Scaffold
            immutableBalances.isEmpty() ->
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Text(
                        text = "All settled up",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            else ->
                LazyColumn(contentPadding = padding) {
                    items(items = immutableBalances, key = { balance -> balance.friend.id }) {
                        balance ->
                        BalanceListItem(balance)
                    }
                    item { Spacer(Modifier.height(88.dp)) }
                }
        }
    }
}
