package com.waitty.waiter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.waitty.waiter.adapter.viewholders.GenericOrderViewHolder
import com.waitty.waiter.adapter.viewholders.ProcessingOrderViewHolder
import com.waitty.waiter.databinding.LayoutOrderProcessingBinding
import com.waitty.waiter.model.OrderDetails

class ProcessingOrdersAdapter(private val layoutRes: Int,dataList : MutableList<OrderDetails>?) : GenericOrderAdapter<OrderDetails>(dataList = dataList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericOrderViewHolder<OrderDetails> {
        val inflater = LayoutInflater.from(parent.context)
        val bindingProcessingOrder = DataBindingUtil.inflate<LayoutOrderProcessingBinding>(inflater, layoutRes,parent,false)
        return ProcessingOrderViewHolder(bindingProcessingOrder,variablesMap = getVariablesMap())
    }
}