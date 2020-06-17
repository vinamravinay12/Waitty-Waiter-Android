package com.waitty.waiter.adapter.viewholders

import androidx.databinding.library.baseAdapters.BR
import com.waitty.waiter.databinding.LayoutOrderProcessingBinding
import com.waitty.waiter.model.OrderDetails

class ProcessingOrderViewHolder(private val viewDataBinding: LayoutOrderProcessingBinding, private val variablesMap : HashMap<Int,Any?>) : GenericOrderViewHolder<OrderDetails>(viewDataBinding) {

    override fun bind(item: OrderDetails, position : Int) {

        variablesMap[BR.position] = position
        setVariables(variablesMap)

    }

}