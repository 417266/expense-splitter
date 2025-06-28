package com.example.splitter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splitter.model.Dependencies
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(dependencies: Dependencies) {
    val appNavController = rememberNavController()
    MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        NavHost(
            appNavController,
            AppDestination.Home.route,
            Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            AppDestination.entries.forEach { destination ->
                composable(destination.route) {
                    when (destination) {
                        AppDestination.Home -> Home(appNavController, dependencies)
                        AppDestination.AddExpense -> AddExpense(appNavController, dependencies)
                        AppDestination.WhoPaid -> WhoPaid(appNavController, dependencies)
                        AppDestination.SplitAmong -> SplitAmong(appNavController, dependencies)
                        AppDestination.AddTransfer -> AddTransfer(appNavController, dependencies)
                        AppDestination.ToOrFrom -> ToOrFrom(appNavController, dependencies)
                    }
                }
            }
        }
    }
}
