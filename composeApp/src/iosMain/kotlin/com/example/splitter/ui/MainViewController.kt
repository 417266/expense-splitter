package com.example.splitter.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.example.splitter.model.Dependencies

fun MainViewController(dependencies: Dependencies) = ComposeUIViewController { App(dependencies) }
