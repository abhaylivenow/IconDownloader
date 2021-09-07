package com.example.icondownloader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable

import android.widget.ProgressBar

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.load.engine.GlideException

import com.bumptech.glide.request.RequestListener

import com.bumptech.glide.Glide

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target

import com.example.icondownloader.model.Wallpaper

class ImageAdapter(
    var context: Context? = null,
    var imageList: List<Wallpaper>? = null,
    var recyclerViewClickInterface: RecyclerViewClickInterface? = null
): RecyclerView.Adapter<ImageAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(context!!)
            .load(imageList!![position].getSrc()!!.medium)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressbar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressbar.visibility = View.GONE
                    return false
                }
            })
            .into(holder.imageView)

        holder.imageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                recyclerViewClickInterface!!.onItemClick(position)

            }
        })
    }

    override fun getItemCount(): Int {
        return imageList!!.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var progressbar: ProgressBar

        init {
            imageView = itemView.findViewById(R.id.wallpaper)
            progressbar = itemView.findViewById(R.id.progressBar)
        }
    }
}