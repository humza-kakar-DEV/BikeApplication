package com.example.smartbikefyp.fragment.mechanic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.FragmentMechanicHomeBinding
import com.example.smartbikefyp.databinding.FragmentUserHomeBinding
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MechanicHome : Fragment() {

    lateinit var binding: FragmentMechanicHomeBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMechanicHomeBinding.inflate(layoutInflater, container, false)

        binding.productStoreStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.MECHANIC_PRODUCT)
        }

        binding.serviceStoreStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.MECHANIC_SERVICE)
        }

        binding.chatBotStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.CHAT_BOT)
        }

        binding.mapStartImageView.setOnClickListener {
            sharedViewModel.setChangeFragment(ChangeFragment.OPEN_MAP)
        }

        return binding.root
    }
}