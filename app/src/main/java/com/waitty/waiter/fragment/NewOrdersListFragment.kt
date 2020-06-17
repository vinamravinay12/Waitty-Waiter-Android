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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.waiter.R
import com.waitty.waiter.constant.constant
import com.waitty.waiter.databinding.FragmentNewOrdersListBinding
import com.waitty.waiter.databinding.FragmentNeworderListBinding
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.model.apimodels.OrderResponse
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.utility.WKItemClickListener
import com.waitty.waiter.viewmodel.ApiErrorViewModel
import com.waitty.waiter.viewmodel.NewOrdersViewModel

class NewOrdersListFragment : Fragment(),WKItemClickListener {


    companion object {
        fun newInstance() = NewOrderDetailsFragment()
    }

    private var viewModel: NewOrdersViewModel? = null
    private var homeFragment : HomeFragment? = null
    private var apiErrorViewModel : ApiErrorViewModel? = null
    private lateinit var bindingNewOrdersListFragment : FragmentNewOrdersListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bindingNewOrdersListFragment = DataBindingUtil.inflate(inflater,R.layout.fragment_new_orders_list, container, false)
        viewModel = activity?.let { ViewModelProvider(it).get(NewOrdersViewModel::class.java) }
        apiErrorViewModel = activity?.let { ViewModelProvider(it).get(ApiErrorViewModel::class.java) }
        homeFragment = (parentFragment as? HomeFragment)
        return bindingNewOrdersListFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)
        viewModel?.setWaiterId(Utility.getWaiterName(context))
        apiErrorViewModel?.resetValues()
        bindingNewOrdersListFragment.layoutError.apiErrorVM = apiErrorViewModel
        bindingNewOrdersListFragment.rvNewOrders.layoutManager = LinearLayoutManager(context)

        bindingNewOrdersListFragment.rvNewOrders.setHasFixedSize(true)
        bindingNewOrdersListFragment.rvNewOrders.adapter = viewModel?.getOrderAdapter()
        viewModel?.getOrderAdapter()?.setVariablesMap(getVariablesMap())

        bindingNewOrdersListFragment.rvNewOrders.addOnScrollListener(RecyclerViewScrollListener())
        scheduleAlarm()


    }

    private fun getVariablesMap(): HashMap<Int, Any?> {
        return hashMapOf(BR.newOrdersVM to viewModel, BR.itemClickEvent to this)
    }


    override fun onResume() {
        super.onResume()
        if(homeFragment == null) homeFragment = (parentFragment as? HomeFragment)
        homeFragment?.setPageTitle()
        homeFragment?.hideBackButton()
        showNoInvite(viewModel?.getOrderListData()?.value.isNullOrEmpty())
        FragmentUtils.hideKeyboard(bindingNewOrdersListFragment.root,context)
        refreshNewOrdersList()
    }


    override fun onItemClick(position: Int) {
        viewModel?.setSelectedOrderDetails(position)
        FragmentUtils.launchFragment(activity?.supportFragmentManager,R.id.nav_host_fragment,NewOrderDetailsFragment(), constant.TAG_NEW_ORDER_DETAILS)


    }


    fun refreshNewOrdersList() {

        val previousListCount = viewModel?.getOrderListData()?.value?.size ?: 0

        viewModel?.fetchNewOrders(Utility.getToken(context))?.observe(viewLifecycleOwner, Observer { response ->

             when(response.status) {
                APIStatus.LOADING -> homeFragment?.showProgress(true)
                APIStatus.ERROR -> showError(true,response.errorCode ?: 404,Utility.getMessageOnErrorCode(response.errorCode,context))
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
        bindingNewOrdersListFragment.rvNewOrders.visibility = if(isVisible) View.GONE else View.VISIBLE
        bindingNewOrdersListFragment.ivNoInvite.visibility = if(isVisible) View.VISIBLE else View.GONE
    }


    inner class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val topRowVerticalPosition =
                    if(bindingNewOrdersListFragment.rvNewOrders == null || bindingNewOrdersListFragment.rvNewOrders?.childCount == 0) 0 else bindingNewOrdersListFragment?.rvNewOrders?.getChildAt(0)?.top ?: 0;
            homeFragment?.enableSwipeRefresh(topRowVerticalPosition >= 0)
        }
    }


    fun showError(toShow: Boolean, errorCode: Int, errorMessage: String?) {
        homeFragment?.showRefresh(false)

        if (toShow) apiErrorViewModel?.let { FragmentUtils.showError( it, errorCode, errorMessage, true) } else apiErrorViewModel?.resetValues()
        bindingNewOrdersListFragment.rvNewOrders.visibility = if (toShow) View.GONE else View.VISIBLE
        bindingNewOrdersListFragment.ivNoInvite.visibility = if (toShow) View.GONE else View.VISIBLE
        if (toShow) {
            Handler().postDelayed({ showError(false, errorCode, "") }, 3000)
        }

        if(!toShow) showNoInvite(checkIfNoInvites())

    }

    fun scheduleAlarm() {

        val intent = Intent(context?.applicationContext, MyAlarmReceiver::class.java)
        // Create a PendingIntent to be triggered when the alarm goes off
        val pIntent = PendingIntent.getBroadcast(context, 111,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val firstMillis = System.currentTimeMillis() // alarm is set right away
        val alarm = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                30*1000, pIntent)
    }


    inner class MyAlarmReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            refreshNewOrdersList()
        }

    }

}