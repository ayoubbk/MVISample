package com.bks.mvisample.ui

import com.bks.mvisample.util.DataState

interface DataStateListener {

    fun onDataStateChange(dataState : DataState<*>)
}