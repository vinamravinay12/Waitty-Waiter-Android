package com.waitty.waiter.model.apimodels

import com.google.gson.annotations.SerializedName
import com.waitty.waiter.model.OrderDetails

data class OrderResponse(
        @SerializedName("success")
        val success : Boolean,
        @SerializedName("data")
        val data : List<OrderDetails>,
        @SerializedName("message")
        val message : String?
)