package com.sunil.mockwebservicedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sunil.mockwebservicedemo.model.api.RetrofitBuilder
import com.sunil.mockwebservicedemo.model.Post
import com.sunil.mockwebservicedemo.model.api.ApiHelperImpl
import com.sunil.mockwebservicedemo.util.Resource
import com.sunil.mockwebservicedemo.util.SingleLiveEvent
import com.sunil.mockwebservicedemo.util.Status
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PostViewModel : ViewModel() {
    private val liveData = SingleLiveEvent<Resource<List<Post>>?>()
    fun fetchPostDetails() {
        val apiHelper = ApiHelperImpl(RetrofitBuilder.apiInterface)
        apiHelper.getPostData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Post>> {
                override fun onSubscribe(d: Disposable) {
                    liveData.postValue(Resource(Status.LOADING))
                }

                override fun onSuccess(t: List<Post>) {
                    liveData.postValue(Resource(Status.SUCCESS, t))
                }

                override fun onError(e: Throwable) {
                    liveData.postValue(Resource(Status.ERROR, throwable = e))
                }
            })
    }

    fun getPostDetail(): LiveData<Resource<List<Post>>?> {
        return liveData
    }
}