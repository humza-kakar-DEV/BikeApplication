package com.example.smartbikefyp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartbikefyp.databinding.RecyclerViewStoreLayoutBinding
import com.example.smartbikefyp.model.Product
import com.example.smartbikefyp.model.ProductUser
import com.example.smartbikefyp.model.ServiceUser

class UserItemStoreRecyclerViewAdapter<T>(
    val context: Context,
    val callback: (selectedItem: T) -> Unit
) : RecyclerView.Adapter<UserItemStoreRecyclerViewAdapter<T>.ViewHolder>() {

    var items = listOf<T>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: RecyclerViewStoreLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewStoreLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return items.size
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedItem = items.get(position)
        with(holder) {

            itemsLeftToRightAnimation(holder.itemView)

            binding.recyclerViewStoreImageView.setOnClickListener {
                callback(selectedItem)
            }
            when (selectedItem) {
                is ProductUser -> {
                    binding.recyclerViewStoreNameTextView.text = selectedItem.product.name
                    binding.recyclerViewPriceTextView.text = "Rs.${selectedItem.product.price}"
                    Glide
                        .with(context)
                        .load(selectedItem.product.imageUri)
                        .centerCrop()
                        .into(binding.recyclerViewStoreImageView)
                }

                is ServiceUser -> {
                    binding.recyclerViewStoreNameTextView.text = selectedItem.service.name
                    binding.recyclerViewPriceTextView.text = "Rs.${selectedItem.service.price}"
                    Glide
                        .with(context)
                        .load(selectedItem.service.imageUri)
                        .centerCrop()
                        .into(binding.recyclerViewStoreImageView)
                }

                else -> {}
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