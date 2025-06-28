package com.example.splitter.utils

actual fun Double.format(decimals: Int) = "%.${decimals}f".format(this)
