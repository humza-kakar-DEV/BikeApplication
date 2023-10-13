package com.example.smartbikefyp.fragment.splash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartbikefyp.R
import com.example.smartbikefyp.checkPermission
import com.example.smartbikefyp.databinding.FragmentSplashScreenBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.UserRoleType
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreen : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var changeFragment: ChangeFragment? = null

    private var bottomNavigationMenu: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                sharedViewModel.currentUser.collect() {
                    when (it) {
                        is UserRoleType.Exception -> {
                            if (it.message == UserRoleType.Exception.USER_NOT_FOUND_EXCEPTION.message) {
                                changeFragment = ChangeFragment.AUTHENTICATION
                                bottomNavigationMenu = R.menu.authentication_bottom_navigation
                            }
                            sharedViewModel.setUpdateDrawerState(
                                UpdateDrawerInfo(
                                    title = getString(R.string.app_name),
                                    description = "",
                                )
                            )
                        }

                        UserRoleType.Loading -> {
                        }

                        is UserRoleType.Mechanic -> {
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

        binding.welcomeBackButton.setOnClickListener {
            if (requireContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) and requireContext().checkPermission(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            ) {
                changeFragment?.let {
                    sharedViewModel.setChangeBottomMenuState(bottomNavigationMenu!!)
                    sharedViewModel.setChangeFragment(it)
                }
            } else {
                sharedViewModel.setChangeFragment(ChangeFragment.PERMISSION_SCREEN)
            }
        }

        return binding.root
    }

}