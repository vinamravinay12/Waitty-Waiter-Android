package com.waitty.waiter.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.waitty.waiter.R

class ServedOrderDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ServedOrderDetailsFragment()
    }

    private lateinit var viewModel: ServedOrderDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.served_order_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ServedOrderDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}