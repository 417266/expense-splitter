package com.example.splitter.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeDestination(val route: String, val label: String, val icon: ImageVector) {
    Expenses("expenses", "Expenses", Icons.Default.AttachMoney),
    Balances("balances", "Balances", Icons.Default.Balance),
    Transfers("transfers", "Transfers", Icons.Default.SyncAlt),
    Friends("friends", "Friends", Icons.Default.Groups),
}
