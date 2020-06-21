package com.waitty.waiter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import com.waitty.waiter.adapter.viewholders.GenericOrderViewHolder
import com.waitty.waiter.adapter.viewholders.SearchOrdersViewHolder
import com.waitty.waiter.databinding.CardSearchOrdersBinding
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.utility.WKFilterResultListener

class SearchOrdersAdapter (private val layoutRes : Int, private var dataList: MutableList<OrderDetails>?, private val filterResultListener: WKFilterResultListener<OrderDetails>) : GenericOrderAdapter<OrderDetails>(dataList), Filterable  {
    private val completeList = ArrayList<OrderDetails>()
    init {
        if(dataList == null)dataList = ArrayList()
        completeList.clear()
        dataList?.let { completeList.addAll(it) }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericOrderViewHolder<OrderDetails> {
        val inflater = LayoutInflater.from(parent.context)
        val bindingPreparedOrders = DataBindingUtil.inflate<CardSearchOrdersBinding >(inflater, layoutRes,parent,false)
        return SearchOrdersViewHolder(bindingPreparedOrders,variablesMap = getVariablesMap())
    }

    override fun getFilter(): Filter {
        return OrdersFilter(completeList,filterResultListener)
    }

    fun updateFilterList(newList: MutableList<OrderDetails>?) {
        dataList?.clear()
        newList?.let {
            dataList?.addAll(it)

        }

        notifyDataSetChanged()
    }

    override fun updateList(newList : MutableList<OrderDetails>?) {
        dataList?.clear()
        completeList.clear()
        newList?.let {
            dataList?.addAll(it)
            completeList.addAll(it)
        }

        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: GenericOrderViewHolder<OrderDetails>, position: Int) {
        val item = dataList?.get(position)
        item?.let { holder.bind(it,position) }
    }
}