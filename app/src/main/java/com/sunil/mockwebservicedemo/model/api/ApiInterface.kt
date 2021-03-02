package com.sunil.mockwebservicedemo.model.api

import com.sunil.mockwebservicedemo.model.Post
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("posts")
    fun getPosts(): Single<List<Post>>
}