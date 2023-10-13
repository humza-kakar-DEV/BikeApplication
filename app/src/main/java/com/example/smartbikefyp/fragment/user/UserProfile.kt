package com.example.smartbikefyp.fragment.user

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.FragmentUserProfileBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.example.smartbikefyp.viewmodel.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfile : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val userProfileViewModel: UserProfileViewModel by viewModels()

    private var previousUser: User? = null

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                requireContext().log("register activity image: ${imageUri}")
                Glide
                    .with(requireContext())
                    .load(imageUri)
                    .centerCrop()
                    .into(binding.profileImageView)
            }
        }

        binding.createProductEditImageButton.setOnClickListener {
            startForResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel
                    .currentUser
                    .onEach {
                        when (it) {
                            is UserRoleType.Exception -> {}
                            UserRoleType.Loading -> {}
                            is UserRoleType.Mechanic -> {}
                            is UserRoleType.User -> {
                                requireContext().log("data is here: ${it.data}")
                                previousUser = it.data
                                Glide
                                    .with(requireContext())
                                    .load(previousUser?.imageUri)
                                    .centerCrop()
                                    .into(binding.profileImageView)
                                binding.outlinedTextField1.editText?.setText(previousUser?.name)
                                binding.outlinedTextField2.editText?.setText(previousUser?.phoneNumber)
                                binding.outlinedTextField3.editText?.setText(previousUser?.yourAddress)
                                binding.outlinedTextField4.editText?.setText(previousUser?.shopAddress)

                                previousUser?.let {
                                    sharedViewModel
                                        .setUpdateDrawerState(
                                            UpdateDrawerInfo(
                                                imageUri = it.imageUri,
                                                title = it.name,
                                                description = it.email,
                                            )
                                        )
                                }
                            }
                        }
                    }
                    .launchIn(lifecycleScope)
            }
        }

        binding.updateButton.setOnClickListener {
            previousUser?.let {
                userProfileViewModel.updateUserData(
                    User(
                        name = binding.outlinedTextField1.editText?.text?.trim().toString(),
                        phoneNumber = binding.outlinedTextField2.editText?.text?.trim().toString(),
                        yourAddress = binding.outlinedTextField3.editText?.text?.trim().toString(),
                        shopAddress = binding.outlinedTextField4.editText?.text?.trim().toString(),
                        userId = previousUser!!.userId,
                        email = previousUser!!.email,
                        password = previousUser!!.password,
                        role = previousUser!!.role,
                    ),
                    imageUri
                )
            }
        }

        return binding.root
    }
}












