package com.waitty.waiter.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.waiter.R
import com.waitty.waiter.constant.constant
import com.waitty.waiter.databinding.FragmentProcessingOrdersLIstBinding
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.model.apimodels.OrderResponse
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.utility.WKItemClickListener
import com.waitty.waiter.viewmodel.ApiErrorViewModel
import com.waitty.waiter.viewmodel.NewOrdersViewModel
import com.waitty.waiter.viewmodel.ServedOrdersListViewModel

class ServedOrdersListFragment : Fragment(),WKItemClickListener {

    companion object {
        fun newInstance() = ServedOrdersListFragment()
    }

    private var viewModel: ServedOrdersListViewModel? = null
    private var homeFragment : HomeFragment? = null
    private var apiErrorViewModel : ApiErrorViewModel? = null
    private lateinit var bindingProcessingOrdersListFragment : FragmentProcessingOrdersLIstBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bindingProcessingOrdersListFragment = DataBindingUtil.inflate(inflater,R.layout.fragment_processing_orders_l_ist, container, false)
        viewModel = activity?.let { ViewModelProvider(it).get(ServedOrdersListViewModel::class.java) }
        apiErrorViewModel = activity?.let { ViewModelProvider(it).get(ApiErrorViewModel::class.java) }
        homeFragment = (parentFragment as? HomeFragment)
        return bindingProcessingOrdersListFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)
        viewModel?.setWaiterDetails(Utility.getWaiterName(context))
        bindingProcessingOrdersListFragment.layoutError.apiErrorVM = apiErrorViewModel
        bindingProcessingOrdersListFragment.rvOrders.layoutManager = LinearLayoutManager(context)
        bindingProcessingOrdersListFragment.rvOrders.setHasFixedSize(true)
        viewModel?.getOrderAdapter()?.setVariablesMap(getVariablesMap())
        bindingProcessingOrdersListFragment.rvOrders.adapter = viewModel?.getOrderAdapter()
        bindingProcessingOrdersListFragment.rvOrders.addOnScrollListener(RecyclerViewScrollListener())



    }

    private fun getVariablesMap(): HashMap<Int, Any?> {
        return hashMapOf(BR.preparedOrdersVM to viewModel, BR.itemClickEvent to this)
    }



    override fun onResume() {
        super.onResume()
        if(homeFragment == null) homeFragment = (parentFragment as? HomeFragment)
        homeFragment?.setPageTitle()
        homeFragment?.hideBackButton()
        FragmentUtils.hideKeyboard(bindingProcessingOrdersListFragment.root,context)
        refreshServedOrdersList()
    }


    override fun onItemClick(position: Int) {
        viewModel?.setSelectedOrderDetails(position)
        FragmentUtils.launchFragment(activity?.supportFragmentManager,R.id.nav_host_fragment,ServedOrderDetailsFragment(), constant.TAG_SERVED_ORDER_DETAILS)
//        viewModel?.updateOrderPreparationStatus(Utility.getToken(context))?.observe(viewLifecycleOwner, Observer { response ->
//            when(response.status) {
//                APIStatus.LOADING -> homeFragment?.showProgress(true)
//                APIStatus.ERROR -> showError(true,response.errorCode ?: 404, response.message)
//                APIStatus.SUCCESS ->  {
//                    showError(false,404,"")
//                    refreshNewOrdersList()
//                }
//            }
//        })

    }


    fun refreshServedOrdersList() {

        val previousListCount = viewModel?.getOrderListData()?.value?.size ?: 0

        viewModel?.fetchServedOrders(Utility.getToken(context))?.observe(viewLifecycleOwner, Observer { response ->

            when(response.status) {
                APIStatus.LOADING -> homeFragment?.showProgress(true)
                APIStatus.ERROR -> showError(true,response.errorCode ?: 404, Utility.getMessageOnErrorCode(response.errorCode,context))
                APIStatus.SUCCESS -> handleOrdersFetched(response.data,previousListCount)
            }
        })
    }

    private fun handleOrdersFetched(data: Any?, previousListCount: Int) {
        viewModel?.setOrdersList((data as? OrderResponse)?.data ?: ArrayList())
        showError(false,404,"")
        showNoInvite(viewModel?.getOrderListData()?.value?.size ?:0 == 0)
        if(previousListCount != viewModel?.getOrderListData()?.value?.size ?: 0 && viewModel?.getOrderListData()?.value?.size ?: 0 > 0) showNewInviteIndicator()
        else homeFragment?.showBadge(false)
        viewModel?.updateList()

    }

    private fun showNewInviteIndicator() {
        homeFragment?.showBadge(true)
    }

    private fun checkIfNoInvites() : Boolean {
        return viewModel?.getOrderListData()?.value?.size ?: 0 == 0
    }


    private fun showNoInvite(isVisible : Boolean) {
        bindingProcessingOrdersListFragment.rvOrders.visibility = if(isVisible) View.GONE else View.VISIBLE
        bindingProcessingOrdersListFragment.ivNoInvite.visibility = if(isVisible) View.VISIBLE else View.GONE
    }


    inner class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val topRowVerticalPosition =
                    if(bindingProcessingOrdersListFragment.rvOrders == null || bindingProcessingOrdersListFragment.rvOrders?.childCount == 0) 0 else bindingProcessingOrdersListFragment?.rvOrders?.getChildAt(0)?.top ?: 0;
            homeFragment?.enableSwipeRefresh(topRowVerticalPosition >= 0)
        }
    }


    fun showError(toShow: Boolean, errorCode: Int, errorMessage: String?) {
        homeFragment?.showRefresh(false)

        if (toShow) apiErrorViewModel?.let { FragmentUtils.showError( it, errorCode, errorMessage, true) } else apiErrorViewModel?.resetValues()
        bindingProcessingOrdersListFragment.rvOrders.visibility = if (toShow) View.GONE else View.VISIBLE
        bindingProcessingOrdersListFragment.ivNoInvite.visibility = if (toShow) View.GONE else View.VISIBLE
        if (toShow) {
            Handler().postDelayed({ showError(false, errorCode, "") }, 3000)
        }

        if(!toShow) showNoInvite(checkIfNoInvites())

    }


}