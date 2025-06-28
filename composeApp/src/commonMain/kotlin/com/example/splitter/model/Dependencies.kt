package com.example.splitter.model

import com.example.splitter.database.Database

class Dependencies(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory.createDriver())
    val repository = Repository(database)
}
