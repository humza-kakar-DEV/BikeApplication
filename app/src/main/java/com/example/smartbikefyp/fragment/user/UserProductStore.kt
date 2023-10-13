package com.example.smartbikefyp.fragment.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.smartbikefyp.R
import com.example.smartbikefyp.adapter.UserItemStoreRecyclerViewAdapter
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.ProductUser
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.ProductCategory
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.example.smartbikefyp.viewmodel.UserProductStoreViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UserProductStore : Fragment() {

    private lateinit var userItemStoreRecyclerViewAdapter: UserItemStoreRecyclerViewAdapter<ProductUser>

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val userProductStoreViewModel: UserProductStoreViewModel by viewModels()

    private val productUserList = mutableListOf<ProductUser>()

    private var imageList = mutableListOf<SlideModel>()

    private var productItemsCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_product_store, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.userProductStoreRecyclerView)
        val itemCountTextView = view.findViewById<TextView>(R.id.textView6)
        val allChip = view.findViewById<Chip>(R.id.allChip)
        val engineChip = view.findViewById<Chip>(R.id.engineChip)
        val lightsChip = view.findViewById<Chip>(R.id.lightsChip)
        val engineOilChip = view.findViewById<Chip>(R.id.engineOilChip)
        val tiresChip = view.findViewById<Chip>(R.id.tiresChip)
        val enginePartsChip = view.findViewById<Chip>(R.id.enginePartsChip)

        userItemStoreRecyclerViewAdapter =
            UserItemStoreRecyclerViewAdapter<ProductUser>(requireContext()) {
                sharedViewModel.setSelectedItemUserState(it)
                sharedViewModel.setChangeFragment(ChangeFragment.ITEM_DETAIL)
            }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch(Dispatchers.Main) {
                    productUserList.clear()
                    userProductStoreViewModel.productUserState.collect() {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().log("product store exception: ${it.message}")
                            }

                            UiState.Loading -> {
                                requireContext().log("product store LOADING")
                            }

                            is UiState.Success -> run {
                                productUserList.add(it.data!!)
                                if (productUserList.size == productItemsCount) {
                                    requireContext().log("called!")

                                    val countTextView = view.findViewById<TextView>(R.id.textView6)
                                    val imageSlider =
                                        view.findViewById<ImageSlider>(R.id.image_slider)

                                    allChip.isChecked = true

                                    recyclerView.layoutManager =
                                        GridLayoutManager(requireContext(), 2)

//                                    recyclerView.adapter =
//                                        AlphaInAnimationAdapter(userItemStoreRecyclerViewAdapter).apply {
//                                            // Change the durations.
//                                            setDuration(1000)
//                                            // Disable the first scroll mode.
//                                            setFirstOnly(false)
//                                        }

//                                    custom animation from adapter
                                    recyclerView.adapter = userItemStoreRecyclerViewAdapter

                                    userItemStoreRecyclerViewAdapter.items = productUserList

                                    addDataToImageSlider()
                                    imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)

                                    countTextView.text = productUserList.size.toString()
                                }
                            }
                        }
                    }
                }

                launch {
                    userProductStoreViewModel.productCountState.collect() {
                        it?.let {
                            if (it == 0) {
//                                nothing in product
                            } else {
//                                there are services
                                productItemsCount = it
                                userProductStoreViewModel.getProducts()
                            }
                        }
                    }
                }
            }
        }

        allChip.isChecked = true
        allChip.setOnClickListener {
            if (allChip.isChecked) {
                itemCountTextView.text = productUserList.size.toString()
                userItemStoreRecyclerViewAdapter.items = productUserList
            }
        }

        engineChip.setOnClickListener {
            if (engineChip.isChecked) {
                itemCountTextView.text =
                    productUserList.filter { it.product.productCategory == ProductCategory.ENGINE.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    productUserList.filter { it.product.productCategory == ProductCategory.ENGINE.category }
            }
        }
//
        engineOilChip.setOnClickListener {
            if (engineOilChip.isChecked) {
                itemCountTextView.text =
                    productUserList.filter { it.product.productCategory == ProductCategory.ENGINE_OIL.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    productUserList.filter { it.product.productCategory == ProductCategory.ENGINE_OIL.category }
            }
        }
//
        enginePartsChip.setOnClickListener {
            if (enginePartsChip.isChecked) {
                itemCountTextView.text =
                    productUserList.filter { it.product.productCategory == ProductCategory.ENGINE_PARTS.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    productUserList.filter { it.product.productCategory == ProductCategory.ENGINE_PARTS.category }
            }
        }
//
        lightsChip.setOnClickListener {
            if (lightsChip.isChecked) {
                itemCountTextView.text =
                    productUserList.filter { it.product.productCategory == ProductCategory.LIGHTS.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    productUserList.filter { it.product.productCategory == ProductCategory.LIGHTS.category }
            }
        }
//
        tiresChip.setOnClickListener {
            if (tiresChip.isChecked) {
                itemCountTextView.text =
                    productUserList.filter { it.product.productCategory == ProductCategory.TIRES.category }.size.toString()
                userItemStoreRecyclerViewAdapter.items =
                    productUserList.filter { it.product.productCategory == ProductCategory.TIRES.category }
            }
        }

        return view.rootView
    }

    private fun addDataToImageSlider() {
        imageList.clear()
        imageList.add(SlideModel(R.drawable.bike_part_image_1, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_3, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_4, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_5, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_6, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_7, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_8, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_9, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_10, ""))
        imageList.add(SlideModel(R.drawable.bike_part_image_11, ""))
    }
}