package com.waitty.waiter.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.waiter.R
import androidx.databinding.library.baseAdapters.BR
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.databinding.FragmentNewOrderDetailsBinding
import com.waitty.waiter.databinding.ServedOrderDetailsFragmentBinding
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.viewmodel.ItemDescriptionViewModel
import com.waitty.waiter.viewmodel.ItemOrderViewModel
import com.waitty.waiter.viewmodel.ServedOrdersListViewModel

class ServedOrderDetailsFragment : Fragment() {

    private lateinit var bindingServedOrders: ServedOrderDetailsFragmentBinding
    private var itemOrdersViewModel : ItemOrderViewModel? = null
    private var itemDescriptionViewModel : ItemDescriptionViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bindingServedOrders = DataBindingUtil.inflate(inflater,R.layout.served_order_details_fragment,container,false)

        val viewModelProvider = activity?.let { ViewModelProvider(it) }
        itemOrdersViewModel = viewModelProvider?.get(ItemOrderViewModel::class.java)
        itemDescriptionViewModel = viewModelProvider?.get(ItemDescriptionViewModel::class.java)
        val preparedOrdersViewModel = viewModelProvider?.get(ServedOrdersListViewModel::class.java)

        itemOrdersViewModel?.getOrderDetails()?.value = preparedOrdersViewModel?.getSelectedOrderDetails()?.value

        itemDescriptionViewModel?.getOrderItems()?.value = itemOrdersViewModel?.getOrderDetails()?.value?.orderItems
        itemDescriptionViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        bindingServedOrders.orderDetailsVM = itemOrdersViewModel
        bindingServedOrders.lifecycleOwner = viewLifecycleOwner
        return bindingServedOrders.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemOrdersViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        itemOrdersViewModel?.setWaiterId(Utility.getWaiterName(context))
        setupRecyclerView()
        setPageDetails()
        showBackButton()
        FragmentUtils.hideKeyboard(bindingServedOrders.rvOrderItems,context)

    }

    private fun showBackButton() {
        if(activity is HomeActivity) {
            (activity as? HomeActivity)?.setBackButtonVisibility(true)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = false
        bindingServedOrders.rvOrderItems.layoutManager = layoutManager
        bindingServedOrders.rvOrderItems.setHasFixedSize(true)
        itemOrdersViewModel?.getAdapter()?.setVariablesMap(hashMapOf(BR.itemDescriptionVM to itemDescriptionViewModel))
        bindingServedOrders.rvOrderItems.adapter = itemOrdersViewModel?.getAdapter()
        itemOrdersViewModel?.updateList()
    }


    fun setPageDetails() {
        if (activity is HomeActivity) {
            (activity as HomeActivity).setBackButtonVisibility(true)
            (activity as HomeActivity).setPageTitle(String.format(getString(R.string.title_order_details),"#" +itemOrdersViewModel?.getOrderDetails()?.value?.id ?: ""))
        }
    }

}

