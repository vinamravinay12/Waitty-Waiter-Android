package com.waitty.waiter.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.waiter.R
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.adapter.SearchOrdersAdapter
import com.waitty.waiter.constant.constant
import com.waitty.waiter.databinding.FragmentSearchOrdersListBinding
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.apimodels.OrderResponse
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.utility.WKItemClickListener
import com.waitty.waiter.viewmodel.*


class SearchOrdersListFragment : Fragment(),WKItemClickListener {

    private lateinit var bindingSearchOrders : FragmentSearchOrdersListBinding
    private var searchOrdersViewModel : SearchOrdersViewModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bindingSearchOrders = DataBindingUtil.inflate(inflater,R.layout.fragment_search_orders_list, container, false)
        searchOrdersViewModel = activity?.let { ViewModelProvider(it).get(SearchOrdersViewModel::class.java) }

        bindingSearchOrders.lifecycleOwner = this
        return bindingSearchOrders.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingSearchOrders.rvSearchOrders.layoutManager = LinearLayoutManager(context)

        bindingSearchOrders.rvSearchOrders.setHasFixedSize(true)
        bindingSearchOrders.rvSearchOrders.adapter = searchOrdersViewModel?.getOrderAdapter()

        searchOrdersViewModel?.getOrderAdapter()?.setVariablesMap(getVariablesMap())


        bindingSearchOrders.rvSearchOrders.addOnScrollListener(RecyclerViewScrollListener())
        searchOrdersViewModel?.updateList()
        bindingSearchOrders.etOrderSearch.doAfterTextChanged { refreshSearcOrdersList() }
    }

    private fun getVariablesMap(): HashMap<Int, Any?> {
        return hashMapOf(BR.searchOrdersVM to searchOrdersViewModel, BR.itemClickEvent to this)
    }


    override fun onResume() {
        super.onResume()
        val newOrdersViewModel = activity?.let { ViewModelProvider(it).get(NewOrdersViewModel::class.java) }
        val processingOrdersViewModel = activity?.let { ViewModelProvider(it).get(ProcessingOrdersViewModel::class.java) }
        val servedOrdersListViewModel = activity?.let { ViewModelProvider(it).get(ServedOrdersListViewModel::class.java) }

        searchOrdersViewModel?.getOrderListData()?.value = ArrayList()

        val newOrdersList  =  newOrdersViewModel?.getOrderListData()?.value ?: ArrayList()
        val processingOrdersList = processingOrdersViewModel?.getOrderListData()?.value ?: ArrayList()
        val servedOrderList = servedOrdersListViewModel?.getOrderListData()?.value ?: ArrayList()

         (searchOrdersViewModel?.getOrderListData()?.value as ArrayList<OrderDetails>).addAll(newOrdersList)
        (searchOrdersViewModel?.getOrderListData()?.value as ArrayList<OrderDetails>).addAll(processingOrdersList)
        (searchOrdersViewModel?.getOrderListData()?.value as ArrayList<OrderDetails>).addAll(servedOrderList)

        if(searchOrdersViewModel?.getOrderListData()?.value?.size ?: 0 > 0) showNoInvite(false) else showNoInvite(true)
        searchOrdersViewModel?.updateList()

        if(activity is HomeActivity) {
            (activity as? HomeActivity)?.setPageTitle("Search Orders")
            (activity as? HomeActivity)?.setBackButtonVisibility(true)
        }
        
        FragmentUtils.hideKeyboard(bindingSearchOrders.root,context)

    }


    override fun onItemClick(position: Int) {
        searchOrdersViewModel?.setSelectedOrderDetails(position)
        FragmentUtils.launchFragment(activity?.supportFragmentManager,R.id.nav_host_fragment,SearchOrderDetailsFragment(), constant.TAG_SEARCH_ORDERS_DETAILS)


    }


    fun refreshSearcOrdersList() {

        if(bindingSearchOrders.etOrderSearch.text.toString().isNullOrEmpty()) { return }
        val searchTerm = bindingSearchOrders.etOrderSearch.text.toString()
        (searchOrdersViewModel?.getOrderAdapter() as? SearchOrdersAdapter)?.filter?.filter(searchTerm)
    }




    private fun checkIfNoInvites() : Boolean {
        return searchOrdersViewModel?.getOrderListData()?.value?.size ?: 0 == 0
    }


    private fun showNoInvite(isVisible : Boolean) {
        bindingSearchOrders.rvSearchOrders.visibility = if(isVisible) View.GONE else View.VISIBLE
        bindingSearchOrders.ivNoInvite.visibility = if(isVisible) View.VISIBLE else View.GONE
    }


    inner class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val topRowVerticalPosition =
                    if(bindingSearchOrders.rvSearchOrders == null || bindingSearchOrders.rvSearchOrders?.childCount == 0) 0 else bindingSearchOrders?.rvSearchOrders?.getChildAt(0)?.top ?: 0;

        }
    }


}