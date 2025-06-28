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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Transfers(appNavController: NavHostController, dependencies: Dependencies) {
    val details = dependencies.repository.getTransfersWithFriendNames().collectAsState(null)
    val immutableDetails = details.value
    val scope = rememberCoroutineScope()
    var deletedTransferID by remember { mutableStateOf<Long?>(null) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar(title = { Text("Transfers") }, scrollBehavior = scrollBehavior) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add transfer") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = { appNavController.navigate(AppDestination.AddTransfer.route) },
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { padding ->
        when {
            immutableDetails == null -> return@Scaffold
            immutableDetails.isEmpty() ->
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Text(
                        text = "No transfers yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            else ->
                LazyColumn(contentPadding = padding) {
                    items(items = immutableDetails, key = { details -> details.id }) { details ->
                        TransferListItem(
                            details = details,
                            onDelete = { deletedTransferID = details.id },
                        )
                    }
                    item { Spacer(Modifier.height(88.dp)) }
                }
        }
    }
    deletedTransferID?.let { id ->
        DeleteDialog(
            text = "Delete transfer?",
            onDismiss = { deletedTransferID = null },
            onDelete = { scope.launch { dependencies.repository.deleteTransfer(id) } },
        )
    }
}
