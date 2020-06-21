package com.waitty.waiter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.waiter.R
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.databinding.FragmentSearchOrderDetailsBinding
import com.waitty.waiter.databinding.ServedOrderDetailsFragmentBinding
import com.waitty.waiter.viewmodel.ItemDescriptionViewModel
import com.waitty.waiter.viewmodel.ItemOrderViewModel
import com.waitty.waiter.viewmodel.SearchOrdersViewModel
import com.waitty.waiter.viewmodel.ServedOrdersListViewModel

class SearchOrderDetailsFragment : Fragment() {
    private lateinit var bindingSearchOrderDetail: FragmentSearchOrderDetailsBinding
    private var itemOrdersViewModel: ItemOrderViewModel? = null
    private var itemDescriptionViewModel: ItemDescriptionViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bindingSearchOrderDetail = DataBindingUtil.inflate(inflater, R.layout.fragment_search_order_details, container, false)

        val viewModelProvider = activity?.let { ViewModelProvider(it) }
        itemOrdersViewModel = viewModelProvider?.get(ItemOrderViewModel::class.java)
        itemDescriptionViewModel = viewModelProvider?.get(ItemDescriptionViewModel::class.java)
        val preparedOrdersViewModel = viewModelProvider?.get(SearchOrdersViewModel::class.java)

        itemOrdersViewModel?.getOrderDetails()?.value = preparedOrdersViewModel?.getSelectedOrderDetails()?.value

        itemDescriptionViewModel?.getOrderItems()?.value = itemOrdersViewModel?.getOrderDetails()?.value?.orderItems
        itemDescriptionViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        bindingSearchOrderDetail.orderDetailsVM = itemOrdersViewModel
        bindingSearchOrderDetail.lifecycleOwner = viewLifecycleOwner
        return bindingSearchOrderDetail.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemOrdersViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        setupRecyclerView()
        setPageDetails()
        showBackButton()
        FragmentUtils.hideKeyboard(bindingSearchOrderDetail.rvOrderItems, context)

    }

    private fun showBackButton() {
        if (activity is HomeActivity) {
            (activity as? HomeActivity)?.setBackButtonVisibility(true)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = false
        bindingSearchOrderDetail.rvOrderItems.layoutManager = layoutManager
        bindingSearchOrderDetail.rvOrderItems.setHasFixedSize(true)
        itemOrdersViewModel?.getAdapter()?.setVariablesMap(hashMapOf(BR.itemDescriptionVM to itemDescriptionViewModel))
        bindingSearchOrderDetail.rvOrderItems.adapter = itemOrdersViewModel?.getAdapter()
        itemOrdersViewModel?.updateList()
    }


    fun setPageDetails() {
        if (activity is HomeActivity) {
            (activity as HomeActivity).setBackButtonVisibility(true)
            (activity as HomeActivity).setPageTitle(String.format(getString(R.string.title_order_details), "#" + itemOrdersViewModel?.getOrderDetails()?.value?.id
                    ?: ""))
        }
    }

}
