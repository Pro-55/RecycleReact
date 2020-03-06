package com.example.recyclereact

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_pop_up.view.*
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val adapter by lazy { ReactionsAdapter() }
    private val layoutManager by lazy { CustomLinearLayoutManager(this) }
    private val wP by lazy { getDisplayMetrics().widthPixels }
    private val hP by lazy { getDisplayMetrics().heightPixels }
    private val reactions =
        intArrayOf(R.drawable.mascot_love, R.drawable.mascot_cool, R.drawable.mascot_star)
    private var isLoading = false
    private var totalCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        adapter.listener = object : ReactionsAdapter.ReactionListener {
            override fun isShowingChanged(isShowing: Boolean) {
                layoutManager.setScrollEnabled(!isShowing)
            }
        }
        recycle_reactions.layoutManager = layoutManager
        recycle_reactions.adapter = adapter
        val hP = getDisplayMetrics().heightPixels
        recycle_reactions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rV: RecyclerView, dx: Int, dy: Int) {
                //positions
                val fVIP = layoutManager.findFirstVisibleItemPosition()
                val lVIP = layoutManager.findLastVisibleItemPosition()
                val cVIP = if (lVIP - fVIP > 1) fVIP + 1 else -1
                val lastPosition = adapter.playerPosition

                //current adapter count
                val count = adapter.itemCount

                //Fetch more posts
                if (!isLoading &&
                    lVIP < 5 && // Load only if 5 items are left to scroll
                    totalCount > count // Don't load after the last page is reached
                ) {
                    isLoading = true
                    //fetch next batch of items
                }

                //visible height of each position
                val fVH = getVisibleHeight(rV, fVIP)
                val cVH = getVisibleHeight(rV, cVIP)
                val lVH = getVisibleHeight(rV, lVIP)
                val playingVH = getVisibleHeight(rV, lastPosition)

                val targetPosition = when {
                    fVH >= cVH && fVH >= lVH -> fVIP
                    cVH >= fVH && cVH >= lVH -> cVIP
                    else -> lVIP
                }

                if (lastPosition > -1 && playingVH < 250) {
                    //stopVideo
                }
                if (targetPosition != lastPosition) {
                    //playVideo
                }

            }
        })
        val data = arrayListOf<Item>()
        for (x in 0..1000) {
            val type = if (x % 2 == 0) 0 else 1
            val index = Random.nextInt(0, 2)
            val item = Item("$x", type, reactions[index])
            data.add(item)
        }
        adapter.swapData(data)
    }

    private fun getVisibleHeight(rV: RecyclerView, position: Int): Int {
        val playPosition = position - layoutManager.findFirstVisibleItemPosition()
        val view = rV.getChildAt(playPosition) ?: return 0
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return if (location[1] < 0) location[1] + wP else hP - location[1]
    }

}