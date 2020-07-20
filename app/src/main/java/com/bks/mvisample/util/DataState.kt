package com.bks.mvisample.util

/**
 * it's called Resource class in Google Architecture Guideline, but for the purposes of MVI Arch,
 * DataState name fit much better
 *
 * this class acts like Resource (as recommended app arch) class in My Recipe project,
 * it demonstrate how to expose network status using a Resource class that encapsulate both the data and its state.
 *
 */
data class DataState<T>(
    var message : String? = null,
    var loading : Boolean = false,
    var data : T? = null
)
{
    companion object {

        fun <T> error(message : String) : DataState<T> {
            return DataState(
                message = message,
                loading = false,
                data = null
            )
        }

        fun <T> loading(isLoading : Boolean): DataState<T> {
            return DataState(
                message = null,
                loading = isLoading,
                data = null
            )
        }

        fun <T> data(
            message : String? = null,
            data : T? = null
        ): DataState<T> {
            return DataState(
                message = message,
                loading = false,
                data = data
            )
        }
    }

    override fun toString(): String {
        return "DataState(message=$message, loading=$loading, data=$data)"
    }


}