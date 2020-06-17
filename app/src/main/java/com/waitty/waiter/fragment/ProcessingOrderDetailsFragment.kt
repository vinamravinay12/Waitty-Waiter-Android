package com.waitty.waiter.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.waitty.kitchen.fragment.FragmentUtils
import com.waitty.waiter.R
import com.waitty.waiter.activity.HomeActivity
import com.waitty.waiter.constant.constant
import com.waitty.waiter.databinding.FragmentProcessingOrderDetailsBinding
import com.waitty.waiter.model.APIStatus
import com.waitty.waiter.model.OrderDetails
import com.waitty.waiter.model.apimodels.OrderResponse
import com.waitty.waiter.utility.Utility
import com.waitty.waiter.viewmodel.*

class ProcessingOrderDetailsFragment :  Fragment() {

    private lateinit var bindingProcessingOrderDetails: FragmentProcessingOrderDetailsBinding
    private var itemOrdersViewModel : ItemOrderViewModel? = null
    private var apiErrorViewModel : ApiErrorViewModel? = null
    private var itemDescriptionViewModel : ItemDescriptionViewModel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        bindingProcessingOrderDetails = DataBindingUtil.inflate(inflater,R.layout.fragment_processing_order_details,container,false)

        val viewModelProvider = activity?.let { ViewModelProvider(it) }
        itemOrdersViewModel = viewModelProvider?.get(ItemOrderViewModel::class.java)
        itemDescriptionViewModel = viewModelProvider?.get(ItemDescriptionViewModel::class.java)
        val preparedOrdersViewModel = viewModelProvider?.get(ProcessingOrdersViewModel::class.java)
        apiErrorViewModel = viewModelProvider?.get(ApiErrorViewModel::class.java)
        bindingProcessingOrderDetails.layoutError.apiErrorVM = apiErrorViewModel
        bindingProcessingOrderDetails.layoutLoader.apiErrorVM = apiErrorViewModel

        itemOrdersViewModel?.getOrderDetails()?.value = preparedOrdersViewModel?.getSelectedOrderDetails()?.value

        itemDescriptionViewModel?.getOrderItems()?.value = itemOrdersViewModel?.getOrderDetails()?.value?.orderItems
        itemDescriptionViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        bindingProcessingOrderDetails.orderDetailsVM = itemOrdersViewModel
        bindingProcessingOrderDetails.lifecycleOwner = viewLifecycleOwner

        return bindingProcessingOrderDetails.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemOrdersViewModel?.setRupeeSymbol(getString(R.string.rupee_symbol))
        itemOrdersViewModel?.setWaiterId(Utility.getWaiterName(context))
        setupRecyclerView()
       checkForOrderStatus()
        FragmentUtils.hideKeyboard(bindingProcessingOrderDetails.rvOrderItems,context)

    }

    override fun onResume() {
        super.onResume()
        setPageDetails()
        showBackButton()
        scheduleAlarm()
        FragmentUtils.hideBottomNavigationView(activity,true)
    }

    private fun showBackButton() {
        if(activity is HomeActivity) {
            (activity as? HomeActivity)?.setBackButtonVisibility(true)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = false
        bindingProcessingOrderDetails.rvOrderItems.layoutManager = layoutManager
        bindingProcessingOrderDetails.rvOrderItems.setHasFixedSize(true)
        itemOrdersViewModel?.getAdapter()?.setVariablesMap(hashMapOf(BR.itemDescriptionVM to itemDescriptionViewModel))
        bindingProcessingOrderDetails.rvOrderItems.adapter = itemOrdersViewModel?.getAdapter()
        itemOrdersViewModel?.updateList()
    }


    fun setPageDetails() {
        if (activity is HomeActivity) {
            (activity as HomeActivity).setBackButtonVisibility(true)
            (activity as HomeActivity).setPageTitle(String.format(getString(R.string.title_order_details),"#" +itemOrdersViewModel?.getOrderDetails()?.value?.id ?: ""))
        }
    }



    fun scheduleAlarm() {

        val intent = Intent(context?.applicationContext, MyAlarmReceiver::class.java)
        val pIntent = PendingIntent.getBroadcast(context, 111,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val firstMillis = System.currentTimeMillis() // alarm is set right away
        val alarm = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                30*1000, pIntent)
    }


    inner class MyAlarmReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
          checkForOrderStatus()
        }

    }

    private fun checkForOrderStatus() {
      itemOrdersViewModel?.checkOrderStatus(Utility.getToken(context))?.observe(viewLifecycleOwner, Observer { response ->
          run {
              if (response.status == APIStatus.SUCCESS) {
                  val orderResponse = response.data as? OrderResponse
                  if(orderResponse?.data != null && orderResponse.data.isNotEmpty()) {
                      changeOrderStatus((response.data as? OrderResponse)?.data?.get(0))
                  }

              }
          }
      })
    }

    private fun changeOrderStatus(orderDetails: OrderDetails?) {
        itemOrdersViewModel?.getOrderDetails()?.value = orderDetails
        when(orderDetails?.orderStatus?.id) {
            constant.ORDER_PREPARING -> {
                setPreparingOrderStatusColors()
            }

            constant.ORDER_READY_SERVE -> {
                setPreparingOrderStatusColors()
                setOrderReadyStatusColors()
                showOrderServedDialog()
            }

            constant.ORDER_DELIVERED -> {
                setPreparingOrderStatusColors()
                setOrderReadyStatusColors()
                setOrderServedStatusColors()

            }
        }
    }


    private fun setPreparingOrderStatusColors() {
        bindingProcessingOrderDetails.flOrderStatusPreparing.backgroundTintList = context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_PREPARING.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.flOuterPreparing.elevationShadowColor = context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_PREPARING.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.flOuterPreparing.backgroundTintList = context?.let { ContextCompat.getColor(it,R.color.colorTransparent) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.tvOrderStatusPreparing.setTextColor(context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_PREPARING.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) })
    }

    private fun setOrderReadyStatusColors() {
        bindingProcessingOrderDetails.flOrderStatusReady.backgroundTintList = context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_READY_SERVE.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.flOuterReady.elevationShadowColor = context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_READY_SERVE.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.flOuterReady.backgroundTintList = context?.let { ContextCompat.getColor(it,R.color.colorWhite) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.tvOrderStatusReady.setTextColor(context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_READY_SERVE.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) })
    }


    private fun setOrderServedStatusColors() {

        bindingProcessingOrderDetails.flOrderStatusServed.backgroundTintList = context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_READY_SERVE.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.flOuterServed.backgroundTintList = context?.let { ContextCompat.getColor(it,R.color.colorWhite) }?.let { color -> ColorStateList.valueOf(color) }
        bindingProcessingOrderDetails.tvOrderStatusServed.setTextColor(context?.let { ContextCompat.getColor(it,OrderStatusColor.ORDER_READY_SERVE.colorOrderStatus) }?.let { color -> ColorStateList.valueOf(color) })
    }

    private fun showOrderServedDialog() {
        val mBottomSheetDialog: BottomSheetMaterialDialog? = activity?.let {
            BottomSheetMaterialDialog.Builder(it)
                .setTitle(getString(R.string.txt_title_order_served))
                .setMessage(getString(R.string.txt_message_order_served))
                .setCancelable(false)
                .setPositiveButton("Served", R.drawable.ic_accept) { dialogInterface, which ->
                    handleOrderDelivered()
                }
                    .setNegativeButton("Cancel", R.drawable.ic_close) { dialogInterface, which -> dialogInterface.dismiss() }
                    .build()
        }


        mBottomSheetDialog?.show()
    }

    private fun handleOrderDelivered() {
        itemOrdersViewModel?.setOrderDelivered(Utility.getToken(context))?.observe(viewLifecycleOwner, Observer { response ->
            run {
                when (response.status) {
                    APIStatus.LOADING -> apiErrorViewModel?.let { FragmentUtils.showProgress(bindingProcessingOrderDetails.cardOrderDetails, true, getString(R.string.txt_accepting_order), it, false) }
                    APIStatus.ERROR -> apiErrorViewModel?.let {
                        FragmentUtils.showError(bindingProcessingOrderDetails.cardOrderDetails, it, response.errorCode,
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
        homeFragment?.let { it.refreshProcessingOrdersList()}
        FragmentUtils.goBackToPreviousScreen(activity,constant.TAG_HOME,constant.TAG_NEW_ORDER_DETAILS )
    }

}