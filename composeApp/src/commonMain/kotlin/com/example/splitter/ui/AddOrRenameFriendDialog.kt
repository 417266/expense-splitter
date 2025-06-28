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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddOrRenameFriendDialog(
    title: String,
    initialName: String,
    buttonLabel: String,
    onSave: (name: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember {
        mutableStateOf(
            TextFieldValue(text = initialName, selection = TextRange(initialName.length))
        )
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    BasicAlertDialog(onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier =
                        Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp)
                            .focusRequester(focusRequester),
                    label = { Text("Name") },
                    singleLine = true,
                )
                Row(Modifier.align(Alignment.End).padding(8.dp)) {
                    TextButton(onDismiss) { Text("Cancel") }
                    TextButton(
                        onClick = {
                            onSave(name.text)
                            onDismiss()
                        },
                        enabled = name.text != initialName && name.text.isNotBlank(),
                    ) {
                        Text(buttonLabel)
                    }
                }
            }
        }
    }
}
