package com.example.smartbikefyp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartbikefyp.alterText
import com.example.smartbikefyp.databinding.RecyclerViewServiceHistoryLayoutBinding
import com.example.smartbikefyp.itemInfoAlertDialog
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.model.ItemBoughtService
import com.example.smartbikefyp.util.BoughtDataType

class ServiceHistoryRecyclerViewAdapter(
    private val boughtDataType: BoughtDataType,
    private val context: Context,
    private val activity: FragmentActivity,
) : RecyclerView.Adapter<ServiceHistoryRecyclerViewAdapter.ViewHolder>() {

    var items = listOf<ItemBoughtService>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: RecyclerViewServiceHistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceHistoryRecyclerViewAdapter.ViewHolder {
        val binding = RecyclerViewServiceHistoryLayoutBinding.inflate(
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

    override fun onViewAttachedToWindow(holder: ServiceHistoryRecyclerViewAdapter.ViewHolder) {
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
                            .load(service?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorItemImageImageView)
                        Glide
                            .with(context)
                            .load(creatorUser?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorImageImageView)
                        binding.dateTextView.text = boughtDate?.date
                        binding.timeTextView.text = boughtDate?.time
                        binding.itemHistoryCreatorNameTextView.text = creatorUser?.name
                        binding.itemHistoryCreatorEmailTextView.text = creatorUser?.email
                        binding.itemHistoryCreatorItemNameTextView.text = service?.name
                        binding.itemHistoryCreatorItemDescriptionTextView.text =
                            context.alterText(service?.description.toString())
                        binding.itemHistoryCreatorItemPriceTextView.text = "Rs. ${service?.price}"

                        binding.itemOptionsButton.setOnClickListener {
                            activity.itemInfoAlertDialog<ItemBoughtService>(
                                this,
                                BoughtDataType.USER
                            )
                        }
                    }

                    BoughtDataType.MECHANIC -> {
                        Glide
                            .with(context)
                            .load(service?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorItemImageImageView)
                        Glide
                            .with(context)
                            .load(creatorUser?.imageUri)
                            .centerCrop()
                            .into(binding.itemHistoryCreatorImageImageView)
                        binding.dateTextView.text = boughtDate?.date
                        binding.timeTextView.text = boughtDate?.time
                        binding.itemHistoryCreatorNameTextView.text = creatorUser?.name
                        binding.itemHistoryCreatorEmailTextView.text = creatorUser?.email
                        binding.itemHistoryCreatorItemNameTextView.text = service?.name
                        binding.itemHistoryCreatorItemDescriptionTextView.text =
                            context.alterText(service?.description.toString())
                        binding.itemHistoryCreatorItemPriceTextView.text = "Rs. ${service?.price}"

                        binding.itemOptionsButton.setOnClickListener {
                            activity.itemInfoAlertDialog<ItemBoughtService>(
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