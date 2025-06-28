package com.example.splitter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeleteDialog(text: String, onDismiss: () -> Unit, onDelete: () -> Unit) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column {
                Text(text, Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp))
                Row(Modifier.align(Alignment.End).padding(8.dp)) {
                    TextButton(onDismiss) { Text("Cancel") }
                    TextButton({
                        onDelete()
                        onDismiss()
                    }) {
                        Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
