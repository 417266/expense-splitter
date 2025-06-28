package com.example.splitter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.splitter.model.ExpenseDetails
import com.example.splitter.utils.format

@Composable
fun ExpenseListItem(details: ExpenseDetails, onDelete: () -> Unit) {
    var dropDownExpanded by remember { mutableStateOf(false) }
    val supportingText = buildAnnotatedString {
        when (details.participants.size) {
            1 -> append("Personal")
            2 -> {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("You") }
                append(" and ")
                details.participants.first { it?.id != 0L }?.name?.let { name -> append(name) }
                    ?: append("deleted")
            }
            else -> {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("You") }
                append(" and ${details.participants.size - 1} others")
            }
        }
    }
    ListItem(
        headlineContent = { Text(details.expense.name) },
        supportingContent = { Text(supportingText) },
        trailingContent = {
            Row(
                Modifier.offset(x = 12.dp),
                Arrangement.spacedBy(2.dp),
                Alignment.CenterVertically,
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text =
                            buildAnnotatedString {
                                when {
                                    details.payer == null -> {
                                        append("Deleted")
                                    }
                                    details.payer.id == 0L -> {
                                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                            append("You")
                                        }
                                    }
                                    else -> {
                                        append(details.payer.name)
                                    }
                                }
                                append(" paid")
                            },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "$${details.expense.amount.format()}",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
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
