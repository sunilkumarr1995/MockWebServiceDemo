package com.sunil.mockwebservicedemo.model.api

import com.sunil.mockwebservicedemo.model.Post
import io.reactivex.Single
import retrofit2.Call

interface ApiHelper {
    fun getPostData():Single<List<Post>>
}