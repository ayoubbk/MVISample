package com.bks.mvisample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bks.mvisample.api.RetrofitBuilder
import com.bks.mvisample.ui.main.state.MainViewState
import com.bks.mvisample.util.ApiEmptyResponse
import com.bks.mvisample.util.ApiErrorResponse
import com.bks.mvisample.util.ApiSuccessResponse

/**
 *
 * because we only have one Repository in this project we call it like this, and if we have
 * more than one repository ( more than one viewModel) we can prepend the correspondence package structure
 * like main (MainRepository)
 */

object Repository {

    fun getBlogPosts() : LiveData<MainViewState> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) {apiResponse ->
                object : LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()
                        when(apiResponse) {
                            is ApiSuccessResponse -> {
                                value = MainViewState(
                                   blogPosts = apiResponse.body
                                )
                            }
                            is ApiErrorResponse -> {
                                value = MainViewState() // handle error ?
                            }
                            is ApiEmptyResponse -> {
                                value = MainViewState() // handle empty / error ?
                            }
                        }
                    }
                }
            }
    }

    fun getUser(userId : String) : LiveData<MainViewState> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getUser(userId)) {apiResponse ->
                object : LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()
                        when(apiResponse) {
                            is ApiSuccessResponse -> {
                                value = MainViewState(
                                    user = apiResponse.body
                                )
                            }
                            is ApiErrorResponse -> {
                                value = MainViewState() // handle error ?
                            }
                            is ApiEmptyResponse -> {
                                value = MainViewState() // handle empty / error ?
                            }
                        }
                    }
                }
            }
    }
}