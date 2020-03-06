package com.example.recyclereact

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclereact.reaction.PopupGravity
import com.example.recyclereact.reaction.ReactionPopup
import com.example.recyclereact.reaction.reactionConfig
import kotlinx.android.synthetic.main.layout_common_view.view.*

class ReactionsAdapter : ListAdapter<Item, ReactionsAdapter.BaseViewHolder>(ItemDC()) {

    var listener: ReactionListener? = null
    var playerPosition = -1
    private val reactionTypes =
        intArrayOf(R.drawable.mascot_love, R.drawable.mascot_cool, R.drawable.mascot_star)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == 0) {
            ImageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_image_item, parent, false)
            )
        } else {
            TextViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_text_item, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun swapData(data: ArrayList<Item>) = submitList(data.toMutableList())

    open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var reactionPopup: ReactionPopup? = null

        open fun bind(item: Item) = with(itemView) {
            img_reaction.setImageResource(item.img)

            if (reactionPopup == null) reactionPopup = getReactionPopup(context).apply {
                reactionSelectedListener = { position ->
                    listener?.isShowingChanged(false)
                    val reactionType = if (position > -1) position + 1 else position
                    Log.d("TAG", "TestLog: rT:$reactionType")
                    true
                }
                rpListener = object : ReactionPopup.Listener {
                    override fun isShowing() {
                        listener?.isShowingChanged(true)
                    }
                }
            }

            img_btn_reaction.setOnTouchListener(reactionPopup)

        }

        private fun getReactionPopup(context: Context): ReactionPopup {
            val size = context.resources.getDimensionPixelSize(R.dimen.reaction_item_size)
            val margin = context.resources.getDimensionPixelSize(R.dimen.reaction_item_margin)

            // Config DSL + listener in popup constructor
            val reactionsConfig = reactionConfig(context) {
                reactionsIds = reactionTypes
                reactionSize = size
                horizontalMargin = margin
                verticalMargin = horizontalMargin / 2
                popupGravity = PopupGravity.PARENT_LEFT
            }

            return ReactionPopup(context, reactionsConfig)
        }
    }

    inner class ImageViewHolder(itemView: View) : BaseViewHolder(itemView)

    inner class TextViewHolder(itemView: View) : BaseViewHolder(itemView)

    private class ItemDC : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean = oldItem._id == newItem._id
    }

    interface ReactionListener {
        fun isShowingChanged(isShowing: Boolean)
    }
}