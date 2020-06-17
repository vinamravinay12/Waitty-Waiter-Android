package com.waitty.waiter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.waitty.waiter.adapter.viewholders.GenericOrderViewHolder
import com.waitty.waiter.adapter.viewholders.ServedOrderViewHolder
import com.waitty.waiter.databinding.LayoutItemOrderPreparedBinding
import com.waitty.waiter.model.OrderDetails

class ServedOrdersAdapter(private val layoutRes: Int,dataList : MutableList<OrderDetails>?) : GenericOrderAdapter<OrderDetails>(dataList = dataList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericOrderViewHolder<OrderDetails> {
        val inflater = LayoutInflater.from(parent.context)
        val bindingServedOrder = DataBindingUtil.inflate<LayoutItemOrderPreparedBinding>(inflater, layoutRes,parent,false)
        return ServedOrderViewHolder(bindingServedOrder,variablesMap = getVariablesMap())
    }
}