package com.bks.mvisample.util

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Converting Call to LiveData
 * use of Generic Type 'R' here which going to define the response from Retrofit, because
 * we have multi different type of response : RecipeResponse, RecipeSearchResponse, etc... and we could
 * have have even more responses
 * we're wrapping the GenericApiResponse around our Retrofit response object which is R and we're wrapping that with LiveData
 */

class LiveDataCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<GenericApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): LiveData<GenericApiResponse<R>> {
        return object : LiveData<GenericApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            postValue(GenericApiResponse.create(response))
                        }

                        override fun onFailure(call: Call<R>, throwable: Throwable) {
                            postValue(GenericApiResponse.create(throwable))
                        }
                    })
                }
            }
        }
    }
}