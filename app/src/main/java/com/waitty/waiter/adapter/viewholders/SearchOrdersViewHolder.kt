package com.waitty.waiter.adapter.viewholders

import androidx.databinding.library.baseAdapters.BR
import com.waitty.waiter.databinding.CardSearchOrdersBinding
import com.waitty.waiter.model.OrderDetails

class SearchOrdersViewHolder(private val viewBinding : CardSearchOrdersBinding,private val variablesMap :  HashMap<Int,Any?>) : GenericOrderViewHolder<OrderDetails>(viewBinding = viewBinding) {

    override fun bind(item: OrderDetails, position: Int) {
        variablesMap[BR.position] = position
        setVariables(variablesMap)
    }
}