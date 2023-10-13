package com.example.smartbikefyp.fragment.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.smartbikefyp.databinding.FragmentUserHomeBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserHome : Fragment() {

    private lateinit var binding: FragmentUserHomeBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserHomeBinding.inflate(layoutInflater, container, false)

        binding.productStoreStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.USER_PRODUCT_STORE)
        }

        binding.chatBotStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.CHAT_BOT)
        }

        binding.mapStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.OPEN_MAP)
        }

        binding.serviceStoreStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.USER_SERVICE_STORE)
        }

        binding.bikeDiagnoseButton.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.BIKE_DIAGNOSE)
        }

        return binding.root
    }
}