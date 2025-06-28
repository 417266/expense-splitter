package com.example.splitter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.splitter.database.Person
import com.example.splitter.model.Dependencies
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddTransfer(appNavController: NavHostController, dependencies: Dependencies) {
    val savedStateHandle = appNavController.currentBackStackEntry?.savedStateHandle ?: return
    val scope = rememberCoroutineScope()
    val friendID = savedStateHandle.get<Long>("friendID")
    var friend by remember { mutableStateOf<Person?>(null) }
    val typeIndex by savedStateHandle.getStateFlow("typeIndex", 0).collectAsState()
    val amount by savedStateHandle.getStateFlow("amount", "").collectAsState()
    val amountHasError by savedStateHandle.getStateFlow("amountHasError", false).collectAsState()
    val toOrFromHasError by
        savedStateHandle.getStateFlow("toOrFromHasError", false).collectAsState()
    LaunchedEffect(Unit) {
        if (friendID == null) return@LaunchedEffect
        friend = dependencies.repository.getPersonByID(friendID)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add transfer") },
                navigationIcon = {
                    IconButton(appNavController::popBackStack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Save") },
                icon = { Icon(Icons.Default.Check, null) },
                onClick = {
                    val convertedAmount = amount.toDoubleOrNull()
                    savedStateHandle["amountHasError"] =
                        convertedAmount == null ||
                            convertedAmount <= 0.0 ||
                            (100 * convertedAmount).rem(1) != 0.0
                    savedStateHandle["toOrFromHasError"] = friendID == null
                    if (
                        savedStateHandle.get<Boolean>("amountHasError") == true ||
                            savedStateHandle.get<Boolean>("toOrFromHasError") == true
                    ) {
                        return@ExtendedFloatingActionButton
                    }
                    scope.launch {
                        dependencies.repository.addTransfer(
                            (if (typeIndex == 0) 1 else -1) * convertedAmount!!,
                            friendID!!,
                        )
                    }
                    appNavController.popBackStack()
                },
            )
        },
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(start = 16.dp, top = 8.dp, end = 16.dp),
            Arrangement.spacedBy(16.dp),
        ) {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                TransferType.entries.forEachIndexed { index, segmentType ->
                    SegmentedButton(
                        selected = index == typeIndex,
                        onClick = { savedStateHandle["typeIndex"] = index },
                        shape = SegmentedButtonDefaults.itemShape(index, TransferType.entries.size),
                    ) {
                        Text(segmentType.label)
                    }
                }
            }
            TextField(
                value = amount,
                onValueChange = { newAmount -> savedStateHandle["amount"] = newAmount },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Amount") },
                isError = amountHasError,
                prefix = { Text("$") },
                singleLine = true,
            )
            SelectionField(
                value = friend?.name.orEmpty(),
                label = if (typeIndex == 0) "From" else "To",
                hasError = toOrFromHasError,
                onClick = { appNavController.navigate(AppDestination.ToOrFrom.route) },
            )
        }
    }
}
