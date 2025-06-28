package com.example.splitter.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.splitter.model.Dependencies

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ToOrFrom(appNavController: NavHostController, dependencies: Dependencies) {
    val savedStateHandle = appNavController.previousBackStackEntry?.savedStateHandle ?: return
    val transferType = TransferType.entries[savedStateHandle["typeIndex"]!!]
    val friends by dependencies.repository.getFriends().collectAsState(null)
    val immutableFriends = friends
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val payerID by savedStateHandle.getStateFlow<Long?>("friendID", null).collectAsState()
    val onSelect = { id: Long ->
        savedStateHandle["friendID"] = id
        appNavController.popBackStack()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(transferType.friendLabel) },
                navigationIcon = {
                    IconButton({ appNavController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        when {
            immutableFriends == null -> return@Scaffold
            immutableFriends.isEmpty() -> {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(
                            text = "No friends yet",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            else -> {
                LazyColumn(contentPadding = padding) {
                    items(immutableFriends, { friend -> friend.id }) { friend ->
                        ListItem(
                            leadingContent = {
                                RadioButton(
                                    selected = payerID == friend.id,
                                    onClick = { onSelect(friend.id) },
                                    modifier = Modifier.offset(x = (-12).dp),
                                )
                            },
                            headlineContent = { Text(friend.name, Modifier.offset(x = (-24).dp)) },
                            modifier = Modifier.clickable { onSelect(friend.id) },
                        )
                    }
                    item { Spacer(Modifier.height(88.dp)) }
                }
            }
        }
    }
}
