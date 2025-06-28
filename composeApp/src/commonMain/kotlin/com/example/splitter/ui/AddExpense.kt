package com.example.splitter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.splitter.database.Person
import com.example.splitter.model.Dependencies
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddExpense(appNavController: NavHostController, dependencies: Dependencies) {
    val savedStateHandle = appNavController.currentBackStackEntry?.savedStateHandle ?: return
    val scope = rememberCoroutineScope()
    val payerID = savedStateHandle.get<Long>("payerID")
    val participantIDs = savedStateHandle.get<List<Long>>("participantIDs") ?: listOf(0L)
    var payer by remember { mutableStateOf<Person?>(null) }
    var participants by remember { mutableStateOf<List<Person>>(emptyList()) }
    val name by savedStateHandle.getStateFlow("name", "").collectAsState()
    val amount by savedStateHandle.getStateFlow("amount", "").collectAsState()
    val nameHasError by savedStateHandle.getStateFlow("nameHasError", false).collectAsState()
    val amountHasError by savedStateHandle.getStateFlow("amountHasError", false).collectAsState()
    val whoPaidHasError by savedStateHandle.getStateFlow("whoPaidHasError", false).collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    LaunchedEffect(Unit) {
        if (payerID != null) payer = dependencies.repository.getPersonByID(payerID)
        participants = participantIDs.map(dependencies.repository::getPersonByID)
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Add expense") },
                navigationIcon = {
                    IconButton(appNavController::popBackStack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Save") },
                icon = { Icon(Icons.Default.Check, null) },
                onClick = {
                    savedStateHandle["nameHasError"] = name.isBlank()
                    val convertedAmount = amount.toDoubleOrNull()
                    savedStateHandle["amountHasError"] =
                        convertedAmount == null ||
                            convertedAmount <= 0 ||
                            (100 * convertedAmount).rem(1) != 0.0
                    savedStateHandle["whoPaidHasError"] = payerID == null
                    if (
                        savedStateHandle.get<Boolean>("nameHasError") == true ||
                            savedStateHandle.get<Boolean>("amountHasError") == true ||
                            savedStateHandle.get<Boolean>("whoPaidHasError") == true
                    ) {
                        return@ExtendedFloatingActionButton
                    }
                    scope.launch {
                        dependencies.repository.addExpense(
                            name,
                            convertedAmount!!,
                            payerID!!,
                            participantIDs,
                        )
                    }
                    appNavController.popBackStack()
                },
            )
        },
    ) { padding ->
        Box(Modifier.verticalScroll(rememberScrollState())) {
            Column(
                Modifier.padding(padding).padding(horizontal = 16.dp),
                Arrangement.spacedBy(16.dp),
            ) {
                TextField(
                    value = name,
                    onValueChange = { savedStateHandle["name"] = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    singleLine = true,
                    isError = nameHasError,
                )
                TextField(
                    value = amount,
                    onValueChange = { savedStateHandle["amount"] = it },
                    label = { Text("Amount") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = amountHasError,
                    prefix = { Text("$") },
                )
                SelectionField(
                    value = if (payer?.id == 0L) "Me" else payer?.name.orEmpty(),
                    label = "Who paid",
                    hasError = whoPaidHasError,
                    onClick = { appNavController.navigate(AppDestination.WhoPaid.route) },
                    fontStyle = if (payer?.id == 0L) FontStyle.Italic else FontStyle.Normal,
                )
                OutlinedCard(
                    onClick = { appNavController.navigate(AppDestination.SplitAmong.route) },
                    modifier = Modifier.padding(bottom = 88.dp),
                ) {
                    Row(
                        modifier =
                            Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp, end = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "Split among", style = MaterialTheme.typography.titleMedium)
                        IconButton({ appNavController.navigate(AppDestination.SplitAmong.route) }) {
                            Icon(Icons.Default.Edit, null)
                        }
                    }
                    participants.forEach { participant ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = if (participant.id == 0L) "Me" else participant.name,
                                    fontStyle =
                                        if (participant.id == 0L) {
                                            FontStyle.Italic
                                        } else {
                                            FontStyle.Normal
                                        },
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
