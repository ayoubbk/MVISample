package com.bks.mvisample.experimental

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer




/** this function demonstrate how we can build custom Transformation using MediatorLiveData
 * if the value of one item among inputs list change (), the Observer callback is fired to update the MediatorLiveData value (result)
 * it will perform unsubscribe/subscribe process I guess !
 **/
fun totalLength(inputs : List<LiveData<String>>) : LiveData<Int> {
    val result = MediatorLiveData<Int>()
    val doSum = Observer<String> {
        result.value = inputs.sumBy { it.value?.length?:0 }
    }

    inputs.forEach {
        result.addSource(it, doSum)
    }
    return result
}