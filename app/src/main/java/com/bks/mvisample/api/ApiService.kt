package com.bks.mvisample.api

import androidx.lifecycle.LiveData
import com.bks.mvisample.models.BlogPost
import com.bks.mvisample.models.User
import com.bks.mvisample.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogPosts() : LiveData<GenericApiResponse<List<BlogPost>>>

    @GET("placeholder/user/{userId}")
    fun getUser(
        @Path("userId") userId : String
    ) : LiveData<GenericApiResponse<User>>
}