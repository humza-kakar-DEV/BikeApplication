package com.example.smartbikefyp.fragment.splash

import android.Manifest
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
import com.example.smartbikefyp.databinding.FragmentPermissionScreenBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PermissionScreen : Fragment() {

    private lateinit var binding: FragmentPermissionScreenBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val requestPermissions = listOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_MEDIA_IMAGES,
    )

    private var changeFragment: ChangeFragment? = null

    private var bottomNavigationMenu: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionScreenBinding.inflate(layoutInflater, container, false)

        sharedViewModel
            .setUpdateDrawerState(
                UpdateDrawerInfo(
                    title = getString(R.string.app_name),
                    description = ""
                )
            )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                sharedViewModel.currentUser.collect() {
                    when (it) {
                        is UserRoleType.Exception -> {
                            if (it.message == UserRoleType.Exception.USER_NOT_FOUND_EXCEPTION.message) {
                                changeFragment = ChangeFragment.AUTHENTICATION
                                bottomNavigationMenu = R.menu.authentication_bottom_navigation
                            }
//?                         UPDATE UI ON OTHER EXCEPTION
                        }

                        UserRoleType.Loading -> {
//?                         UPDATE UI LOADING
                        }

                        is UserRoleType.Mechanic -> {
                            requireContext().log("mechanic: ${it.data}")
                            changeFragment = ChangeFragment.MECHANIC_HOME
                            bottomNavigationMenu = R.menu.mechanic_bottom_navigation
                            sharedViewModel.setUpdateDrawerState(
                                UpdateDrawerInfo(
                                    imageUri = it.data.imageUri,
                                    title = it.data.name,
                                    description = it.data.email,
                                )
                            )
                        }

                        is UserRoleType.User -> {
                            changeFragment = ChangeFragment.USER_HOME
                            bottomNavigationMenu = R.menu.user_bottom_navigation
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
            }
        }

        binding.allowPermissionButton.setOnClickListener {
            PermissionX.init(requireActivity())
                .permissions(requestPermissions)
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList,
                        "Core fundamental are based on these permissions",
                        "OK",
                        "Cancel"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(
                        deniedList,
                        "You need to allow necessary permissions in Settings manually",
                        "OK",
                        "Cancel"
                    )
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        requireContext().log("all granted called: ${changeFragment?.value}")
                        changeFragment?.let {
                            sharedViewModel.setChangeBottomMenuState(bottomNavigationMenu!!)
                            sharedViewModel.setChangeFragment(it)
                        }
                    }
                }
        }

        return binding.root
    }
}