package com.waitty.waiter.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.waitty.waiter.fragment.*
import java.lang.ref.WeakReference

class OrderPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var currentFragmentWeakReference: WeakReference<Fragment>? = null
    override fun getItemCount(): Int {
       return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val newOrderDetailsListFragment: Fragment = NewOrdersListFragment()
                currentFragmentWeakReference = WeakReference(newOrderDetailsListFragment)
                return newOrderDetailsListFragment
            }
            1-> {
                val preparingOrderListFragment: Fragment = ProcessingOrdersListFragment()
                currentFragmentWeakReference = WeakReference(preparingOrderListFragment)
                return preparingOrderListFragment
            }
            2 -> {
                val preparedOrdersFragment: Fragment = ServedOrdersListFragment()
                currentFragmentWeakReference = WeakReference(preparedOrdersFragment)
                return preparedOrdersFragment
            }
        }
        return NewOrderListFragment()
    }
}