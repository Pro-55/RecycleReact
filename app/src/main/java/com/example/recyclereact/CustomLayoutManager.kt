package com.example.recyclereact

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

class CustomLinearLayoutManager(
    context: Context,
    private val direction: Direction = Direction.VERTICAL
) :
    LinearLayoutManager(context) {

    enum class Direction {
        VERTICAL,
        HORIZONTAL
    }

    private var isScrollEnabled = true

    fun setScrollEnabled(isScrollEnabled: Boolean) {
        this.isScrollEnabled = isScrollEnabled
    }

    override fun canScrollVertically(): Boolean {
        return if (direction == Direction.VERTICAL) isScrollEnabled else false
    }

    override fun canScrollHorizontally(): Boolean {
        return if (direction == Direction.HORIZONTAL) isScrollEnabled else false
    }
}