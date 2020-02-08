package com.example.recyclereact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_common_view.view.*
import kotlinx.android.synthetic.main.layout_text_item.view.*

class ReactionsAdapter : ListAdapter<Item, ReactionsAdapter.BaseViewHolder>(ItemDC()) {

    var listener: ReactionListener? = null
    var lastPosition = -1

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
        open fun bind(item: Item) {
            itemView.img_reaction.setImageResource(item.img)
        }
    }

    inner class ImageViewHolder(itemView: View) : BaseViewHolder(itemView)

    inner class TextViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(item: Item) {
            super.bind(item)
            itemView.txt_reaction_id.text = item._id
        }
    }

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
        fun onItemTouch()
    }
}