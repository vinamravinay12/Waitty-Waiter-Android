package com.waitty.waiter.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.waitty.waiter.R
import com.waitty.waiter.model.WaittyAPIResponse
import com.waitty.waiter.model.apimodels.LoginResponse
import com.waitty.waiter.model.postdatamodels.LoginData
import com.waitty.waiter.retrofit.ApiClient
import com.waitty.waiter.viewmodel.repository.LoginRepository


enum class ClickType {
    Login,ForgotPassword
}

class LoginViewModel : ViewModel() {

    private val waiterIdMutableLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val userIdError = MutableLiveData<Int>()
    private val passwordError = MutableLiveData<Int>()



    fun getWaiterId() : MutableLiveData<String> { return waiterIdMutableLiveData}

    fun getPassword() : MutableLiveData<String> { return passwordLiveData }

    fun getUserIdError() : MutableLiveData<Int> {
        return userIdError
    }

    fun getPasswordError() : MutableLiveData<Int> {
        return passwordError
    }


    fun validateUserName() : Boolean {
        if (TextUtils.isEmpty(waiterIdMutableLiveData.value)) {
            userIdError.value = (R.string.username_empty_error)
            return false
        }
        userIdError.value = null
        return true
    }

    fun validatePassword() : Boolean {
        if (TextUtils.isEmpty(passwordLiveData.value)) {
            passwordError.value =  R.string.password_empty_error
            return false
        }

        else if(passwordLiveData.value?.length ?: 0 < 5) {
            passwordError.value = R.string.password_length_error
            return false
        }

        passwordError.value = null
        return true

    }


     fun login(userDeviceID : String, userFCMTokenID : String) : MutableLiveData<WaittyAPIResponse>? {

       if(!validateUserName() || !validatePassword()) {
            return null
       }

         val loginData = LoginData(
                 waiterId = waiterIdMutableLiveData.value?.trim() ?: "",
                 password = passwordLiveData.value?.trim() ?: "",
                 deviceID = userDeviceID,
                 fcmTokenID = userFCMTokenID
         )

         val loginRepository = LoginRepository<LoginResponse>(ApiClient.getAPIInterface())

         return loginRepository.loginUser(loginData)

    }


}