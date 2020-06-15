package com.waitty.waiter.utility

import android.content.Context
import com.waitty.waiter.viewmodel.ClickType

interface WKClickListener {

    fun onClick(clickType: ClickType)
}

interface WKItemClickListener {
    fun onItemClick(position: Int)
}


interface ViewInteractionHandler {
    fun setContext(context: Context)
}


interface KeyItemActionListener {
    fun onKeyEvent(valueEntered: String, data: Any?)
}

interface WKCheckChangeListener {
    fun onCheckChanged(item : Any?)
}

interface WKFilterResultListener<E> {
    fun onResultsFiltered(filteredList : List<E>)
}