package com.example.recyclereact

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private val reactions =
        intArrayOf(R.drawable.mascot_love, R.drawable.mascot_cool, R.drawable.mascot_star)
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = ReactionsAdapter()
        adapter.listener = object : ReactionsAdapter.ReactionListener {
            override fun onItemTouch() {
                Log.d(TAG, "TestLog: onItemTouch!")
            }
        }
        val layoutManager = LinearLayoutManager(this)
        recycle_reactions.layoutManager = layoutManager
        recycle_reactions.adapter = adapter
        val hP = getDisplayMetrics().heightPixels
        recycle_reactions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // Wait for user to stop scrolling
                // binding.nestedScrollHome.canScrollVertically(1) indicates that user cannot scroll anymore
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && !isLoading
                    && !recyclerView.canScrollVertically(1)
                ) {
                    //Do additional checks and fetch next page
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val fVIP = layoutManager.findFirstVisibleItemPosition()
                val lVIP = layoutManager.findLastVisibleItemPosition()
                val lastPosition = adapter.lastPosition

                val fIV =
                    if (fVIP > -1) recyclerView.findViewHolderForAdapterPosition(fVIP)?.itemView else null
                val lIV =
                    if (lVIP > -1) recyclerView.findViewHolderForAdapterPosition(lVIP)?.itemView else null
                val fPost = if (fVIP > -1) adapter.currentList[fVIP] else null
                val lPost = if (fVIP > -1) adapter.currentList[lVIP] else null

                if (fPost?.type == 1 && lastPosition != fVIP && fIV != null && fIV.bottom.toDouble() / hP > 0.35) {
//                    playVideo
                } else if (lastPosition == fVIP && fIV != null && fIV.bottom.toDouble() / hP < 0.35) {
//                    stopVideo
                } else if (lPost?.type == 1 && lastPosition != lVIP && lIV != null && lIV.top.toDouble() / hP < 0.35) {
//                    playVideo
                } else if (lastPosition == lVIP && lIV != null && lIV.top.toDouble() / hP > 0.65) {
//                    stopVideo
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
}