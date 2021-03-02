package com.sunil.mockwebservicedemo.model.api

import com.sunil.mockwebservicedemo.model.Post
import io.reactivex.Single
import retrofit2.Call

class ApiHelperImpl(private val apiInterface: ApiInterface) : ApiHelper {
    override fun getPostData(): Single<List<Post>> {
        return apiInterface.getPosts()
    }
}