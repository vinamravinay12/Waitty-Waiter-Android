package com.waitty.waiter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.waitty.waiter.adapter.viewholders.GenericOrderViewHolder
import com.waitty.waiter.adapter.viewholders.OrderItemsViewHolder
import com.waitty.waiter.databinding.LayoutOrderItemsBinding
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.OrderItem

class NewOrderDetailsAdapter(private val layoutRes: Int, private val dataList: MutableList<OrderItem>?) : GenericOrderAdapter<OrderItem>(dataList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericOrderViewHolder<OrderItem> {
        val inflater = LayoutInflater.from(parent.context)
        val bindingPreparedItemsOrder = DataBindingUtil.inflate<LayoutOrderItemsBinding>(inflater, layoutRes,parent,false)
        return OrderItemsViewHolder(bindingPreparedItemsOrder,getVariablesMap())
    }
}