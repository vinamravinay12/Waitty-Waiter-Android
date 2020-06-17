package com.waitty.waiter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.google.gson.Gson
import com.translabtechnologies.visitormanagementsystem.vmshost.database.SharedPreferenceManager
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.kitchen.fragment.FragmentUtils.hideKeyboard
import com.waitty.waiter.R
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.adapter.OrderPagerAdapter
import com.waitty.waiter.constant.constant
import com.waitty.waiter.databinding.FragmentHomeBinding
import com.waitty.waiter.model.LoginUser
import com.waitty.waiter.retrofit.API
import com.waitty.waiter.retrofit.API.WAITER_USER
import com.waitty.waiter.viewmodel.ApiErrorViewModel

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {


    private lateinit var fragmentHomeBinding: FragmentHomeBinding
   // private var apiErrorViewModel: ApiErrorViewModel? = null


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        (activity as? HomeActivity)?.setBackButtonVisibility(false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentHomeBinding.swipeRefresh.setOnRefreshListener(this)
        fragmentHomeBinding.ordersViewPager.adapter = OrderPagerAdapter(this)



        TabLayoutMediator(fragmentHomeBinding.orderTabLayout, fragmentHomeBinding.ordersViewPager, TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.txt_new)
                1 -> tab.text = getString(R.string.txt_preparing)
                2 -> tab.text = getString(R.string.txt_served)
            }
        }).attach()
        fragmentHomeBinding.ordersViewPager.currentItem = 0
        fragmentHomeBinding.ordersViewPager.registerOnPageChangeCallback(PageChangeCallbackHandler())

    }

    override fun onStart() {
        super.onStart()
        fragmentHomeBinding.ordersViewPager.currentItem = 0
    }

    override fun onResume() {
        super.onResume()
        hideBackButton()
        setPageTitle()

        FragmentUtils.hideBottomNavigationView(activity,false)
        hideKeyboard(fragmentHomeBinding.ordersViewPager.getRootView(), context)
    }

    override fun onRefresh() {
        refreshVisibleFragment(fragmentHomeBinding.ordersViewPager.getCurrentItem())
    }

    inner class PageChangeCallbackHandler : OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            refreshVisibleFragment(position)
        }
    }

    private fun refreshVisibleFragment(position: Int) {
        if (childFragmentManager.fragments.size <= position) return
        val fragment = childFragmentManager.fragments[position]
        when (position) {
            0 -> if (fragment is NewOrdersListFragment && fragment.isVisible()) {
                (fragment as? NewOrdersListFragment)?.refreshNewOrdersList()
            } else fragmentHomeBinding.swipeRefresh.isRefreshing = false
            1 -> if (fragment is ProcessingOrdersListFragment && fragment.isVisible) {
                (fragment as? ProcessingOrdersListFragment)?.refreshProcessingOrdersList()
            } else fragmentHomeBinding.swipeRefresh.isRefreshing = false
            2 -> if (fragment is ServedOrdersListFragment && fragment.isVisible) {
                (fragment as? ServedOrdersListFragment)?.refreshServedOrdersList()
            } else fragmentHomeBinding.swipeRefresh.isRefreshing = false
        }
    }

    fun showProgress(toShow: Boolean) {
        fragmentHomeBinding.swipeRefresh.setRefreshing(toShow)
    }


    fun enableSwipeRefresh(isEnabled: Boolean) {
        fragmentHomeBinding.swipeRefresh.setEnabled(isEnabled)
    }

     fun hideBackButton() {
        if (activity is HomeActivity) {
            (activity as HomeActivity).setBackButtonVisibility(false)
        }
    }

     fun setPageTitle() {
        if (activity is HomeActivity) {
            (activity as? HomeActivity)?.setPageTitle(getString(R.string.title_order_history))
        }
    }


    fun showRefresh(toShow: Boolean) {
        fragmentHomeBinding.swipeRefresh.isRefreshing = toShow
    }

    fun showBadge(toShow: Boolean) {
        val badgeDrawable: BadgeDrawable? = fragmentHomeBinding.orderTabLayout.getTabAt(0)?.getOrCreateBadge()
        badgeDrawable?.backgroundColor = ResourcesCompat.getColor(resources, R.color.colorCountdownTimer, null)
        if (toShow) badgeDrawable?.isVisible = true else fragmentHomeBinding.orderTabLayout.getTabAt(0)?.removeBadge()
    }

    fun refreshNewOrdersList() {
        val fragment = childFragmentManager.fragments[0]
        if (fragment is NewOrdersListFragment) {
            (fragment as? NewOrdersListFragment)?.refreshNewOrdersList()
        }
    }


    fun refreshProcessingOrdersList() {

        val fragment = childFragmentManager.fragments[1]
        if (fragment is ProcessingOrdersListFragment) {
            (fragment as? ProcessingOrdersListFragment)?.refreshProcessingOrdersList()
        }
    }



}