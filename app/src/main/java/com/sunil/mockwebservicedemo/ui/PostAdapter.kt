package com.sunil.mockwebservicedemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.sunil.mockwebservicedemo.R
import com.sunil.mockwebservicedemo.model.Post
import kotlinx.android.synthetic.main.row_post.view.*

internal class PostAdapter : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    var posts: ArrayList<Post> = ArrayList()

    fun setList(arrayList: ArrayList<Post>, clear: Boolean = false) {
        if (clear) {
            posts.clear()
        }
        this.posts = arrayList
        notifyDataSetChanged()
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_post, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = posts[position]
        val holder=holder.itemView
        holder.title.text = movie.title
        holder.genre.text = movie.body
    }
    override fun getItemCount(): Int {
        return posts.size
    }
     inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}