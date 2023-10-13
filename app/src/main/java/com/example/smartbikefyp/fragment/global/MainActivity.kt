package com.example.smartbikefyp.fragment.global

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.plantsservicefyp.MapRequest
import com.example.smartbikefyp.R
import com.example.smartbikefyp.changeMenuLayout
import com.example.smartbikefyp.databinding.ActivityMainBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.navAlertDialog
import com.example.smartbikefyp.navAlertDialogCloseApplication
import com.example.smartbikefyp.updateDrawerNavigation
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.NavAlertDialogType
import com.example.smartbikefyp.viewmodel.SharedViewModel
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


//! V.V.IMP
//? MOTION LAYOUT DIRECT CHILD'S ELEMENTS CANNOT CHANGE VISIBILITY AT RUN TIME
//? TO REMOVE START DESTINATION OF CURRENT INFLATED NAV GRAPH FROM BACK STACK, ADD THE ID OF START DESTINATION INTO POP UP INCLUSIVE WITH TRUE

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var mapRequest: MapRequest

    private lateinit var navController: NavController

    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        navHostFragment = (supportFragmentManager
            .findFragmentById(binding.fragmentContainerView.id)
                as NavHostFragment)
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.appBarTextView.text = destination.label.toString()
            if (destination.id == R.id.userHome) {
                binding.motionLayout.transitionToState(R.id.start)
                binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener(this)

        binding.appBarImageView.setOnClickListener {
            if (navController.currentBackStackEntry?.destination?.id == R.id.userProductStore) {
                sharedViewModel.setChangeFragment(ChangeFragment.USER_HOME)
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.bikeDiagnoseFragment) {
                binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)
                binding.motionLayout.transitionToState(R.id.start)
                window.navigationBarColor = Color.WHITE
                navController.popBackStack()
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.videoPlayerFragment) {
                navController.popBackStack()
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.chatBot) {
                sharedViewModel.setChangeFragment(ChangeFragment.USER_HOME)
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.mapFragment) {
                sharedViewModel.setChangeFragment(ChangeFragment.USER_HOME)
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.userServiceStore) {
                sharedViewModel.setChangeFragment(ChangeFragment.USER_HOME)
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.createProduct) {
                binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)
                binding.motionLayout.transitionToState(R.id.start)
                navController.popBackStack()
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.createService) {
                binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)
                binding.motionLayout.transitionToState(R.id.start)
                navController.popBackStack()
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.chatBot) {
                binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)
                binding.motionLayout.transitionToState(R.id.start)
                navController.popBackStack()
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.mapFragment) {
                binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)
                binding.motionLayout.transitionToState(R.id.start)
                sharedViewModel.setChangeBottomMenuState(R.menu.mechanic_bottom_navigation)
                sharedViewModel.setChangeFragment(ChangeFragment.MECHANIC_HOME)
                return@setOnClickListener
            }
            if (navController.currentBackStackEntry?.destination?.id == R.id.itemDetail) {
                navController.popBackStack()
                return@setOnClickListener
            }

            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START))
                binding.drawerLayout.openDrawer(GravityCompat.START)
            else
                binding.drawerLayout.openDrawer(GravityCompat.END)

        }

        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    sharedViewModel
                        .updateDrawerInfoState
                        .collect() {
                            it?.let {
                                binding.navigationView.updateDrawerNavigation(this@MainActivity, it)
                            }
                        }
                }

                launch(Dispatchers.Main) {
                    sharedViewModel.changeBottomMenuState.collect() {
                        it?.let {
                            binding.bottomNavigationView2.changeMenuLayout(it)
                        }
                    }
                }

                launch {
                    sharedViewModel.observeChangeFragment.collect { changeFragment ->
                        navController.currentDestination?.let { currentDestination ->
                            when (changeFragment) {
                                ChangeFragment.USER_PRODUCT_STORE -> run {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.userHome) {
                                        navController.navigate(R.id.action_userHome_to_userProductStore)
                                        return@run
                                    }
                                }

                                ChangeFragment.USER_SERVICE_STORE -> {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                    navController.navigate(R.id.action_userHome_to_userServiceStore)
                                }

                                ChangeFragment.USER_HOME -> run {
                                    binding.appBarContainer.visibility = View.VISIBLE
                                    binding.bottomNavigationView2.visibility = View.VISIBLE
                                    binding.motionLayout.transitionToState(R.id.start)
                                    binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)

                                    if (navController.currentBackStackEntry?.destination?.id == R.id.splashScreen) {
                                        navController.navigate(R.id.action_splashScreen_to_user_nav_graph)
                                        return@run
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.signUp) {
                                        navController.navigate(R.id.action_signUp_to_user_nav_graph)
                                        return@run
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.signIn) {
                                        navController.navigate(R.id.action_signIn_to_user_nav_graph)
                                        return@run
                                    }
                                    navController.popBackStack()
                                }

                                ChangeFragment.CHAT_BOT -> {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.userHome) {
                                        navController.navigate(R.id.action_userHome_to_chatBot)
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicHome) {
                                        navController.navigate(R.id.action_mechanicHome_to_chatBot)
                                    }
                                }

                                ChangeFragment.OPEN_MAP -> {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.userHome) {
                                        navController.navigate(R.id.action_userHome_to_mapFragment)
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicHome) {
                                        navController.navigate(R.id.action_mechanicHome_to_mapFragment)
                                    }
                                }

                                ChangeFragment.ITEM_DETAIL -> run {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.userProductStore) {
                                        navController.navigate(R.id.action_userProductStore_to_itemDetail)
                                        return@run
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.userServiceStore) {
                                        navController.navigate(R.id.action_userServiceStore_to_itemDetail)
                                        return@run
                                    }
                                }

                                ChangeFragment.AUTHENTICATION -> run {
                                    binding.appBarContainer.visibility = View.VISIBLE
                                    binding.bottomNavigationView2.visibility = View.VISIBLE
                                    binding.motionLayout.transitionToState(R.id.start)
                                    binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)

                                    if (navController.currentBackStackEntry?.destination?.id == R.id.permissionScreen) {
                                        navController.navigate(R.id.action_permissionScreen_to_authentication_nav_graph)
                                        return@run
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.splashScreen) {
                                        navController.navigate(R.id.action_splashScreen_to_authentication_nav_graph)
                                        return@run
                                    }
                                    sharedViewModel.logout()
                                    binding.bottomNavigationView2.changeMenuLayout(R.menu.authentication_bottom_navigation)
                                    navController.navigate(
                                        R.id.authentication_nav_graph,
                                        null,
                                        NavOptions
                                            .Builder()
                                            .setExitAnim(R.anim.custom_slide_out_left)
                                            .setEnterAnim(R.anim.custom_slide_in_right)
                                            .setPopUpTo(
                                                navController.currentBackStackEntry!!.destination.id,
                                                inclusive = true
                                            )
                                            .build()
                                    )
                                }

                                ChangeFragment.MECHANIC_HOME -> run {
                                    binding.bottomNavigationView2.visibility = View.VISIBLE
                                    binding.appBarContainer.visibility = View.VISIBLE
                                    binding.motionLayout.transitionToState(R.id.start)
                                    binding.appBarImageView.setImageResource(R.drawable.hamburger_navigation_icon_one_32)
//                                    binding.bottomNavigationView2.setupWithNavController(
//                                        navController
//                                    )
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.mapFragment) {
                                        navController.popBackStack()
                                        return@run
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.createProduct) {
                                        navController.popBackStack()
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.createService) {
                                        navController.popBackStack()
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.splashScreen) {
                                        navController.navigate(R.id.action_splashScreen_to_mechanic_nav_graph)
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.permissionScreen) {
                                        navController.navigate(R.id.action_permissionScreen_to_mechanic_nav_graph)
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.signIn) {
                                        navController.navigate(R.id.action_signIn_to_mechanic_nav_graph)
                                    }
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.signUp) {
                                        navController.navigate(R.id.action_signUp_to_mechanic_nav_graph)
                                    }
                                }

                                ChangeFragment.MECHANIC_PRODUCT -> {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicHome) {
                                        binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                        navController.navigate(R.id.action_mechanicHome_to_createProduct)
                                    }
                                }

                                ChangeFragment.MECHANIC_SERVICE -> {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicHome) {
                                        binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                        navController.navigate(R.id.action_mechanicHome_to_createService)
                                    }
                                }

                                ChangeFragment.SPLASH_SCREEN -> {

                                }

                                ChangeFragment.PERMISSION_SCREEN -> {
                                    navController.navigate(R.id.action_splashScreen_to_permissionScreen)
                                }

                                ChangeFragment.BIKE_DIAGNOSE -> {
//!                             BIKE DIAGNOSE V.V.IMP
                                    binding.motionLayout.transitionToState(R.id.end)
                                    window.navigationBarColor = Color.BLACK
                                    binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                    navController.navigate(R.id.action_global_bikeDiagnoseFragment)
                                }

                                ChangeFragment.VIDEO_PLAYER_FRAGMENT -> {
                                    binding.motionLayout.transitionToState(R.id.end)
                                    window.navigationBarColor = Color.BLACK
                                    binding.appBarImageView.setImageResource(R.drawable.baseline_keyboard_backspace_30)
                                    navController.navigate(R.id.action_global_videoPlayerFragment)
                                }
                            }
                        }
                    }
                }

            }
        }

        binding.bottomNavigationView2.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.userHomeBottomNavigation -> run {
                        if (navController.currentBackStackEntry?.destination?.id == R.id.userHome) return@run
                        if (navController.currentBackStackEntry?.destination?.id == R.id.userOrderHistory) {
                            navController.navigate(R.id.action_userOrderHistory_to_userHome)
                        } else {
                            navController.navigate(R.id.action_userProfile_to_userHome)
                        }
                    }

                    R.id.userOrderHistoryBottomNavigation -> run {
                        if (navController.currentBackStackEntry?.destination?.id == R.id.userOrderHistory) return@run
                        if (navController.currentBackStackEntry?.destination?.id == R.id.userHome) {
                            navController.navigate(R.id.action_userHome_to_userOrderHistory)
                        } else {
                            navController.navigate(R.id.action_userProfile_to_userOrderHistory)
                        }
                    }

                    R.id.userProfileBottomNavigation -> run {
                        if (navController.currentBackStackEntry?.destination?.id == R.id.userProfile) return@run
                        if (navController.currentBackStackEntry?.destination?.id == R.id.userHome) {
                            navController.navigate(R.id.action_userHome_to_userProfile)
                        } else {
                            navController.navigate(R.id.action_userOrderHistory_to_userProfile)
                        }
                    }

                    R.id.mechanicHomeBottomNavigation -> run {
                        if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicHome) return@run
                        navController.navigate(
                            R.id.mechanicHome,
                            null,
                            NavOptions
                                .Builder()
                                .setEnterAnim(R.anim.custom_slide_in_left)
                                .setExitAnim(R.anim.custom_slide_out_right)
                                .setPopUpTo(
                                    R.id.mechanicHome,
                                    true
                                )
                                .build()
                        )
                    }

                    R.id.mechanicOrderHistoryBottomNavigation -> run {
                        if (navController.currentBackStackEntry!!.destination.id == R.id.mechanicOrderHistory) return@run
                        if (navController.currentBackStackEntry!!.destination.id == R.id.mechanicHome) {
                            navController.navigate(
                                R.id.mechanicOrderHistory,
                                null,
                                NavOptions
                                    .Builder()
                                    .setEnterAnim(R.anim.custom_slide_in_right)
                                    .setExitAnim(R.anim.custom_slide_out_left)
                                    .setPopEnterAnim(R.anim.custom_slide_in_left)
                                    .setPopExitAnim(R.anim.custom_slide_out_right)
                                    .build()
                            )
                        }
                        if (navController.currentBackStackEntry!!.destination.id == R.id.mechanicProfile) {
                            navController.navigate(
                                R.id.mechanicOrderHistory,
                                null,
                                NavOptions
                                    .Builder()
                                    .setEnterAnim(R.anim.custom_slide_in_left)
                                    .setExitAnim(R.anim.custom_slide_out_right)
                                    .setPopEnterAnim(R.anim.custom_slide_in_right)
                                    .setPopExitAnim(R.anim.custom_slide_out_left)
                                    .build()
                            )
                        }
                    }

                    R.id.mechanicProfileBottomNavigation -> run {
                        if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicProfile) return@run
                        if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicHome) {
                            navController.navigate(
                                R.id.mechanicProfile,
                                null,
                                NavOptions
                                    .Builder()
                                    .setEnterAnim(R.anim.custom_slide_in_right)
                                    .setExitAnim(R.anim.custom_slide_out_left)
                                    .setPopEnterAnim(R.anim.custom_slide_in_left)
                                    .setPopExitAnim(R.anim.custom_slide_out_right)
                                    .build()
                            )
                        }
                        if (navController.currentBackStackEntry?.destination?.id == R.id.mechanicOrderHistory) {
                            navController.navigate(
                                R.id.mechanicProfile,
                                null,
                                NavOptions
                                    .Builder()
                                    .setEnterAnim(R.anim.custom_slide_in_right)
                                    .setExitAnim(R.anim.custom_slide_out_left)
                                    .setPopEnterAnim(R.anim.custom_slide_in_left)
                                    .setPopExitAnim(R.anim.custom_slide_out_right)
                                    .build()
                            )
                        }
                    }

                    R.id.signInBottomNavigation -> run {
                        if (navController.currentBackStackEntry?.destination?.id == R.id.signIn) return@run
                        navController.navigate(R.id.action_signUp_to_signIn)
                    }

                    R.id.signUpBottomNavigation -> run {
                        if (navController.currentBackStackEntry?.destination?.id == R.id.signUp) return@run
                        navController.navigate(R.id.action_signIn_to_signUp)
                    }
                }
                return true
            }
        })

    }

    override fun onBackPressed() {
        window.navigationBarColor = Color.WHITE
        supportFragmentManager.fragments.forEach {
            log("back stack details: ${it.javaClass}")
        }
        log("back stack: ${navHostFragment.childFragmentManager.backStackEntryCount}")
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
            return
        }
        if (navController.currentBackStackEntry!!.destination.id == R.id.signIn) {
            navAlertDialogCloseApplication { }
            return
        }
        if (navHostFragment.childFragmentManager.backStackEntryCount != 0) {
            navController.popBackStack()
        } else {
            navAlertDialogCloseApplication { }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_drawer_contributors -> {
                navAlertDialog(
                    R.string.contributors_heading,
                    R.string.contributors,
                    NavAlertDialogType.SIMPLE_ALERT_DIALOG
                )
                return true
            }

            R.id.nav_drawer_about -> {
                navAlertDialog(
                    R.string.about_us_heading,
                    R.string.about_us,
                    NavAlertDialogType.SCROLL_ALERT_DIALOG
                )
                return true
            }

            R.id.nav_drawer_theme -> {
                navAlertDialog(
                    R.string.theme_app_heading,
                    R.string.theme_app,
                    NavAlertDialogType.SIMPLE_ALERT_DIALOG
                )
                return true
            }

            R.id.nav_drawer_rate_us -> {
                navAlertDialog(
                    R.string.rate_us_heading,
                    R.string.rate_us,
                    NavAlertDialogType.SIMPLE_ALERT_DIALOG
                )
                return true
            }

            R.id.nav_drawer_share -> {
                navAlertDialog(
                    R.string.share_app_heading,
                    R.string.share_app,
                    NavAlertDialogType.SIMPLE_ALERT_DIALOG
                )
                return true
            }

            R.id.nav_drawer_privacy_policy -> {
                navAlertDialog(
                    R.string.privacy_policy_heading,
                    R.string.privacy_policy,
                    NavAlertDialogType.SCROLL_ALERT_DIALOG
                )
                return true
            }

            R.id.nav_drawer_logout -> {
                binding.drawerLayout.closeDrawers()
                sharedViewModel.setChangeFragment(ChangeFragment.AUTHENTICATION)
                sharedViewModel
                    .setUpdateDrawerState(
                        UpdateDrawerInfo(
                            title = getString(R.string.app_name),
                            description = "",
                        )
                    )
                return true
            }

            R.id.nav_drawer_exit -> {
                navAlertDialogCloseApplication { }
                return true
            }
        }
        return true
    }

}