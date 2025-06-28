package com.example.splitter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.splitter.database.Person

@Composable
fun FriendListItem(friend: Person, onRename: () -> Unit, onDelete: () -> Unit) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    ListItem(
        headlineContent = { Text(friend.name) },
        trailingContent = {
            Box {
                IconButton({ dropDownExpanded = true }, Modifier.offset(x = 12.dp)) {
                    Icon(Icons.Default.MoreVert, null)
                }
                DropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Rename") },
                        onClick = {
                            dropDownExpanded = false
                            onRename()
                        },
                        leadingIcon = { Icon(Icons.Default.Edit, null) },
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Delete", color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            dropDownExpanded = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                        },
                    )
                }
            }
        },
    )
}
