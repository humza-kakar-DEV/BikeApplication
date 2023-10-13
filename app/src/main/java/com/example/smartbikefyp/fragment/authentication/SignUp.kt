package com.example.smartbikefyp.fragment.authentication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.FragmentSignUpBinding
import com.example.smartbikefyp.isValidPassword
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.example.smartbikefyp.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val signUpViewModel: SignUpViewModel by viewModels()

    private var imageUri: Uri? = null

    private lateinit var usernameInput: String
    private lateinit var phoneNumber: String
    private lateinit var yourAddress: String
    private lateinit var shopAddress: String
    private lateinit var emailInput: String
    private lateinit var passwordInput: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        ArrayAdapter(
            requireContext(),
            R.layout.auth_role_menu_layout,
            resources.getStringArray(R.array.auth_role_menu)
        ).apply {
            binding.authRoleMenuAutoCompleteTextView.setAdapter(this)
        }

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                binding.profileImageView.setImageURI(imageUri)
            }
        }

        binding.createProductEditImageButton.setOnClickListener {
            binding.editPlaceHolderImageView.visibility = View.GONE
            startForResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
            binding.profileImageView.setImageURI(imageUri)
        }

        sharedViewModel
            .setUpdateDrawerState(
                UpdateDrawerInfo(
                    title = getString(R.string.app_name),
                    description = ""
                )
            )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.signUpState.collect() {
                    when (it) {
                        is UiState.Exception -> {
                            context?.log("sign up state: exception -> ${it.message}")
                        }

                        UiState.Loading -> {
                        }

                        is UiState.Success -> {
                            delay(1000)
                            it.data?.let {
                                sharedViewModel.currentUser
                                    .onEach {
                                        when (it) {
                                            is UserRoleType.Exception -> {
                                                requireContext().log("sign in fragment - current user: exception = ${it.message}")
                                            }

                                            UserRoleType.Loading -> {
                                            }

                                            is UserRoleType.Mechanic -> {
                                                requireContext().log("current user: ${it.data}")
                                                sharedViewModel.setChangeBottomMenuState(R.menu.mechanic_bottom_navigation)
                                                sharedViewModel.setChangeFragment(ChangeFragment.MECHANIC_HOME)
                                                sharedViewModel.setUpdateDrawerState(
                                                    UpdateDrawerInfo(
                                                        imageUri = it.data.imageUri,
                                                        title = it.data.name,
                                                        description = it.data.email,
                                                    )
                                                )
                                            }

                                            is UserRoleType.User -> {
                                                requireContext().log("current user: ${it.data}")
                                                sharedViewModel.setChangeBottomMenuState(R.menu.user_bottom_navigation)
                                                sharedViewModel.setChangeFragment(ChangeFragment.USER_HOME)
                                                sharedViewModel.setUpdateDrawerState(
                                                    UpdateDrawerInfo(
                                                        imageUri = it.data.imageUri,
                                                        title = it.data.name,
                                                        description = it.data.email,
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    .launchIn(this)
                            }
                        }
                    }
                }
            }
        }

        binding.signUpButton.setOnClickListener {
            if (!validateEmail() or !validatePassword() or !validateUsername() or !validateShopAddress() or !validateYourAddress() or !validatePhoneNumber() or !validateImageUri()) {
                return@setOnClickListener
            }
            signUpViewModel.signUpUserEmailAndPassword(
                User(
                    name = usernameInput,
                    phoneNumber = phoneNumber,
                    yourAddress = yourAddress,
                    shopAddress = shopAddress,
                    email = emailInput,
                    password = passwordInput,
                    role = binding.authRoleMenuAutoCompleteTextView.text.toString(),
                ),
                imageUri!!
            )
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (imageUri == null) {
            binding.editPlaceHolderImageView.visibility = View.VISIBLE
        } else {
            binding.profileImageView.setImageURI(imageUri)
        }
    }

    private fun validateUsername(): Boolean {
        usernameInput = binding.userNameTextInput.getEditText()?.getText().toString().trim()
        return if (usernameInput.isEmpty()) {
            binding.userNameTextInput.setError("Field can't be empty")
            false
        } else if (usernameInput.length > 15) {
            binding.userNameTextInput.setError("Username too long")
            false
        } else {
            binding.userNameTextInput.setError(null)
            true
        }
    }

    private fun validatePhoneNumber(): Boolean {
        phoneNumber = binding.phoneNumberTextInput.getEditText()?.getText().toString().trim()
        return if (phoneNumber.isEmpty()) {
            binding.phoneNumberTextInput.setError("Field can't be empty")
            false
        } else {
            binding.phoneNumberTextInput.setError(null)
            true
        }
    }

    private fun validateYourAddress(): Boolean {
        yourAddress = binding.yourAddressTextInput.getEditText()?.getText().toString().trim()
        return if (yourAddress.isEmpty()) {
            binding.yourAddressTextInput.setError("Field can't be empty")
            false
        } else {
            binding.yourAddressTextInput.setError(null)
            true
        }
    }

    private fun validateShopAddress(): Boolean {
        shopAddress = binding.shopAddressTextInput.getEditText()?.getText().toString().trim()
        return if (shopAddress.isEmpty()) {
            binding.shopAddressTextInput.setError("Field can't be empty")
            false
        } else {
            binding.shopAddressTextInput.setError(null)
            true
        }
    }

    private fun validateEmail(): Boolean {
        emailInput = binding.emailTextInput.getEditText()?.getText().toString().trim()
        return if (emailInput.isEmpty()) {
            binding.emailTextInput.setError("Field can't be empty")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.emailTextInput.setError("Please enter a valid email address")
            false
        } else {
            binding.emailTextInput.setError(null)
            true
        }
    }

    private fun validateImageUri(): Boolean {
        return if (imageUri == null) {
            requireContext().toast("Please select an image!")
            false
        } else {
            true
        }
    }

    private fun validatePassword(): Boolean {
        passwordInput = binding.passwordTextInput.editText!!.text.toString().trim()
        return if (passwordInput.isEmpty()) {
            binding.passwordTextInput.error = "Field can't be empty"
            false
        } else if (!passwordInput.isValidPassword()) {
            binding.passwordTextInput.error = "password is too weak!"
            return false
        } else {
            binding.passwordTextInput.error = null
            true
        }
    }

}