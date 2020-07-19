package com.bks.mvisample.ui.main.state

import com.bks.mvisample.models.BlogPost
import com.bks.mvisample.models.User

data class MainViewState(
    var blogPosts : List<BlogPost>? = null, // by default is null
    var user : User? = null // by default is null
)