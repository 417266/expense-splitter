package com.example.splitter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.splitter.database.SelectAllWithFriendNames
import com.example.splitter.utils.format
import kotlin.math.abs

@Composable
fun TransferListItem(details: SelectAllWithFriendNames, onDelete: () -> Unit) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    ListItem(
        headlineContent = { Text(details.friendName) },
        trailingContent = {
            Row(
                Modifier.offset(x = 12.dp),
                Arrangement.spacedBy(2.dp),
                Alignment.CenterVertically,
            ) {
                Text(
                    text =
                        "${if (details.amount > 0) "+" else "−"}$${abs(details.amount).format()}",
                    color =
                        if (details.amount > 0) {
                            Color(0xff16a34a)
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                    style = MaterialTheme.typography.bodyLarge,
                )
                Box {
                    IconButton({ dropDownExpanded = true }) { Icon(Icons.Default.MoreVert, null) }
                    DropdownMenu(
                        expanded = dropDownExpanded,
                        onDismissRequest = { dropDownExpanded = false },
                        offset = DpOffset((-12).dp, 0.dp),
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                            },
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
            }
        },
    )
}
