package com.waitty.kitchen.adapter.viewholders

import com.waitty.kitchen.adapter.viewholders.GenericOrderViewHolder
import com.waitty.waiter.databinding.LayoutOrderItemCustomizationHeaderBinding


class OrderItemCustomizationOptionTypeViewHolder(private val viewBinding : LayoutOrderItemCustomizationHeaderBinding) : GenericOrderViewHolder<String>(viewBinding) {

    override fun bind(item: String, position: Int) {
        viewBinding.tvOrderItemDescriptionHeader.text = item
    }
}