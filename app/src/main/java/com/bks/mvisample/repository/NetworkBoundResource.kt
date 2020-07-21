package com.bks.mvisample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.bks.mvisample.ui.main.state.MainViewState
import com.bks.mvisample.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject, ViewStateType> {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {
        result.value = DataState.loading(true)

        GlobalScope.launch(IO) {
            delay(Constants.TESTING_NETWORK_DELAY)

            withContext(Main) {
                val apiResponse = createCall()
                result.addSource(apiResponse) {response ->
                    result.removeSource(apiResponse)

                    handleNetworkResponse(response)
                }
            }
        }
    }

    private fun handleNetworkResponse(response: GenericApiResponse<ResponseObject>) {
        when(response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse -> {
                println("DEBUG: NetworkBoundResource : ${response.errorMessage}")
                onReturnError(response.errorMessage)
            }
            is ApiEmptyResponse -> {
                println("DEBUG: NetworkBoundResource : HTTP 204. Returned NOTHING!")
                onReturnError("HTTP 204. Returned NOTHING!")
            }
        }
    }

    fun onReturnError(message : String) {
        result.value = DataState.error(message)
    }

    abstract fun handleApiSuccessResponse(response : ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

}