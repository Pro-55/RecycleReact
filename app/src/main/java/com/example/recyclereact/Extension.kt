package com.example.recyclereact

import android.app.Activity
import android.util.DisplayMetrics

/**
 * Get Screen size
 */
fun Activity.getDisplayMetrics(): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}