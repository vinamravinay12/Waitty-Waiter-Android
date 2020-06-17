package com.waitty.waiter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.waitty.waiter.adapter.viewholders.GenericOrderViewHolder
import com.waitty.waiter.adapter.viewholders.NewOrderViewHolder
import com.waitty.waiter.databinding.LayoutItemOrderNewBinding

import com.waitty.waiter.model.OrderDetails

class NewOrderAdapter(private val layoutRes: Int,dataList : MutableList<OrderDetails>?) : GenericOrderAdapter<OrderDetails>(dataList = dataList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericOrderViewHolder<OrderDetails> {
        val inflater = LayoutInflater.from(parent.context)
        val bindingNewOrder = DataBindingUtil.inflate<LayoutItemOrderNewBinding>(inflater, layoutRes,parent,false)
        return NewOrderViewHolder(bindingNewOrder,variablesMap = getVariablesMap())
    }
}