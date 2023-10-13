package com.example.smartbikefyp.fragment.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.smartbikefyp.convertTo12Hr
import com.example.smartbikefyp.databinding.FragmentItemDetailBinding
import com.example.smartbikefyp.model.BoughtDate
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.model.ItemBoughtService
import com.example.smartbikefyp.model.ProductUser
import com.example.smartbikefyp.model.ServiceUser
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.purchaseAlertDialog
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.ItemDetailViewModel
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ItemDetail : Fragment() {

    private lateinit var binding: FragmentItemDetailBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val itemDetailViewModel: ItemDetailViewModel by viewModels()

    private var currentBuyerUser: User? = null

    private var productUser: ProductUser? = null

    private var serviceUser: ServiceUser? = null

    private var boughtDate = BoughtDate()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailBinding.inflate(layoutInflater, container, false)

        binding.itemDetailBuyButton.setOnClickListener {
            currentBuyerUser?.let {
                productUser?.let {
                    itemDetailViewModel.buyProduct(
                        ItemBoughtProduct(
                            buyerId = currentBuyerUser!!.userId,
                            creatorId = productUser!!.user.userId,
                            buyerUser = currentBuyerUser!!,
                            creatorUser = productUser!!.user,
                            product = productUser!!.product
                        )
                    )
                    lifecycleScope.launch {
                        delay(500)
                        requireActivity().purchaseAlertDialog()
                    }
                }
                serviceUser?.let {
                    openDateAndTimePicker {
                        itemDetailViewModel.buyService(
                            ItemBoughtService(
                                buyerId = currentBuyerUser!!.userId,
                                creatorId = serviceUser!!.user.userId,
                                buyerUser = currentBuyerUser!!,
                                creatorUser = serviceUser!!.user,
                                service = serviceUser!!.service,
                                boughtDate = boughtDate
                            )
                        )
                        lifecycleScope.launch {
                            delay(500)
                            requireActivity().purchaseAlertDialog()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    sharedViewModel.selectedItemUserState.collect {
                        it?.let {
                            when (it) {
                                is ProductUser -> {
                                    it.apply {
                                        productUser = it
                                        Glide
                                            .with(requireContext())
                                            .load(product.imageUri)
                                            .centerCrop()
                                            .into(binding.itemDetailProductImageView)
                                        binding.itemDetailNameTextView.text = product.name
                                        binding.itemDetailPriceTextView.text = "Rs.${product.price}"
                                        binding.itemDetailDescriptionTextView.text =
                                            product.description
                                        binding.categoryContainerDataTextView.text =
                                            product.productCategory
                                        binding.idContainerDataTextView.text = product.productId

                                        Glide
                                            .with(requireContext())
                                            .load(user.imageUri)
                                            .centerCrop()
                                            .into(binding.shapeableImageView2)
                                        binding.textView4.text = user.name
                                        binding.textView5.text = user.email
                                        binding.textview16.text = user.phoneNumber
                                        binding.textview18.text = user.name
                                        binding.textview20.text = user.shopAddress
                                        binding.textview22.text = user.yourAddress
                                    }
                                }

                                is ServiceUser -> {
                                    it.apply {
                                        serviceUser = this
                                        Glide
                                            .with(requireContext())
                                            .load(service.imageUri)
                                            .centerCrop()
                                            .into(binding.itemDetailProductImageView)

                                        binding.itemDetailNameTextView.text = service.name
                                        binding.itemDetailPriceTextView.text = "Rs.${service.price}"
                                        binding.itemDetailDescriptionTextView.text =
                                            service.description
                                        binding.categoryContainerDataTextView.text =
                                            service.serviceCategory
                                        binding.idContainerDataTextView.text = service.serviceId

                                        Glide
                                            .with(requireContext())
                                            .load(user.imageUri)
                                            .centerCrop()
                                            .into(binding.shapeableImageView2)
                                        binding.textView4.text = user.name
                                        binding.textView5.text = user.email
                                        binding.textview16.text = user.phoneNumber
                                        binding.textview18.text = user.name
                                        binding.textview20.text = user.shopAddress
                                        binding.textview22.text = user.yourAddress
                                    }
                                }
                            }
                        }
                    }
                }
                launch {
                    sharedViewModel.currentUser.collect {
                        when (it) {
                            is UserRoleType.Exception -> {}
                            UserRoleType.Loading -> {}
                            is UserRoleType.Mechanic -> {}
                            is UserRoleType.User -> {
                                currentBuyerUser = it.data
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun openDateAndTimePicker(callback: () -> Unit) {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select appointment date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        datePicker.show(requireActivity().supportFragmentManager, "Date.Picker")

        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            boughtDate.date = sdf.format(it)
            openTime() {
                callback()
            }
        }
    }

    private fun openTime(callback: () -> Unit) {
        val timePicker =
            MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

        timePicker.show(requireActivity().supportFragmentManager, "Time.Picker")

        timePicker.addOnPositiveButtonClickListener {
            boughtDate.apply {
                time = context?.convertTo12Hr("${timePicker.hour}:${timePicker.minute}")
                    ?: "${timePicker.hour}:${timePicker.minute}"
            }
            callback()
        }
    }

}