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
import com.example.splitter.database.Person
import com.example.splitter.model.Dependencies
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Friends(dependencies: Dependencies) {
    val friends by dependencies.repository.getFriends().collectAsState(null)
    val immutableFriends = friends
    var addFriendDialogOpen by remember { mutableStateOf(false) }
    var renamedFriend by remember { mutableStateOf<Person?>(null) }
    var deletedFriend by remember { mutableStateOf<Person?>(null) }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar(title = { Text("Friends") }, scrollBehavior = scrollBehavior) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add friend") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = { addFriendDialogOpen = true },
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { padding ->
        when {
            immutableFriends == null -> return@Scaffold
            immutableFriends.isEmpty() -> {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Text(
                        text = "No friends yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            else -> {
                LazyColumn(contentPadding = padding) {
                    items(items = immutableFriends, key = { friend -> friend.id }) { friend ->
                        if (friend.id == 0L) return@items
                        FriendListItem(
                            friend = friend,
                            onRename = { renamedFriend = friend },
                            onDelete = { deletedFriend = friend },
                        )
                    }
                    item { Spacer(Modifier.height(88.dp)) }
                }
            }
        }
    }
    if (addFriendDialogOpen) {
        AddOrRenameFriendDialog(
            title = "Add friend",
            initialName = "",
            buttonLabel = "Add",
            onSave = { name -> scope.launch { dependencies.repository.addPerson(name) } },
            onDismiss = { addFriendDialogOpen = false },
        )
    }
    renamedFriend?.let { friend ->
        AddOrRenameFriendDialog(
            title = "Rename friend",
            initialName = friend.name,
            buttonLabel = "Rename",
            onSave = { name ->
                scope.launch { dependencies.repository.renamePerson(friend.id, name) }
            },
            onDismiss = { renamedFriend = null },
        )
    }
    deletedFriend?.let { friend ->
        DeleteDialog(
            text = "Delete friend ${friend.name}?",
            onDismiss = { deletedFriend = null },
            onDelete = {
                scope.launch {
                    dependencies.repository.deletePerson(friend.id)
                    deletedFriend = null
                }
            },
        )
    }
}
