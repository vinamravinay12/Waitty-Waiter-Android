package com.waitty.waiter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.waitty.waiter.R
import com.waitty.waiter.adapter.viewholders.GenericOrderViewHolder
import com.waitty.waiter.adapter.viewholders.OrderItemCustomizationOptionTypeViewHolder
import com.waitty.waiter.adapter.viewholders.OrderItemCustomizationOptionViewHolder
import com.waitty.waiter.databinding.LayoutOrderItemCustomizationHeaderBinding
import com.waitty.waiter.databinding.LayoutTextItemCustomizationBinding
import java.util.*

class OrderItemCustomizationOptionsAdapter(private val dataList : MutableList<String>?, private var dataMap : TreeMap<String,List<String>>) : GenericOrderAdapter<String>(dataList) {

    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val  VIEW_TYPE_ITEM = 2
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericOrderViewHolder<String> {

        return if(viewType == VIEW_TYPE_HEADER) getHeaderViewHolder(parent) else getItemViewHolder(parent)
    }

    private fun getItemViewHolder(parent : ViewGroup) : GenericOrderViewHolder<String> {
        val inflater = LayoutInflater.from(parent.context)
        val itemViewHolder = DataBindingUtil.inflate<LayoutTextItemCustomizationBinding>(inflater, R.layout.layout_text_item_customization,parent,false)
        return OrderItemCustomizationOptionViewHolder(itemViewHolder)
    }

    private fun getHeaderViewHolder(parent: ViewGroup): GenericOrderViewHolder<String> {
        val inflater = LayoutInflater.from(parent.context)
        val headerViewHolder = DataBindingUtil.inflate<LayoutOrderItemCustomizationHeaderBinding>(inflater, R.layout.layout_order_item_customization_header,parent,false)
        return OrderItemCustomizationOptionTypeViewHolder(headerViewHolder)
    }

    override fun getItemViewType(position: Int): Int {
       return if(dataMap.keys.contains(dataList?.get(position))) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }



    fun updateList(newList : MutableList<String>?, newDataMap : TreeMap<String,List<String>>?) {
        dataList?.clear()
        if (newDataMap != null) {
            dataMap = newDataMap
        }

        newList?.let { dataList?.addAll(it) }
        notifyDataSetChanged()
    }
}