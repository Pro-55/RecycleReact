package com.example.recyclereact.reaction

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow

class ReactionPopup(
    context: Context,
    reactionsConfig: ReactionsConfig,
    var reactionSelectedListener: ReactionSelectedListener? = null
) : PopupWindow(context), View.OnTouchListener {

    var rpListener: Listener? = null
    private val rootView = FrameLayout(context).also {
        it.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
    private val view: ReactionViewGroup by lazy(LazyThreadSafetyMode.NONE) {
        // Lazily inflate content during first display
        ReactionViewGroup(context, reactionsConfig).also {
            it.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )

            it.reactionSelectedListener = reactionSelectedListener

            rootView.addView(it)
        }.also { it.dismissListener = ::dismiss }
    }

    init {
        contentView = rootView
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, mE: MotionEvent): Boolean {
        if (!isShowing) {
            // Show fullscreen with button as context provider
            showAtLocation(v, Gravity.NO_GRAVITY, 0, 0)
            view.show(mE, v)
            rpListener?.isShowing()
        }
        return view.onTouchEvent(mE)
    }

    override fun dismiss() {
        view.dismiss()
        super.dismiss()
    }

    interface Listener {
        fun isShowing()
    }

}