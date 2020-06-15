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
import com.waitty.kitchen.fragment.FragmentUtils.hideKeyboard
import com.waitty.waiter.R
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.adapter.OrderPagerAdapter
import com.waitty.waiter.databinding.FragmentHomeBinding
import com.waitty.waiter.viewmodel.ApiErrorViewModel

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {


    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private var apiErrorViewModel: ApiErrorViewModel? = null


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        apiErrorViewModel = activity?.let { ViewModelProvider(it).get<ApiErrorViewModel>(ApiErrorViewModel::class.java) }

        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentHomeBinding.swipeRefresh.setOnRefreshListener(this)
        fragmentHomeBinding.ordersViewPager.adapter = OrderPagerAdapter(this)
        hideBackButton()
        setPageTitle()
        TabLayoutMediator(fragmentHomeBinding.orderTabLayout, fragmentHomeBinding.ordersViewPager, TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.txt_new)
                1 -> tab.text = getString(R.string.txt_preparing)
                2 -> tab.text = getString(R.string.txt_served)
            }
        }).attach()
        fragmentHomeBinding.ordersViewPager.registerOnPageChangeCallback(PageChangeCallbackHandler())
    }


    override fun onResume() {
        super.onResume()
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
//            0 -> if (fragment is NewOrderDetailsFragment && fragment.isVisible()) {
//                fragment.refreshNewOrdersList()
//            } else fragmentHomeBinding.swipeRefresh.setRefreshing(false)
//            1 -> if (fragment is PreparingOrderDetailsListFragment && fragment.isVisible) {
//                (fragment as PreparingOrderDetailsListFragment).refreshPreparingOrdersList()
//            } else fragmentHomeBinding.swipeRefresh.setRefreshing(false)
//            2 -> if (fragment is PreparedOrdersFragment && fragment.isVisible) {
//                (fragment as PreparedOrdersFragment).fetchPreparedOrders()
//            } else fragmentHomeBinding.swipeRefresh.setRefreshing(false)
        }
    }

    fun showProgress(toShow: Boolean) {
        fragmentHomeBinding.swipeRefresh.setRefreshing(toShow)
    }


    fun enableSwipeRefresh(isEnabled: Boolean) {
        fragmentHomeBinding.swipeRefresh.setEnabled(isEnabled)
    }

    private fun hideBackButton() {
        if (activity is HomeActivity) {
            (activity as HomeActivity).setBackButtonVisibility(false)
        }
    }

    private fun setPageTitle() {
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


}