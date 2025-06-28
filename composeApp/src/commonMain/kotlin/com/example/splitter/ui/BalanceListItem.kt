package com.example.splitter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import com.example.splitter.model.Balance
import com.example.splitter.utils.format
import kotlin.math.abs

@Composable
fun BalanceListItem(balance: Balance) {
    ListItem(
        headlineContent = { Text(balance.friend.name) },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text =
                        buildAnnotatedString {
                            if (balance.amount > 0) {
                                append("Owes ")
                                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("you") }
                            } else {
                                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("You") }
                                append(" owe")
                            }
                        },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "$${abs(balance.amount).format()}",
                    color =
                        if (balance.amount > 0) {
                            Color(0xff16a34a)
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
    )
}
