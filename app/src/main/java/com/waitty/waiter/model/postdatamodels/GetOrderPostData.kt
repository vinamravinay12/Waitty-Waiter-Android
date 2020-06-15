package com.waitty.waiter.model.postdatamodels

import com.google.gson.annotations.SerializedName
import com.waitty.waiter.retrofit.API

data class GetOrderPostData(
        @SerializedName(API.LIMIT)
        val limit : Int,
        @SerializedName(API.PAGE)
        val page : Int
)