package com.example.smartbikefyp.fragment.user

import android.net.eap.EapSessionConfig
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Interpolator
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartbikefyp.R
import com.example.smartbikefyp.adapter.ProductHistoryRecyclerViewAdapter
import com.example.smartbikefyp.adapter.ServiceHistoryRecyclerViewAdapter
import com.example.smartbikefyp.databinding.FragmentUserOrderHistoryBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.util.BoughtDataType
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.example.smartbikefyp.viewmodel.UserOrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserOrderHistory : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val userOrderHistoryViewModel: UserOrderHistoryViewModel by viewModels()

    private lateinit var binding: FragmentUserOrderHistoryBinding

    private lateinit var productHistoryRecyclerViewAdapter: ProductHistoryRecyclerViewAdapter

    private lateinit var serviceHistoryRecyclerViewAdapter: ServiceHistoryRecyclerViewAdapter

    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserOrderHistoryBinding.inflate(layoutInflater, container, false)

        productHistoryRecyclerViewAdapter = ProductHistoryRecyclerViewAdapter(
            BoughtDataType.USER,
            requireContext(),
            requireActivity()
        )
        serviceHistoryRecyclerViewAdapter = ServiceHistoryRecyclerViewAdapter(
            BoughtDataType.USER,
            requireContext(),
            requireActivity()
        )

        binding.uohRecyclerViewLayout.adapter = productHistoryRecyclerViewAdapter

        binding.uohRecyclerViewLayout.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.uohServiceImageView.setBackgroundResource(R.drawable.circle_yellow_bg)

        binding.uohProductImageView.setOnClickListener {
            binding.uohServiceImageView.setBackgroundResource(R.drawable.circle_black_stroke_bg)
            binding.uohProductImageView.setBackgroundResource(R.drawable.circle_yellow_bg)
            currentUser?.let {
                userOrderHistoryViewModel.getBoughtProductsWithBuyerId(currentUser?.userId!!)
            }
        }

        binding.uohServiceImageView.setOnClickListener {
            binding.uohServiceImageView.setBackgroundResource(R.drawable.circle_yellow_bg)
            binding.uohProductImageView.setBackgroundResource(R.drawable.circle_black_stroke_bg)
            currentUser?.let {
                userOrderHistoryViewModel.getBoughtServicesWithBuyerId(currentUser?.userId!!)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.currentUser.collect() {
                        if (it is UserRoleType.User) {
                            currentUser = it.data
                            userOrderHistoryViewModel.getBoughtServicesWithBuyerId(currentUser?.userId!!)
                        }
                    }
                }

                launch {
                    userOrderHistoryViewModel.boughtProductsState.collect() {
                        when (it) {

                            is UiState.Exception -> {
                                requireContext().log("item bought exception: ${it.message}")
                            }

                            UiState.Loading -> {
                                requireContext().log("item bought loading")
                            }

                            is UiState.Success -> {
                                it.data?.let {
                                    if (it.isEmpty()) {
                                        binding.uohItemCountTextView.text = 0.toString()
                                        binding.uohRecyclerViewLayout.visibility = View.INVISIBLE
                                        binding.emptyDataIllustrationImageView.visibility =
                                            View.VISIBLE
                                        requireContext().log("product list is empty")
                                        return@let
                                    }

                                    binding.uohRecyclerViewLayout.visibility = View.VISIBLE

                                    binding.emptyDataIllustrationImageView.visibility =
                                        View.INVISIBLE

                                    binding.uohRecyclerViewLayout.adapter =
                                        productHistoryRecyclerViewAdapter

                                    productHistoryRecyclerViewAdapter.items = it

                                    binding.uohItemCountTextView.text = it.size.toString()
                                }
                            }

                        }
                    }
                }

                launch {
                    userOrderHistoryViewModel.boughtServicesState.collect() {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().log("item bought exception: ${it.message}")
                            }

                            UiState.Loading -> {
                                requireContext().log("item bought loading")
                            }

                            is UiState.Success -> {
                                it.data?.let {
                                    if (it.isEmpty()) {
                                        binding.uohItemCountTextView.text = 0.toString()
                                        binding.uohRecyclerViewLayout.visibility = View.INVISIBLE
                                        binding.emptyDataIllustrationImageView.visibility =
                                            View.VISIBLE
                                        return@let
                                    }
                                    binding.uohRecyclerViewLayout.visibility = View.VISIBLE
                                    binding.emptyDataIllustrationImageView.visibility =
                                        View.INVISIBLE

                                    binding.uohRecyclerViewLayout.adapter =
                                        serviceHistoryRecyclerViewAdapter

                                    serviceHistoryRecyclerViewAdapter.items = it

                                    binding.uohItemCountTextView.text = it.size.toString()

                                }
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }
}