package com.waitty.waiter.viewmodel.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Api
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.waitty.waiter.constant.constant
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.model.WaittyAPIResponse
import com.waitty.waiter.retrofit.ApiInterface

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsRepository(private val apiInterface: ApiInterface) {

    fun changeNotificationSettings(token : String, postData : JsonObject) : MutableLiveData<WaittyAPIResponse> {

        val responseData = MutableLiveData<WaittyAPIResponse>()

        var waittyAPIResponse = WaittyAPIResponse(data = null,status = APIStatus.LOADING,errorCode = -1,message = null)

        apiInterface.profileUpdate(postData,token).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
               waittyAPIResponse = WaittyAPIResponse(data = null,status = APIStatus.ERROR,errorCode =  -1, message = constant.SWW)
                responseData.value = waittyAPIResponse
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
              waittyAPIResponse = if(!response.isSuccessful) {
                  WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")
              } else {
                  WaittyAPIResponse(response.body().toString(), APIStatus.SUCCESS, null, null)
              }

                responseData.value = waittyAPIResponse
            }

        })
        return responseData;
    }
}