package com.example.smartbikefyp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.model.UpdateDrawerInfo
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.model.VideoDetail
import com.example.smartbikefyp.repository.Authentication
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.util.UserRoleType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val authentication: Authentication,
) : ViewModel() {

    private val _observeChangeFragment = MutableSharedFlow<ChangeFragment>()
    val observeChangeFragment: SharedFlow<ChangeFragment>
        get() = _observeChangeFragment

    private val _selectedItemUserState = MutableStateFlow<Any?>(null)
    val selectedItemUserState: StateFlow<Any?>
        get() = _selectedItemUserState

    private val _changeBottomMenuState = MutableSharedFlow<Int?>()
    val changeBottomMenuState: SharedFlow<Int?>
        get() = _changeBottomMenuState

    private val _updateDrawerInfoState = MutableStateFlow<UpdateDrawerInfo?>(null)
    val updateDrawerInfoState: StateFlow<UpdateDrawerInfo?>
        get() = _updateDrawerInfoState

    private val _observeVideoDetail = MutableStateFlow<VideoDetail?>(null)
    val observeVideoDetail: StateFlow<VideoDetail?>
        get() = _observeVideoDetail

    val currentUser: StateFlow<UserRoleType<User>> =
        authentication
            .currentUser
            .onStart { emit(UserRoleType.Loading) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                UserRoleType.Loading
            )

    fun <T> setSelectedItemUserState(selectedItem: T) {
        _selectedItemUserState.value = selectedItem
    }

    fun logout() = authentication.signOutUser()

    fun setUpdateDrawerState(updateDrawerInfo: UpdateDrawerInfo) {
        _updateDrawerInfoState.value = updateDrawerInfo
    }

    fun setChangeBottomMenuState(bottomMenu: Int) = viewModelScope.launch {
        _changeBottomMenuState.emit(bottomMenu)
    }

    fun setChangeFragment(fragmentName: ChangeFragment) = viewModelScope.launch {
        _observeChangeFragment.emit(fragmentName)
    }

    fun setVideoDetail(videoDetail: VideoDetail) {
        _observeVideoDetail.value = videoDetail
    }

}