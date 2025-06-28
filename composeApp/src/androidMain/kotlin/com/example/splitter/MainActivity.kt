package com.example.splitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.splitter.model.DatabaseDriverFactory
import com.example.splitter.model.Dependencies
import com.example.splitter.ui.App

class MainActivity : ComponentActivity() {
    private val dependencies by lazy { Dependencies(DatabaseDriverFactory(applicationContext)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { App(dependencies) }
    }
}
