package com.waitty.kitchen.adapter.viewholders

import com.waitty.waiter.databinding.LayoutTextItemCustomizationBinding


class OrderItemCustomizationOptionViewHolder(private val viewBinding : LayoutTextItemCustomizationBinding) : GenericOrderViewHolder<String>(viewBinding) {

    override fun bind(item: String, position: Int) {
        viewBinding.tvOrderItemCustomizationOption.text = item
    }

}