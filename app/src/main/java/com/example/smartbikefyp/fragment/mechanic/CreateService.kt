package com.example.smartbikefyp.fragment.mechanic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.FragmentCreateServiceBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.Service
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.util.ServiceCategory
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.CreateServiceViewModel
import com.example.smartbikefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateService : Fragment() {

    private lateinit var binding: FragmentCreateServiceBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val createServiceViewModel: CreateServiceViewModel by viewModels()

    private var user: User? = null

    private lateinit var name: String

    private lateinit var description: String

    private lateinit var price: String

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateServiceBinding.inflate(layoutInflater, container, false)

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                binding.createProductImageImageView.setImageURI(imageUri)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    sharedViewModel.currentUser.collect() {
                        when (it) {
                            is UserRoleType.Exception -> {}
                            is UserRoleType.Mechanic -> {
                                user = it.data
                                context?.log("current user: ${user}")
                            }

                            is UserRoleType.User -> {
                                user = it.data
                                context?.log("current user: ${user}")
                            }

                            UserRoleType.Loading -> {
                            }
                        }
                    }
                }
                launch {
                    createServiceViewModel.createServiceState.collect() {
                        when (it) {
                            is UiState.Exception -> {
                                context?.log("create service state: Exception ${it.message}")
                            }

                            UiState.Loading -> {
                                context?.log("create service state: Loading")
                            }

                            is UiState.Success -> {
                                it.data?.let {
                                    if (it) {
                                        context?.log("create service state: Success ${it}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        ArrayAdapter(
            requireContext(),
            R.layout.item_category_menu_layout,
            resources.getStringArray(R.array.service_category_menu)
        ).apply {
            binding.serviceCategoryMenuAutoCompleteTextView.setAdapter(this)
        }

        binding.serviceCategoryMenuAutoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            resources.getStringArray(R.array.service_category_menu).get(i)
        }

        binding.createProductEditImageButton.setOnClickListener {
            binding.createProductCameraImageView.visibility = View.GONE
            startForResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
            binding.createProductImageImageView.setImageURI(imageUri)
        }

        binding.createProductButton.setOnClickListener {
            user?.let {
                if (!validateName() or !validateDescription() or !validatePrice() or !validateImageUri()) {
                    return@setOnClickListener
                }
                createServiceViewModel.createService(
                    Service(
                        name = binding.nameTextInput.editText?.text?.trim().toString(),
                        description = binding.descriptionTextInput.editText?.text?.trim().toString(),
                        price = binding.priceTextInput.editText?.text?.trim().toString(),
                        serviceCategory = binding.serviceCategoryMenuAutoCompleteTextView.text.toString(),
                        creatorId = user?.userId!!,
                    ),
                    imageUri!!
                )
            }
        }

        return binding.root
    }

    private fun validateImageUri() = if (imageUri == null) {
        requireContext().toast("Please select an image!")
        false
    } else {
        true
    }

    private fun validateName(): Boolean {
        name = binding.nameTextInput.editText?.text.toString().trim()
        return if (name.isEmpty()) {
            binding.nameTextInput.error = "Field can't be empty"
            false
        } else if (name.length > 15) {
            binding.nameTextInput.error = "Username too long"
            false
        } else {
            binding.nameTextInput.error = null
            true
        }
    }

    private fun validateDescription(): Boolean {
        description = binding.descriptionTextInput.editText?.text.toString().trim()
        return if (description.isEmpty()) {
            binding.descriptionTextInput.error = "Field can't be empty"
            false
        } else {
            binding.descriptionTextInput.setError(null)
            true
        }
    }

    private fun validatePrice(): Boolean {
        price = binding.priceTextInput.editText?.text.toString().trim()
        return if (price.isEmpty()) {
            binding.priceTextInput.error = "Field can't be empty"
            false
        } else {
            binding.priceTextInput.error = null
            true
        }
    }

    override fun onStart() {
        super.onStart()
        if (imageUri == null) {
            binding.createProductCameraImageView.visibility = View.VISIBLE
        } else {
            binding.createProductImageImageView.setImageURI(imageUri)
        }
    }

}























