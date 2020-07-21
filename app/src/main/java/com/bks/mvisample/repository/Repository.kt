package com.bks.mvisample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.bks.mvisample.api.RetrofitBuilder
import com.bks.mvisample.models.BlogPost
import com.bks.mvisample.models.User
import com.bks.mvisample.ui.main.state.MainViewState
import com.bks.mvisample.util.*

/**
 *
 * because we only have one Repository in this project we call it like this, and if we have
 * more than one repository ( more than one viewModel) we can prepend the correspondence package structure
 * like main (MainRepository)
 */

object Repository {

    fun getBlogPosts() : LiveData<DataState<MainViewState>> {
//        return Transformations
//            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) {apiResponse ->
//                object : LiveData<DataState<MainViewState>>() {
//                    override fun onActive() {
//                        super.onActive()
//                        when(apiResponse) {
//                            is ApiSuccessResponse -> {
//                                value = DataState.data(
//                                    data = MainViewState(
//                                        blogPosts = apiResponse.body)
//                                )
//                            }
//                            is ApiErrorResponse -> {
//                                value = DataState.error(
//                                    message = apiResponse.errorMessage
//                                )
//                            }
//                            is ApiEmptyResponse -> {
//                                value = DataState.error(
//                                    message = "HTTP 204. Returned NOTHING!"
//                                )
//                            }
//                        }
//                    }
//                }
//            }

        return object : NetworkBoundResource<List<BlogPost>, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {

                result.value = DataState.data(
                    data = MainViewState(
                        blogPosts = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return RetrofitBuilder.apiService.getBlogPosts()
            }
        }.asLiveData()
    }

    fun getUser(userId : String) : LiveData<DataState<MainViewState>> {
//        return Transformations
//            .switchMap(RetrofitBuilder.apiService.getUser(userId)) {apiResponse ->
//                object : LiveData<DataState<MainViewState>>() {
//                    override fun onActive() {
//                        super.onActive()
//                        when(apiResponse) {
//                            is ApiSuccessResponse -> {
//                                value = DataState.data(
//                                    data = MainViewState(
//                                        user = apiResponse.body)
//                                )
//                            }
//                            is ApiErrorResponse -> {
//                                value = DataState.error(
//                                    message = apiResponse.errorMessage
//                                )
//                            }
//                            is ApiEmptyResponse -> {
//                                value = DataState.error(
//                                    message = "HTTP 204. Returned NOTHING!"
//                                )
//                            }
//                        }
//                    }
//                }
//            }

        return object : NetworkBoundResource<User, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    data = MainViewState(
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitBuilder.apiService.getUser(userId)
            }
        }.asLiveData()
    }
}