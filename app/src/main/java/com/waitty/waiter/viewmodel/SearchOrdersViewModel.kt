package com.waitty.waiter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.waitty.waiter.R
import com.waitty.waiter.adapter.GenericOrderAdapter
import com.waitty.waiter.adapter.SearchOrdersAdapter
import com.waitty.waiter.constant.constant
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.WaittyAPIResponse
import com.waitty.waiter.retrofit.API
import com.waitty.waiter.retrofit.ApiClient
import com.waitty.waiter.retrofit.ApiInterface
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.utility.WKFilterResultListener
import com.waitty.waiter.utility.WKItemClickListener
import com.waitty.waiter.viewmodel.repository.NewOrderRepository
import java.text.SimpleDateFormat

class SearchOrdersViewModel : ListOrderViewModel(), WKFilterResultListener<OrderDetails> {

    private val searchOrdersList: MutableLiveData<List<OrderDetails>> = MutableLiveData(ArrayList<OrderDetails>())
    private val orderId = MutableLiveData<String>()
    private val tableId: MutableLiveData<String> = MutableLiveData()
    private val orderType: MutableLiveData<String> = MutableLiveData()
    private val userName: MutableLiveData<String> = MutableLiveData()
    private val userPhone: MutableLiveData<String> = MutableLiveData()
    private val tableIdPrefix: MutableLiveData<String> = MutableLiveData("T")
    private val orderCreationTime = MutableLiveData<String>()

    private var orderAdapter: GenericOrderAdapter<OrderDetails>? = null
    private lateinit var clickItemListener : WKItemClickListener
    private var repository : NewOrderRepository? = null
    private var selectedOrder = MutableLiveData<OrderDetails>()
    private var orderStatusColor = MutableLiveData<Int>(R.color.colorTransparent)
    private var orderStatus = MutableLiveData<String>()


    init {
        orderAdapter = SearchOrdersAdapter(getViewType(),searchOrdersList.value?.toMutableList(),this)

    }


    fun getOrderListData() = searchOrdersList
    fun getOrderAdapter() = orderAdapter

    fun setSelectedOrderDetails(position: Int){
        selectedOrder.value =  searchOrdersList.value?.get(position)
    }

    fun getSelectedOrderDetails() = selectedOrder

    
    fun getOrderId(position : Int) : MutableLiveData<String>{
        if(position >= searchOrdersList.value?.size ?: 0) return orderId
        orderId.value =  "Order No #" + searchOrdersList.value?.get(position)?.id ?: ""
        return orderId
    }


    fun getUserName(position: Int): MutableLiveData<String> {
        if(position >= searchOrdersList.value?.size ?: 0) return userName
        userName.value = searchOrdersList.value?.get(position)?.user?.name ?: ""
        return userName
    }
    
    fun getUserPhoneNumber(position: Int) : MutableLiveData<String> {
        if(position >= searchOrdersList.value?.size ?: 0) return userName
        userPhone.value = searchOrdersList.value?.get(position)?.user?.mobile ?: ""
        return userPhone
    }

    fun getOrderCreationDateTime(position: Int) : MutableLiveData<String> {
        if(position >= searchOrdersList.value?.size ?: 0) return orderCreationTime
        val orderCreationTime = searchOrdersList.value?.get(position)?.createdAt
        val simpleDateFormat = SimpleDateFormat(constant.DISPLAY_DATE_TIME_FORMAT)
        val formattedCreatedAt = Utility.chageUTC_ToLocalDate(constant.dateFormaterServer,orderCreationTime, simpleDateFormat)
        this.orderCreationTime.value = formattedCreatedAt
        return this.orderCreationTime

    }



    fun getOrderType(position: Int): LiveData<String> {
        if(position >= searchOrdersList.value?.size ?: 0) return orderType
        if (searchOrdersList.value.isNullOrEmpty()) return orderType

        orderType.value = "" + searchOrdersList.value?.get(position)?.orderType
        return orderType
    }

    fun getOrderStatusColor(position : Int) : MutableLiveData<Int> {
        if(position >= searchOrdersList.value?.size ?: 0) return orderStatusColor
        if (searchOrdersList.value.isNullOrEmpty())  return orderStatusColor

        orderStatusColor.value = when(searchOrdersList.value?.get(position)?.orderStatus?.id) {
            constant.ORDER_PREPARING -> OrderStatusColor.ORDER_PREPARING.colorOrderStatus
            constant.ORDER_READY_SERVE -> OrderStatusColor.ORDER_READY_SERVE.colorOrderStatus
            constant.ORDER_DELIVERED -> OrderStatusColor.ORDER_DELIVERED.colorOrderStatus
            else ->  R.color.colorWaiterIdText
        }

        return orderStatusColor
    }


    fun getOrderStatus(position: Int) : MutableLiveData<String> {
        if(position >= searchOrdersList.value?.size ?: 0) return orderStatus
        if (searchOrdersList.value.isNullOrEmpty()) return orderStatus

        orderStatus.value =  searchOrdersList.value?.get(position)?.orderStatus?.name
        return orderStatus
    }


    fun searchOrders(token : String, searchText : String) : MutableLiveData<WaittyAPIResponse> {
        val newOrderRepository = NewOrderRepository(ApiClient.getAPIInterface(),token)
        var jsonObject =  JsonObject();
        jsonObject.addProperty(API.ORDER_ID_DISPLAY, searchText.trim())
        return newOrderRepository.searchOrders(postData = jsonObject, responseData = MutableLiveData())
    }


    override fun getViewType(): Int {
        return R.layout.card_search_orders
    }

    override fun updateList() {
        orderAdapter?.updateList(searchOrdersList.value?.toMutableList())
    }

    override fun onResultsFiltered(filteredList: List<OrderDetails>) {
        searchOrdersList.value = filteredList
        (orderAdapter as? SearchOrdersAdapter)?.updateFilterList(filteredList.toMutableList())
    }

}