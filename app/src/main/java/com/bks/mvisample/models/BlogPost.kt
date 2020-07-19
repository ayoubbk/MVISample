package com.bks.mvisample.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BlogPost(
    @Expose
    @SerializedName(value = "pk")
    val pk : Int? = null,

    @Expose
    @SerializedName(value = "title")
    val title : String? = null,

    @Expose
    @SerializedName(value = "body")
    val body : String? = null,

    @Expose
    @SerializedName(value = "image")
    val image : String? = null
)

{

    override fun toString(): String {
        return "BlogPost(pk=$pk, title=$title, body=$body, image=$image)"
    }
}