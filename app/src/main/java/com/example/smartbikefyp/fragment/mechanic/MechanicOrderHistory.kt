package com.example.smartbikefyp.fragment.mechanic

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartbikefyp.R
import com.example.smartbikefyp.adapter.ProductHistoryRecyclerViewAdapter
import com.example.smartbikefyp.adapter.ServiceHistoryRecyclerViewAdapter
import com.example.smartbikefyp.alterText
import com.example.smartbikefyp.databinding.FragmentMechanicHomeBinding
import com.example.smartbikefyp.databinding.FragmentMechanicOrderHistoryBinding
import com.example.smartbikefyp.databinding.FragmentUserOrderHistoryBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.util.BoughtDataType
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.MechanicOrderHistoryViewModel
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.example.smartbikefyp.viewmodel.UserOrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MechanicOrderHistory : Fragment() {

    private lateinit var binding: FragmentMechanicOrderHistoryBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val mechanicOrderHistoryViewModel: MechanicOrderHistoryViewModel by viewModels()

    private lateinit var productHistoryRecyclerViewAdapter: ProductHistoryRecyclerViewAdapter

    private lateinit var serviceHistoryRecyclerViewAdapter: ServiceHistoryRecyclerViewAdapter

    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMechanicOrderHistoryBinding.inflate(layoutInflater, container, false)

        binding.uohServiceImageView.setBackgroundResource(R.drawable.circle_yellow_bg)

        productHistoryRecyclerViewAdapter = ProductHistoryRecyclerViewAdapter(
            BoughtDataType.MECHANIC,
            requireContext(),
            requireActivity()
        )
        serviceHistoryRecyclerViewAdapter = ServiceHistoryRecyclerViewAdapter(
            BoughtDataType.MECHANIC,
            requireContext(),
            requireActivity()
        )

        binding.uohRecyclerViewLayout.adapter = serviceHistoryRecyclerViewAdapter

        binding.uohRecyclerViewLayout.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        binding.uohProductImageView.setOnClickListener {
            binding.uohServiceImageView.setBackgroundResource(R.drawable.circle_black_stroke_bg)
            binding.uohProductImageView.setBackgroundResource(R.drawable.circle_yellow_bg)
            currentUser?.let {
                mechanicOrderHistoryViewModel.getBoughtProductsWithCreatorId(currentUser?.userId!!)
            }
        }

        binding.uohServiceImageView.setOnClickListener {
            binding.uohServiceImageView.setBackgroundResource(R.drawable.circle_yellow_bg)
            binding.uohProductImageView.setBackgroundResource(R.drawable.circle_black_stroke_bg)
            currentUser?.let {
                mechanicOrderHistoryViewModel.getBoughtServicesWithCreatorId(currentUser?.userId!!)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.currentUser.collect() {

                        if (it is UserRoleType.Mechanic) {
                            currentUser = it.data
                            mechanicOrderHistoryViewModel.getBoughtServicesWithCreatorId(currentUser?.userId!!)
                        }

                    }
                }

//?             product order history
                launch {
                    mechanicOrderHistoryViewModel.boughtProductsState.collect() {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().log("mechanic item bought exception: ${it.message}")
                            }

                            UiState.Loading -> {
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

//?             service order history
                launch {
                    mechanicOrderHistoryViewModel.boughtServicesState.collect() {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().log("mechanic item bought exception: ${it.message}")
                            }

                            UiState.Loading -> {
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