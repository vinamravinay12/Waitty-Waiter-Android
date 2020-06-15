package com.waitty.waiter.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.databinding.DataBindingUtil
import com.waitty.waiter.R
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.waiter.constant.constant
import com.waitty.waiter.databinding.ActivityLoginBinding
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.model.apimodels.LoginResponse
import com.waitty.waiter.retrofit.API
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.utility.WKClickListener
import com.waitty.waiter.viewmodel.ApiErrorViewModel
import com.waitty.waiter.viewmodel.ClickType
import com.waitty.waiter.viewmodel.LoginViewModel
import androidx.databinding.library.baseAdapters.BR

class LoginActivity : AppCompatActivity(), WKClickListener {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var apiErrorViewModel: ApiErrorViewModel


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initializeViewModels()
        initializeListeners()
    }

    private fun initializeListeners() {

        //        bindingLoginFragment.etKitchenId.doAfterTextChanged {
//
//            val isValidated = loginViewModel.validateUserName()
//            setErrorProperties(bindingLoginFragment.txtInputKitchenIdLayout,bindingLoginFragment.etKitchenId,!isValidated)
//        }
//
//        bindingLoginFragment.etPassword.doAfterTextChanged {
//            val isValidated = loginViewModel.validatePassword()
//            bindingLoginFragment.txtInputPasswordLayout?.let { it1 -> setErrorProperties(it1,bindingLoginFragment.etPassword,!isValidated) }
//        }

      //  activityLoginBinding.etWaiterId.setOnFocusChangeListener()
        activityLoginBinding.btnLogin.setOnClickListener { onClick(ClickType.Login) }
        activityLoginBinding.tvForgotPassword.setOnClickListener { onClick(ClickType.ForgotPassword) }
    }


    // Variable initialization
    private fun initializeViewModels() {
        activityLoginBinding.let { FragmentUtils.setBindingVariables(hashMapOf(BR.LoginVM to loginViewModel, BR.clickEvent to this), it) }
        activityLoginBinding.layoutLoader.apiErrorVM = apiErrorViewModel
        activityLoginBinding.layoutError.apiErrorVM = apiErrorViewModel

        activityLoginBinding.setLifecycleOwner(this)

    }

    private fun setErrorProperties(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, isError: Boolean) {
        setHintColor(textInputLayout,isError)
        textInputLayout.endIconDrawable = getEndIcon(isError)
        textInputLayout.isEndIconVisible = true

    }

    private fun setHintColor(view: TextInputLayout, isError: Boolean) {

        if (isError) view.markRequiredInRed()
        view.hintTextColor = getHintTextColor(isError)

        view.boxStrokeColor = getBoxStrokeColor(isError) ?: R.color.colorWaiterIdText
    }

    private fun getEndIcon(isError: Boolean) : Drawable? {
        return this.let { context ->
            if(isError) AppCompatResources.getDrawable(context,R.drawable.ic_error_red_24dp) else AppCompatResources.getDrawable(context,R.drawable.ic_done_tick_green_24dp)

        }
    }


    private fun getHintTextColor(isError: Boolean): ColorStateList? {
        return this.let { context ->
            if (isError) ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorCountdownTimer)) else ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTabItemTextSelected))
        }
    }

    private fun getBoxStrokeColor(isError: Boolean): Int? {
        return this.let { context ->
            if (isError) ContextCompat.getColor(context, R.color.colorCountdownTimer) else ContextCompat.getColor(context, R.color.colorWaiterIdText)
        }

    }

    private fun loginUser() {
        val isValidated = loginViewModel.validateUserName() ?: false
        setErrorProperties( activityLoginBinding.txtInputPasswordLayout, activityLoginBinding.etWaiterId,!isValidated)

        val isPasswordValidated = loginViewModel.validatePassword() ?: false
        setErrorProperties(activityLoginBinding.txtInputPasswordLayout,activityLoginBinding.etPassword,!isPasswordValidated)


        FragmentUtils.hideKeyboard(activityLoginBinding.root,this)
        val deviceId = this.let { context -> SharedPreferenceManager(context, constant.LOGIN_SP).getStringPreference(constant.USER_DEVICEID) }

        val fcmToken = this.let { context -> SharedPreferenceManager(context, constant.LOGIN_SP).getStringPreference(constant.USER_FCMTOKENID) }



        if (deviceId != null && fcmToken != null) {
            loginViewModel.login(deviceId, fcmToken)?.observe(this, Observer {
                it?.let { response ->
                    when (response.status) {
                        APIStatus.LOADING -> FragmentUtils.showProgress(parentView = activityLoginBinding.viewLogin, toShow = true, apiErrorViewModel = apiErrorViewModel, progressMessage = getString(R.string.wait), isSwipeRefreshed = false)

                        APIStatus.ERROR ->  showError(true,errorCode = response.errorCode ?: 405, errorMessage = Utility.getMessageOnErrorCode(response.errorCode, this))

                        APIStatus.SUCCESS -> {
                            if (response.data == null) showError(false,404,"")
                            else {
                                activityLoginBinding.viewLogin.visibility = View.VISIBLE
                                apiErrorViewModel?.resetValues()
                                handleLoginResponse(response.data)

                            }
                        }
                    }
                }
            })
        }



    }

    private fun handleLoginResponse(response: Any) {

        val sharedPreferenceManager = SharedPreferenceManager(context = this, name = constant.LOGIN_SP)
        (response as? LoginResponse).let { loginResponse ->
            sharedPreferenceManager.storeStringPreference(API.AUTHORIZATION, loginResponse?.token
                    ?: "")
        }
        sharedPreferenceManager.storeBooleanPreference(constant.KEY_IS_LOGGED_IN, true)
        launchHomeActivity()

    }

    private fun launchHomeActivity() {
        Utility.ShowToast(this,getString(R.string.login_success), Toast.LENGTH_LONG)
        //  startActivity(Intent(activity,HomeActivityNew::class.java))
        //activity?.finish()
    }

    override fun onClick(clickType: ClickType) {
        if (clickType == ClickType.Login) loginUser()
    }

    fun TextInputLayout.markRequiredInRed() {

        hint = if (hint?.contains("*") == true) hint?.substring(0, (hint?.length
                ?: 0) - 1) else hint?.substring(0, hint?.length ?: 0)
        hint = buildSpannedString {

            append(hint)
            color(Color.RED) { append("*") } // Mind the space prefix.
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hintTextColor = ColorStateList.valueOf(resources.getColor(R.color.colorCountdownTimer, null))
        }
    }

    fun showError(toShow: Boolean, errorCode: Int, errorMessage: String?) {

        if (toShow) apiErrorViewModel.let { FragmentUtils.showError( it, errorCode, errorMessage, true) } else apiErrorViewModel?.resetValues()
        activityLoginBinding.viewLogin.visibility = if (toShow) View.GONE else View.VISIBLE
        if (toShow) {
            Handler().postDelayed({ showError(false, errorCode, "") }, 3000)
        }


    }

}