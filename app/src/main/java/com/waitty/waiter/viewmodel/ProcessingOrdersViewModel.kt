package com.waitty.waiter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waitty.waiter.R
import com.waitty.waiter.adapter.GenericOrderAdapter
import com.waitty.waiter.adapter.ProcessingOrdersAdapter
import com.waitty.waiter.constant.constant
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.WaittyAPIResponse
import com.waitty.waiter.model.postdatamodels.GetOrderPostData
import com.waitty.waiter.retrofit.ApiClient
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.utility.WKItemClickListener
import com.waitty.waiter.viewmodel.repository.NewOrderRepository

enum class OrderStatusColor(val colorOrderStatus: Int) {
    ORDER_PREPARING(R.color.colorOrderStatusPreparing),
    ORDER_READY_SERVE(R.color.colorOrderStatusReady),
    ORDER_DELIVERED(R.color.colorBtnStartPreparingGreen)
}

class ProcessingOrdersViewModel : ListOrderViewModel() {

    private val processingOrdersList: MutableLiveData<List<OrderDetails>> = MutableLiveData()
    private val orderId = MutableLiveData<String>()
    private val tableId: MutableLiveData<String> = MutableLiveData()
    private val orderType: MutableLiveData<String> = MutableLiveData()
    private val userName: MutableLiveData<String> = MutableLiveData()
    private val tableIdPrefix: MutableLiveData<String> = MutableLiveData("T")
    private var orderAdapter: GenericOrderAdapter<OrderDetails>? = null
    private val totalOrderItems = MutableLiveData<String>()
    private lateinit var clickItemListener: WKItemClickListener
    private var repository: NewOrderRepository? = null
    private var selectedOrder = MutableLiveData<OrderDetails>()
    private var orderStatusColor = MutableLiveData<Int>(R.color.colorTransparent)
    private var orderStatus = MutableLiveData<String>();

    init {
        orderAdapter = ProcessingOrdersAdapter(getViewType(), processingOrdersList.value?.toMutableList())

    }


    fun getOrderListData() = processingOrdersList
    fun getOrderAdapter() = orderAdapter

    fun setSelectedOrderDetails(position: Int) {
        selectedOrder.value = processingOrdersList.value?.get(position)
    }

    fun getSelectedOrderDetails() = selectedOrder

    fun setOrdersList(orders: List<OrderDetails>) {
        val recentOrders = orders.filter { orderDetails -> Utility.isCreatedToday(orderDetails.createdAt) }
        processingOrdersList.value = recentOrders.filter { orderDetails -> Utility.hasOrderItems(orderDetails) }
    }

    fun getOrderId(position: Int): MutableLiveData<String> {
        if (position >= processingOrdersList.value?.size ?: 0) return orderId
        orderId.value = "Order No #" + processingOrdersList.value?.get(position)?.id ?: ""
        return orderId
    }


    fun getUserName(position: Int): LiveData<String> {

        if (position >= processingOrdersList.value?.size ?: 0) return userName
        userName.value = processingOrdersList.value?.get(position)?.user?.name ?: ""
        return userName

    }

    fun setWaiterDetails(id: String) {
        userName.value = id
    }

    fun getTableId(position: Int): MutableLiveData<String> {
        if (processingOrdersList.value.isNullOrEmpty() || processingOrdersList.value?.get(position)?.table == null) return tableId
        else if (position >= processingOrdersList.value?.size ?: 0) return tableId
        tableId.value = StringBuilder().append("T").append(" ").append(processingOrdersList.value?.get(position)?.table?.name).toString()
        return tableId
    }

    fun getTotalOrderItems(position: Int): MutableLiveData<String> {
        if (position >= processingOrdersList.value?.size ?: 0) return totalOrderItems
        totalOrderItems.value = ("x" + processingOrdersList.value?.get(position)?.orderItems?.size
                ?: 0) as String?
        return totalOrderItems
    }


    fun getOrderType(position: Int): LiveData<String> {
        if (position >= processingOrdersList.value?.size ?: 0) return orderType
        if (processingOrdersList.value.isNullOrEmpty()) return orderType

        orderType.value = "" + processingOrdersList.value?.get(position)?.orderType
        return orderType
    }

    fun getOrderStatusColor(position: Int): MutableLiveData<Int> {
        if (position >= processingOrdersList.value?.size ?: 0) return orderStatusColor
        if (processingOrdersList.value.isNullOrEmpty()) return orderStatusColor

        orderStatusColor.value = when (processingOrdersList.value?.get(position)?.orderStatus?.id) {
            constant.ORDER_PREPARING -> OrderStatusColor.ORDER_PREPARING.colorOrderStatus
            constant.ORDER_READY_SERVE -> OrderStatusColor.ORDER_READY_SERVE.colorOrderStatus
            constant.ORDER_DELIVERED -> OrderStatusColor.ORDER_DELIVERED.colorOrderStatus
            else -> R.color.colorTabItemText
        }

        return orderStatusColor
    }


    fun getOrderStatus(position: Int): MutableLiveData<String> {
        if (position >= processingOrdersList.value?.size ?: 0) return orderStatus
        if (processingOrdersList.value.isNullOrEmpty()) return orderStatus

        orderStatus.value = processingOrdersList.value?.get(position)?.orderStatus?.name
        return orderStatus
    }


    override fun getViewType(): Int {
        return R.layout.layout_order_processing
    }

    override fun updateList() {
        orderAdapter?.updateList(processingOrdersList.value?.toMutableList())
    }

    fun fetchProcessingOrders(token: String): MutableLiveData<WaittyAPIResponse>? {

        val orderPostData = GetOrderPostData(200, 1)
        repository = NewOrderRepository(ApiClient.getAPIInterface(), token)
        return repository?.getProcessingOrders(token, orderPostData, MutableLiveData())
    }


}