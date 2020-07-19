package com.bks.mvisample.util


import android.util.Log
import retrofit2.Response

/**
 * Generic class for handling responses from Retrofit; Common class used by API responses.
 * @param <T> the type of the response object
</T>*/
@Suppress("unused") // T is used in extending classes
sealed class GenericApiResponse<T> {

    companion object {
        private const val TAG = "GenericApiResponse"

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            //return ApiErrorResponse(error.message ?: "unknown error\n check network connection")
            return ApiErrorResponse((if (error.message == "") error.message else "Unknown error\nCheck network connection")!!)
        }

        fun <T> create(response: Response<T>): GenericApiResponse<T> {

            Log.d(TAG, "GenericApiResponse: response : $response")
            Log.d(TAG, "GenericApiResponse: raw : ${response.raw()}")
            Log.d(TAG, "GenericApiResponse: headers : ${response.headers()}")
            Log.d(TAG, "GenericApiResponse: message : ${response.message()}")

            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) { // 204 is empty response
                    ApiEmptyResponse()
                } else if(response.code() == 401) {
                    ApiErrorResponse("401 Unauthorized. Token may be invalid")
                }
                else {
                    ApiSuccessResponse(body)
                }

            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }

}

/**
 * Generic success response from api
 * @param <T>
</T> */
data class ApiSuccessResponse<T>(val body :  T) : GenericApiResponse<T>()

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : GenericApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : GenericApiResponse<T>()