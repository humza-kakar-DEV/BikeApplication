package com.example.smartbikefyp.adapter

import com.example.smartbikefyp.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.EditText
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartbikefyp.alterText
import com.example.smartbikefyp.databinding.RecyclerViewProductHistoryLayoutBinding
import com.example.smartbikefyp.fragment.global.MainActivity
import com.example.smartbikefyp.itemInfoAlertDialog
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.util.BoughtDataType


class ProductHistoryRecyclerViewAdapter(
    private val boughtDataType: BoughtDataType,
    private val context: Context,
    private val activity: FragmentActivity,
) : RecyclerView.Adapter<ProductHistoryRecyclerViewAdapter.ViewHolder>() {

    var items = listOf<ItemBoughtProduct>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: RecyclerViewProductHistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductHistoryRecyclerViewAdapter.ViewHolder {
        val binding = RecyclerViewProductHistoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onViewAttachedToWindow(holder: ProductHistoryRecyclerViewAdapter.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            leftToRightAnimation(holder.itemView)

            items[position].apply {
                when (boughtDataType) {
                    BoughtDataType.USER -> {
                        Glide
                            .with(context)
                            .load(product?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorItemImageImageView)
                        Glide
                            .with(context)
                            .load(creatorUser?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorImageImageView)
                        binding.itemHistoryCreatorNameTextView.text = creatorUser?.name
                        binding.itemHistoryCreatorEmailTextView.text = creatorUser?.email
                        binding.itemHistoryCreatorItemNameTextView.text = product?.name
                        binding.itemHistoryCreatorItemDescriptionTextView.text =
                            context.alterText(product?.description.toString())
                        binding.itemHistoryCreatorItemPriceTextView.text = "Rs. ${product?.price}"

                        binding.itemOptionsButton.setOnClickListener {
                            activity.itemInfoAlertDialog<ItemBoughtProduct>(
                                this,
                                BoughtDataType.USER
                            )
                        }
                    }

                    BoughtDataType.MECHANIC -> {
                        Glide
                            .with(context)
                            .load(product?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorItemImageImageView)

                        Glide
                            .with(context)
                            .load(buyerUser?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorImageImageView)

                        binding.itemHistoryCreatorNameTextView.text = buyerUser?.name
                        binding.itemHistoryCreatorEmailTextView.text = buyerUser?.email
                        binding.itemHistoryCreatorItemNameTextView.text = product?.name
                        binding.itemHistoryCreatorItemDescriptionTextView.text =
                            context.alterText(product?.description.toString())
                        binding.itemHistoryCreatorItemPriceTextView.text = "Rs. ${product?.price}"

                        binding.itemOptionsButton.setOnClickListener {
                            activity.itemInfoAlertDialog<ItemBoughtProduct>(
                                this,
                                BoughtDataType.MECHANIC
                            )
                        }
                    }
                }
            }
        }
    }

    private fun leftToRightAnimation(itemView: View) {
        itemView.translationX = -100f
        itemView.alpha = 0f
        itemView.animate().translationX(0f).start()
        itemView.animate().alpha(1f).start()
    }

}