package com.waitty.waiter.viewmodel.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager
import com.waitty.waiter.constant.constant
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.WaittyAPIResponse
import com.waitty.waiter.model.apimodels.OrderResponse
import com.waitty.waiter.model.postdatamodels.GetOrderPostData
import com.waitty.waiter.retrofit.ApiInterface
import com.waitty.waiter.viewmodel.OrderPreparationStatus
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface OrderRepository {

    fun getOrders(orderPostData: GetOrderPostData): MutableLiveData<WaittyAPIResponse>
    fun updateOrder(postData: JsonObject): MutableLiveData<WaittyAPIResponse>
}


class NewOrderRepository(private val apiInterface: ApiInterface, private val token: String) {


     fun rejectOrder(postData: JsonObject, responseData: MutableLiveData<WaittyAPIResponse>): MutableLiveData<WaittyAPIResponse> {

        var waittyAPIResponse = WaittyAPIResponse(null, APIStatus.LOADING, null, null)

        apiInterface.rejectOrder(postData, token).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                waittyAPIResponse = WaittyAPIResponse(null, APIStatus.ERROR, 404, t.localizedMessage)
                responseData.value = waittyAPIResponse
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (!response.isSuccessful) {
                    waittyAPIResponse = WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")
                    responseData.value = waittyAPIResponse
                    return
                }

                waittyAPIResponse = WaittyAPIResponse(response.body().toString(), APIStatus.SUCCESS, null, null)
                responseData.value = waittyAPIResponse
            }


        })
        responseData.value = waittyAPIResponse
        return responseData
    }

     fun acceptOrder(postData: JsonObject, responseData: MutableLiveData<WaittyAPIResponse>): MutableLiveData<WaittyAPIResponse> {
        var waittyAPIResponse = WaittyAPIResponse(null, APIStatus.LOADING, null, null)

        apiInterface.acceptOrder(postData, token).enqueue(object : Callback<JsonElement> {

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                waittyAPIResponse = WaittyAPIResponse(null, APIStatus.ERROR, 404, t.localizedMessage)
                responseData.value = waittyAPIResponse
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (!response.isSuccessful) {
                    waittyAPIResponse = WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")
                    responseData.value = waittyAPIResponse
                    return
                }

                waittyAPIResponse = WaittyAPIResponse(response.body().toString(), APIStatus.SUCCESS, null, null)
                responseData.value = waittyAPIResponse
            }


        })
        responseData.value = waittyAPIResponse
        return responseData
    }

   fun deliverOrder(postData: JsonObject, responseData: MutableLiveData<WaittyAPIResponse>) :  MutableLiveData<WaittyAPIResponse> {
       var waittyAPIResponse = WaittyAPIResponse(null, APIStatus.LOADING, null, null)

       apiInterface.deliverOrder(postData, token).enqueue(object : Callback<JsonElement> {

           override fun onFailure(call: Call<JsonElement>, t: Throwable) {
               waittyAPIResponse = WaittyAPIResponse(null, APIStatus.ERROR, 404, t.localizedMessage)
               responseData.value = waittyAPIResponse
           }

           override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
               if (!response.isSuccessful) {
                   waittyAPIResponse = WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")
                   responseData.value = waittyAPIResponse
                   return
               }

               waittyAPIResponse = WaittyAPIResponse(response.body().toString(), APIStatus.SUCCESS, null, null)
               responseData.value = waittyAPIResponse
           }


       })
       responseData.value = waittyAPIResponse
       return responseData
   }


    fun getServedOrders(token: String, orderPostData: GetOrderPostData, responseData: MutableLiveData<WaittyAPIResponse>): MutableLiveData<WaittyAPIResponse> {
        var apiResponse = WaittyAPIResponse(null, APIStatus.LOADING, null, null)

        apiInterface.getServedOrder(orderPostData, token).enqueue(object : Callback<OrderResponse> {

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, 404, t.localizedMessage)
                responseData.value = apiResponse
            }

            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (!response.isSuccessful) {
                    apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")
                    responseData.value = apiResponse
                    return
                }

                apiResponse = WaittyAPIResponse(response.body(), APIStatus.SUCCESS, null, null)
                responseData.value = apiResponse

            }
        })

        responseData.value = apiResponse
        return responseData


    }

     fun getProcessingOrders(token: String, orderPostData: GetOrderPostData, responseData: MutableLiveData<WaittyAPIResponse>): MutableLiveData<WaittyAPIResponse> {
        var apiResponse = WaittyAPIResponse(null, APIStatus.LOADING, null, null)

        apiInterface.getProcessingOrder(orderPostData, token).enqueue(object : Callback<OrderResponse> {

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, 404, t.localizedMessage)
                responseData.value = apiResponse
            }

            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (!response.isSuccessful) {
                    apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")
                    responseData.value = apiResponse
                    return
                }

                apiResponse = WaittyAPIResponse(response.body(), APIStatus.SUCCESS, null, null)
                responseData.value = apiResponse

            }
        })

        responseData.value = apiResponse
        return responseData

    }

     fun getNewOrders(orderPostData: GetOrderPostData, responseData: MutableLiveData<WaittyAPIResponse>): MutableLiveData<WaittyAPIResponse> {

        var apiResponse = WaittyAPIResponse(null, APIStatus.LOADING, null, null)

        apiInterface.getNewOrder(orderPostData, token).enqueue(object : Callback<OrderResponse> {

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, 404, t.localizedMessage)
                responseData.value = apiResponse
            }

            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (!response.isSuccessful) {

                    apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")

                    return
                }

                apiResponse = WaittyAPIResponse(response.body(), APIStatus.SUCCESS, null, null)
                responseData.value = apiResponse

            }
        })

        responseData.value = apiResponse
        return responseData
    }


    fun getOrderStatus(postData: JsonObject, responseData: MutableLiveData<WaittyAPIResponse>) : MutableLiveData<WaittyAPIResponse> {

        var apiResponse = WaittyAPIResponse(null, APIStatus.LOADING, null, null)

        apiInterface.checkOrderStatus(postData, token).enqueue(object : Callback<OrderResponse> {

            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, 404, t.localizedMessage)
                responseData.value = apiResponse
            }

            override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                if (!response.isSuccessful) {

                    apiResponse = WaittyAPIResponse(null, APIStatus.ERROR, response.code(), "")

                    return
                }

                apiResponse = WaittyAPIResponse(response.body(), APIStatus.SUCCESS, null, null)
                responseData.value = apiResponse

            }
        })

        responseData.value = apiResponse
        return responseData
    }





}