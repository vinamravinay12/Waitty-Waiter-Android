package com.waitty.kitchen.adapter.viewholders

import androidx.core.view.ViewCompat
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waitty.waiter.model.OrderDetails
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap



/*
class PreparingItemOrdersViewHolder(private val viewBinding: LayoutItemOrderHeaderBinding,private val variablesMap : HashMap<Int,Any?>) : GenericOrderViewHolder<OrderDetails.OrderItem>(viewBinding) {


    override fun bind(item: OrderItem, position: Int) {
        viewBinding.layoutItemDescription.orderItem = item
        variablesMap[BR.position] = position
        setVariables(variablesMap)
        val map = viewBinding.itemDescriptionVM?.getOptionsMap(item)
        val list = map?.let { viewBinding.itemDescriptionVM?.getAllItemsList(it) }
        setupRecyclerView(viewBinding.layoutOrderDescription?.rvOrderCustomizationOptions,map,list)

    }

    private fun setupRecyclerView(rvOrderCustomizationOptions: RecyclerView?, map: TreeMap<String, List<String>>?, list: ArrayList<String>?) {

        val orderItemCustomizationsAdapter = map?.let { OrderItemCustomizationOptionsAdapter(list?.toMutableList(), it) }
        rvOrderCustomizationOptions?.layoutManager = LinearLayoutManager(itemView.context)
        rvOrderCustomizationOptions?.setHasFixedSize(true)
        if (rvOrderCustomizationOptions != null) {
            ViewCompat.setNestedScrollingEnabled(rvOrderCustomizationOptions,false)
        }
        rvOrderCustomizationOptions?.adapter = orderItemCustomizationsAdapter

    }




}*/
