package com.example.smartbikefyp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartbikefyp.databinding.VideoRecyclerViewLayoutBinding
import com.example.smartbikefyp.model.VideoDetail

class VideoRecyclerViewAdapter(
    private val context: Context,
    private val callback: (position: Int) -> Unit,
) : RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder>() {

    var items = listOf<VideoDetail>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: VideoRecyclerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoRecyclerViewAdapter.ViewHolder {
        val binding = VideoRecyclerViewLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            itemsLeftToRightAnimation(holder.itemView)

            if (items.isNotEmpty()) {
                items[position].apply {
                    Glide
                        .with(context)
                        .load(thumbnailLink)
                        .centerCrop()
                        .into(binding.imageFilterView)
                    binding.textView1.text = title
                    binding.playButton.setOnClickListener {
                        callback(position)
                    }
                }
            }
        }
    }

    private fun itemsLeftToRightAnimation(itemView: View) {
        itemView.translationX = -100f
        itemView.alpha = 0f
        itemView.animate().translationX(0f).start()
        itemView.animate().alpha(1f).start()
    }

}