package com.example.splitter.utils

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun Double.format(decimals: Int) = NSString.stringWithFormat("%.${decimals}f", this)
