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
import androidx.lifecycle.Observer
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.constant.constant
import com.waitty.waiter.databinding.FragmentNewOrderDetailsBinding
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.viewmodel.ApiErrorViewModel
import com.waitty.waiter.viewmodel.ItemDescriptionViewModel
import com.waitty.waiter.viewmodel.ItemOrderViewModel
import com.waitty.waiter.viewmodel.NewOrdersViewModel
import kotlinx.android.synthetic.main.fragment_new_order_details.view.*


class NewOrderDetailsFragment : Fragment() {

    private lateinit var bindingNewOrderDetailsFragment: FragmentNewOrderDetailsBinding
    private var itemOrdersViewModel : ItemOrderViewModel? = null
    private var itemDescriptionViewModel : ItemDescriptionViewModel? = null
    private var apiErrorViewModel : ApiErrorViewModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bindingNewOrderDetailsFragment = DataBindingUtil.inflate(inflater,R.layout.fragment_new_order_details,container,false)

        val viewModelProvider = activity?.let { ViewModelProvider(it) }
        itemOrdersViewModel = viewModelProvider?.get(ItemOrderViewModel::class.java)
        itemDescriptionViewModel = viewModelProvider?.get(ItemDescriptionViewModel::class.java)
        apiErrorViewModel = viewModelProvider?.get(ApiErrorViewModel::class.java)

        val newOrderDetailsViewModel = viewModelProvider?.get(NewOrdersViewModel::class.java)

        bindingNewOrderDetailsFragment.layoutLoader.apiErrorVM = apiErrorViewModel
        bindingNewOrderDetailsFragment.layoutError.apiErrorVM = apiErrorViewModel

        itemOrdersViewModel?.getOrderDetails()?.value = newOrderDetailsViewModel?.getSelectedOrderDetails()?.value

        itemDescriptionViewModel?.getOrderItems()?.value = itemOrdersViewModel?.getOrderDetails()?.value?.orderItems
        itemDescriptionViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        bindingNewOrderDetailsFragment.orderDetailsVM = itemOrdersViewModel
        bindingNewOrderDetailsFragment.lifecycleOwner = viewLifecycleOwner
        hideBottomNavigationView(true)
        return bindingNewOrderDetailsFragment.root
    }

    private fun showBackButton() {
        (activity as? HomeActivity)?.setBackButtonVisibility(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemOrdersViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        itemOrdersViewModel?.setWaiterId(Utility.getWaiterName(context))
        setupRecyclerView()
        setPageDetails()
        showBackButton()
        FragmentUtils.hideKeyboard(bindingNewOrderDetailsFragment.rvOrderItems,context)
        bindingNewOrderDetailsFragment.btnAccept.btnAccept.setOnClickListener { v -> acceptOrder() }

    }

    private fun acceptOrder() {
        itemOrdersViewModel?.acceptOrder(Utility.getToken(context))?.observe(viewLifecycleOwner, Observer { response ->
            run {

                when (response.status) {
                    APIStatus.LOADING -> apiErrorViewModel?.let { FragmentUtils.showProgress(bindingNewOrderDetailsFragment.cardOrderDetails, true, getString(R.string.txt_accepting_order), it, false) }
                    APIStatus.ERROR -> apiErrorViewModel?.let {
                        FragmentUtils.showError(bindingNewOrderDetailsFragment.cardOrderDetails, it, response.errorCode,
                                if (!response.message.isNullOrEmpty()) response.message else Utility.getMessageOnErrorCode(response.errorCode, context), false)
                    }

                    APIStatus.SUCCESS -> handleSuccessResponse()

                }
            }
        })
    }

    private fun handleSuccessResponse() {
        apiErrorViewModel?.resetValues()
        val homeFragment = activity?.supportFragmentManager?.findFragmentByTag(constant.TAG_HOME) as? HomeFragment
        homeFragment?.let { it.refreshNewOrdersList()}
        FragmentUtils.goBackToPreviousScreen(activity,constant.TAG_HOME,constant.TAG_NEW_ORDER_DETAILS )

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = false
        bindingNewOrderDetailsFragment.rvOrderItems.layoutManager = layoutManager
        bindingNewOrderDetailsFragment.rvOrderItems.setHasFixedSize(true)
        itemOrdersViewModel?.getAdapter()?.setVariablesMap(hashMapOf(BR.itemDescriptionVM to itemDescriptionViewModel))
        bindingNewOrderDetailsFragment.rvOrderItems.adapter = itemOrdersViewModel?.getAdapter()
        itemOrdersViewModel?.updateList()
    }


    fun setPageDetails() {
        if (activity is HomeActivity) {
            (activity as HomeActivity).setBackButtonVisibility(true)
            (activity as HomeActivity).setPageTitle(String.format(getString(R.string.title_order_details),"#" +itemOrdersViewModel?.getOrderDetails()?.value?.id ?: ""))
        }
    }

    private fun hideBottomNavigationView(toHide : Boolean) {
       (activity as? HomeActivity)?.hideBottomNavigationMenu(toHide)
    }

}