package com.sunil.mockwebservicedemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunil.mockwebservicedemo.R
import com.sunil.mockwebservicedemo.model.Post
import com.sunil.mockwebservicedemo.util.Status
import com.sunil.mockwebservicedemo.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var postViewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        fetchPost()
        postAdapter = PostAdapter()
        val layoutManager = LinearLayoutManager(applicationContext)
        recycleView.layoutManager = layoutManager
        recycleView.itemAnimator = DefaultItemAnimator()
        recycleView.adapter = postAdapter
    }

    private fun fetchPost() {
        postViewModel.getPostDetail().observe(this, Observer { it ->
            when(it?.status){
                Status.LOADING->{
                    progressBar.visibility= View.VISIBLE
                }
                Status.SUCCESS -> {
                    progressBar.visibility= View.GONE
                    postAdapter.setList(it.data as ArrayList<Post>)
                }
                Status.ERROR -> {
                    progressBar.visibility= View.GONE
                    Toast.makeText(this,it.throwable.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
        postViewModel.fetchPostDetails()
    }
    private fun initViewModel() {
        postViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
    }
}