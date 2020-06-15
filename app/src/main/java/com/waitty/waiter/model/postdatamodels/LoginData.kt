package com.waitty.waiter.model.postdatamodels

import com.google.gson.annotations.SerializedName
import com.waitty.waiter.constant.constant
import com.waitty.waiter.retrofit.API


data class LoginData(@SerializedName(API.KEY) val waiterId : String, @SerializedName(API.PASSWORD) val password : String,
                     @SerializedName(API.DEVICE_TYPE) val deviceType : String = constant.DEVICE_TYPE,
                     @SerializedName(constant.USER_DEVICEID) val deviceID : String, @SerializedName(constant.USER_FCMTOKENID) val fcmTokenID : String)