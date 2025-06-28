package com.example.splitter.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.splitter.model.Dependencies

@Composable
fun Home(appNavController: NavHostController, dependencies: Dependencies) {
    val homeNavController = rememberNavController()
    val currentBackStackEntry by homeNavController.currentBackStackEntryAsState()
    Scaffold(
        bottomBar = {
            NavigationBar {
                HomeDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentBackStackEntry?.destination?.route == destination.route,
                        onClick = { homeNavController.navigate(destination.route) },
                        icon = { Icon(destination.icon, null) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
    ) { padding ->
        NavHost(homeNavController, HomeDestination.Expenses.route, Modifier.padding(padding)) {
            HomeDestination.entries.forEach { destination ->
                composable(destination.route) {
                    when (destination) {
                        HomeDestination.Expenses -> Expenses(appNavController, dependencies)
                        HomeDestination.Balances -> Balances(dependencies)
                        HomeDestination.Transfers -> Transfers(appNavController, dependencies)
                        HomeDestination.Friends -> Friends(dependencies)
                    }
                }
            }
        }
    }
}
