package com.waitty.waiter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waitty.waiter.R
import com.waitty.waiter.adapter.GenericOrderAdapter
import com.waitty.waiter.adapter.NewOrderAdapter
import com.waitty.waiter.adapter.ServedOrdersAdapter
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.WaittyAPIResponse
import com.waitty.waiter.model.postdatamodels.GetOrderPostData
import com.waitty.waiter.retrofit.ApiClient
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.utility.WKItemClickListener
import com.waitty.waiter.viewmodel.repository.NewOrderRepository

class ServedOrdersListViewModel :  ListOrderViewModel() {

    private val servedOrdersList: MutableLiveData<List<OrderDetails>> = MutableLiveData()
    private val orderId = MutableLiveData<String>()
    private val tableId: MutableLiveData<String> = MutableLiveData()
    private val orderType: MutableLiveData<String> = MutableLiveData()
    private val waiterId: MutableLiveData<String> = MutableLiveData()
    private val tableIdPrefix: MutableLiveData<String> = MutableLiveData("T")
    private var orderAdapter: GenericOrderAdapter<OrderDetails>? = null
    private val totalOrderItems = MutableLiveData<String>()
    private lateinit var clickItemListener : WKItemClickListener
    private var repository : NewOrderRepository? = null
    private var selectedOrder = MutableLiveData<OrderDetails>()

    init {
        orderAdapter = ServedOrdersAdapter(getViewType(),servedOrdersList.value?.toMutableList())

    }


    fun getOrderListData() = servedOrdersList
    fun getOrderAdapter() = orderAdapter

    fun setSelectedOrderDetails(position: Int){
        selectedOrder.value =  servedOrdersList.value?.get(position)
    }

    fun setOrdersList(orders : List<OrderDetails>) {
        val recentOrders = orders.filter { orderDetails -> Utility.isCreatedToday(orderDetails.createdAt) }
        servedOrdersList.value = recentOrders.filter { orderDetails -> Utility.hasOrderItems(orderDetails) }
    }

    fun getSelectedOrderDetails() = selectedOrder


    fun getOrderId(position : Int) : MutableLiveData<String> {
        if(position >= servedOrdersList.value?.size ?: 0) return orderId
        orderId.value =  "Order No #" + servedOrdersList.value?.get(position)?.id ?: ""
        return orderId
    }


    fun getWaiterDetails(position: Int): LiveData<String> {
        return waiterId
    }

    fun setWaiterDetails(id : String) {
        waiterId.value = id
    }

    fun getTableId(position: Int): MutableLiveData<String> {
        if (servedOrdersList.value.isNullOrEmpty() || servedOrdersList.value?.get(position)?.table == null) return tableId

        else if(position >= servedOrdersList.value?.size ?: 0) return tableId
        tableId.value = StringBuilder().append("T").append(" ").append(servedOrdersList.value?.get(position)?.table?.name).toString()
        return tableId
    }

    fun getTotalOrderItems(position: Int) : MutableLiveData<String> {
        if(position >= servedOrdersList.value?.size ?: 0) return totalOrderItems
        totalOrderItems.value = ("x" + servedOrdersList.value?.get(position)?.orderItems?.size ?: 0) as String?
        return totalOrderItems
    }


    fun getOrderType(position: Int): LiveData<String> {
        if(position >= servedOrdersList.value?.size ?: 0) return orderType
        if (servedOrdersList.value.isNullOrEmpty()) return orderType

        orderType.value = "" + servedOrdersList.value?.get(position)?.orderType
        return orderType
    }


    override fun getViewType(): Int {
        return R.layout.layout_item_order_prepared
    }

    override fun updateList() {
        orderAdapter?.updateList(servedOrdersList.value?.toMutableList())
    }

    fun fetchServedOrders(token : String) : MutableLiveData<WaittyAPIResponse>? {

        val orderPostData = GetOrderPostData(20,1)
        repository = NewOrderRepository(ApiClient.getAPIInterface(),token)
        return repository?.getServedOrders(token,orderPostData, MutableLiveData())
    }




}