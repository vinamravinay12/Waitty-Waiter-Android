package com.waitty.waiter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.waitty.waiter.R
import com.waitty.waiter.adapter.GenericOrderAdapter
import com.waitty.waiter.adapter.NewOrderAdapter
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.WaittyAPIResponse
import com.waitty.waiter.model.postdatamodels.GetOrderPostData
import com.waitty.waiter.retrofit.ApiClient
import com.waitty.waiter.utility.WKItemClickListener
import com.waitty.waiter.viewmodel.repository.NewOrderRepository


abstract class ListOrderViewModel : ViewModel() {
    abstract fun getViewType(): Int
    abstract fun updateList()
}


class NewOrdersViewModel : ListOrderViewModel() {

    private val newOrderList: MutableLiveData<List<OrderDetails>> = MutableLiveData()
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
        orderAdapter = NewOrderAdapter(getViewType(),newOrderList.value?.toMutableList())

    }


    fun getOrderListData() = newOrderList
    fun getOrderAdapter() = orderAdapter

    fun setSelectedOrderDetails(position: Int){
        selectedOrder.value =  newOrderList.value?.get(position)
    }


    fun getOrderId(position : Int) : MutableLiveData<String>{
        if(position >= newOrderList.value?.size ?: 0) return orderId
        orderId.value =  "Order No #" + newOrderList.value?.get(position)?.id ?: ""
        return orderId
    }


    fun getWaiterDetails(position: Int): LiveData<String> {
        if(position >= newOrderList.value?.size ?: 0) return waiterId

        else if (newOrderList.value.isNullOrEmpty()) {
            return waiterId
        }
        waiterId.value = newOrderList.value?.get(position)?.waiter?.name
        return waiterId
    }

    fun getTableId(position: Int): MutableLiveData<String> {
        if (newOrderList.value.isNullOrEmpty() || newOrderList.value?.get(position)?.table == null) return tableId

        else if(position >= newOrderList.value?.size ?: 0) return tableId
        tableId.value = StringBuilder().append("T").append(" ").append(newOrderList.value?.get(position)?.table?.name).toString()
        return tableId
    }

    fun getTotalOrderItems(position: Int) : MutableLiveData<String> {
        if(position >= newOrderList.value?.size ?: 0) return totalOrderItems
        totalOrderItems.value = ("x" + newOrderList.value?.get(position)?.orderItems?.size ?: 0) as String?
        return totalOrderItems
    }


    fun getOrderType(position: Int): LiveData<String> {
        if(position >= newOrderList.value?.size ?: 0) return orderType
        if (newOrderList.value.isNullOrEmpty()) return orderType

        orderType.value = "" + newOrderList.value?.get(position)?.orderType
        return orderType
    }


    override fun getViewType(): Int {
        return R.layout.layout_item_order_new
    }

    override fun updateList() {
        orderAdapter?.updateList(newOrderList.value?.toMutableList())
    }

    fun fetchNewOrders(token : String) : MutableLiveData<WaittyAPIResponse>? {

        val orderPostData = GetOrderPostData(20,1)
        repository = NewOrderRepository(ApiClient.getAPIInterface(),token)
        return repository?.getNewOrders(orderPostData, MutableLiveData())
    }




}
