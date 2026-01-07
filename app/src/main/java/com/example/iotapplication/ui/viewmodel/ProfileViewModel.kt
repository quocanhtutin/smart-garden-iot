package com.example.iotapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iotapplication.data.model.auth.UserProfile
import com.example.iotapplication.data.model.request.ChangePasswordRequest
import com.example.iotapplication.data.model.request.UpdateProfileRequest
import com.example.iotapplication.data.remote.api.ApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(
    private val api: ApiService
) : ViewModel() {

    var profile by mutableStateOf<UserProfile?>(null)
        private set

    var error by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    fun loadProfile() {
        viewModelScope.launch {
            profile = api.getProfile().data
        }
    }

    fun updateProfile(
        username: String,
        email: String,
        onFail: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                profile = api.updateProfile(
                    UpdateProfileRequest(username, email)
                ).data
                error = null
            } catch (e: HttpException) {
                error = e.message()
                onFail()
            }
        }
    }

    fun changePassword(
        old: String,
        new: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                api.changePassword(ChangePasswordRequest(old, new))
                successMessage = "Đổi mật khẩu thành công"
                passwordError = null
                onSuccess()
            } catch (e: HttpException) {
                passwordError = "Mật khẩu hiện tại không đúng"
            }
        }
    }
}
