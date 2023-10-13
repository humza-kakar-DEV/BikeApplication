package com.example.smartbikefyp.fragment.authentication

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.FragmentSignInBinding
import com.example.smartbikefyp.isValidPassword
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.example.smartbikefyp.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val signInViewModel: SignInViewModel by viewModels()

    private lateinit var emailInput: String
    private lateinit var passwordInput: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        sharedViewModel
            .setUpdateDrawerState(
                UpdateDrawerInfo(
                    title = getString(R.string.app_name),
                    description = ""
                )
            )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    signInViewModel.signInState.collect {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().log("sign in fragment: signInState - ${it.message}")
                            }

                            is UiState.Loading -> {}
                            is UiState.Success -> {
                                sharedViewModel
                                    .currentUser
                                    .drop(1)
                                    .onEach {
                                        when (it) {
                                            is UserRoleType.Exception -> {
                                                requireContext().log("sign in fragment - exception: = ${it.message}")
                                            }

                                            UserRoleType.Loading -> {
//                                                requireContext().log("sign in fragment - loading:")
                                            }

                                            is UserRoleType.Mechanic -> {
//                                                requireContext().log("sign in fragment - mechanic: ${it.data}")
                                                sharedViewModel.setChangeBottomMenuState(R.menu.mechanic_bottom_navigation)
                                                sharedViewModel.setChangeFragment(
                                                    ChangeFragment.MECHANIC_HOME
                                                )
                                                sharedViewModel.setUpdateDrawerState(
                                                    UpdateDrawerInfo(
                                                        imageUri = it.data.imageUri,
                                                        title = it.data.name,
                                                        description = it.data.email
                                                    )
                                                )
                                            }

                                            is UserRoleType.User -> {
//                                                requireContext().log("sign in fragment - user: ${it.data}")
                                                sharedViewModel.setChangeBottomMenuState(R.menu.user_bottom_navigation)
                                                sharedViewModel.setChangeFragment(
                                                    ChangeFragment.USER_HOME
                                                )
                                                sharedViewModel.setUpdateDrawerState(
                                                    UpdateDrawerInfo(
                                                        imageUri = it.data.imageUri,
                                                        title = it.data.name,
                                                        description = it.data.email
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    .launchIn(lifecycleScope)
                            }
                        }
                    }
                }
            }
        }

        binding.signInButton.setOnClickListener {
            if (!validateEmail() or !validatePassword()) {
                return@setOnClickListener
            }
            signInViewModel.signInUserEmailAndPassword(
                email = emailInput,
                password = passwordInput
            )
        }

        return binding.root
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