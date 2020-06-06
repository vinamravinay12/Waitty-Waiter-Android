package com.waitty.waiter.retrofit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {

    @POST(API.LOGIN)
    Call<JsonElement> login(@Body JsonObject OBJ);

    @POST(API.APPVERSION)
    Call<JsonElement> checkVersion(@Body JsonObject OBJ);

    @POST(API.GET_CATEGORY)
    Call<JsonElement> getCategory(@Body JsonObject OBJ);

    @POST(API.GET_DISH)
    Call<JsonElement> getDish(@Body JsonObject OBJ);

    @PUT(API.UPDATE_PROFILE)
    Call<JsonElement> profileUpdate(@Body JsonObject OBJ, @Header(API.AUTHORIZATION) String token);

    @GET(API.LOGOUT)
    Call<JsonElement> logoutApplication(@Header(API.AUTHORIZATION) String token);

    @POST(API.GET_NEW_ORDER)
    Call<JsonElement> getNewOrder(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

    @POST(API.GET_PROCESSING_ORDER)
    Call<JsonElement> getProcessingOrder(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

    @POST(API.GET_SERVED_ORDER)
    Call<JsonElement> getServedOrder(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

    @POST(API.ACCEPT_ORDER)
    Call<JsonElement> acceptOrder(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

    @POST(API.REJECT_ORDER)
    Call<JsonElement> rejectOrder(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

    @POST(API.DELIVERD_ORDER)
    Call<JsonElement> deliverdOrder(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

    @POST(API.SEARCH_ORDER)
    Call<JsonElement> searchOrder(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

    @POST(API.ORDER_STATUS)
    Call<JsonElement> checkOrderStatus(@Body JsonObject OBJ,@Header(API.AUTHORIZATION) String token);

}

