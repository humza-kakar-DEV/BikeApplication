package com.example.smartbikefyp.fragment.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.smartbikefyp.R
import com.example.smartbikefyp.adapter.UserItemStoreRecyclerViewAdapter
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.ServiceUser
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.ServiceCategory
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.example.smartbikefyp.viewmodel.UserServiceViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserServiceStore : Fragment() {

    private val userServiceViewModel: UserServiceViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var serviceItemCount: Int = 0

    private var serviceUserList = mutableListOf<ServiceUser>()

    private lateinit var userItemStoreRecyclerViewAdapter: UserItemStoreRecyclerViewAdapter<ServiceUser>

    private val imageList = mutableListOf<SlideModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_service_store, container, false)
        val allChip = view.findViewById<Chip>(R.id.allChip)
        val engineServiceChip = view.findViewById<Chip>(R.id.engineServiceChip)
        val engineOilChangeChip = view.findViewById<Chip>(R.id.engineOilChangeChip)
        val engineRepairChip = view.findViewById<Chip>(R.id.engineRepairChip)
        val brakeServiceChip = view.findViewById<Chip>(R.id.brakeServiceChip)
        val tirePunctureChip = view.findViewById<Chip>(R.id.tirePunctureChip)
        val washingChip = view.findViewById<Chip>(R.id.washingChip)
        val decorationChip = view.findViewById<Chip>(R.id.decorationChip)
        val countTextView = view.findViewById<TextView>(R.id.textView6)

        allChip.isChecked = true

        userItemStoreRecyclerViewAdapter =
            UserItemStoreRecyclerViewAdapter<ServiceUser>(requireContext()) {
                sharedViewModel.setSelectedItemUserState(it)
                sharedViewModel.setChangeFragment(ChangeFragment.ITEM_DETAIL)
            }

//?     place this code in launch coroutine block

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    serviceUserList.clear()
                    userServiceViewModel.serviceUserState.collect() {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().log("service state data: exception ${it.message}}")
                            }

                            UiState.Loading -> {
                                requireContext().log("service state data: loading")
                            }

                            is UiState.Success -> {
                                serviceUserList.add(it.data!!)
                                if (serviceUserList.size == serviceItemCount) {
                                    val userServiceStoreRecyclerView =
                                        view.findViewById<RecyclerView>(R.id.userProductStoreRecyclerView)
                                    val itemCountTextView =
                                        view.findViewById<TextView>(R.id.textView6)
                                    val imageSlider =
                                        view.findViewById<ImageSlider>(R.id.image_slider)

                                    allChip.isChecked = true

                                    userServiceStoreRecyclerView.layoutManager =
                                        GridLayoutManager(requireContext(), 2)

//                                    userProductStoreRecyclerView.adapter =
//                                        AlphaInAnimationAdapter(userItemStoreRecyclerViewAdapter).apply {
//                                            // Change the durations.
//                                            setDuration(1000)
//                                            // Disable the first scroll mode.
//                                            setFirstOnly(false)
//                                        }

//                                    custom animation from adapter
                                    userServiceStoreRecyclerView.adapter =
                                        userItemStoreRecyclerViewAdapter

                                    userItemStoreRecyclerViewAdapter.items = serviceUserList

                                    addDataToImageSlider()
                                    imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

                                    itemCountTextView.text = serviceItemCount.toString()
                                }
                            }
                        }
                    }
                }
                launch {
                    userServiceViewModel.serviceCountState.collect() {
                        it?.let {
                            if (it == 0) {
//                            service is empty
                            } else {
                                serviceItemCount = it
                                userServiceViewModel.getServices()
                            }
                        }
                    }
                }
            }
        }

        allChip.setOnClickListener {
            if (allChip.isChecked) {
                countTextView.text = serviceUserList.size.toString()
                userItemStoreRecyclerViewAdapter.items = serviceUserList
            }
        }

        engineServiceChip.setOnClickListener {
            if (engineServiceChip.isChecked) {
                countTextView.text =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.ENGINE_SERVICE.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.ENGINE_SERVICE.category }
            }
        }

        engineOilChangeChip.setOnClickListener {
            if (engineOilChangeChip.isChecked) {
                countTextView.text =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.ENGINE_OIL_CHANGE.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.ENGINE_OIL_CHANGE.category }
            }
        }

        engineRepairChip.setOnClickListener {
            if (engineRepairChip.isChecked) {
                countTextView.text =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.ENGINE_REPAIR.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.ENGINE_REPAIR.category }
            }
        }

        brakeServiceChip.setOnClickListener {
            if (brakeServiceChip.isChecked) {
                countTextView.text =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.BRAKE_SERVICE.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.BRAKE_SERVICE.category }
            }
        }

        tirePunctureChip.setOnClickListener {
            if (tirePunctureChip.isChecked) {
                countTextView.text =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.TIRE_PUNCTURE.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.TIRE_PUNCTURE.category }
            }
        }

        washingChip.setOnClickListener {
            if (washingChip.isChecked) {
                countTextView.text =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.WASHING.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.WASHING.category }
            }
        }

        decorationChip.setOnClickListener {
            if (decorationChip.isChecked) {
                countTextView.text =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.DECORATION.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    serviceUserList.filter { it.service.serviceCategory == ServiceCategory.DECORATION.category }
            }
        }

        return view.rootView
    }

    private fun addDataToImageSlider() {
        imageList.clear()
        imageList.add(SlideModel(R.drawable.bike_service_image_4, ""))
        imageList.add(SlideModel(R.drawable.bike_service_image_2, ""))
        imageList.add(SlideModel(R.drawable.bike_service_image_1, ""))
        imageList.add(SlideModel(R.drawable.bike_service_image_5, ""))
        imageList.add(SlideModel(R.drawable.bike_service_image_6, ""))
    }
}