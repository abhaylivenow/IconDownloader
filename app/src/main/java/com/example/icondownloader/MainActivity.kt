package com.example.icondownloader

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView

import androidx.cardview.widget.CardView

import com.example.icondownloader.model.Wallpaper

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.example.icondownloader.api.ApiClient
import com.example.icondownloader.model.WallpaperResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.jetbrains.annotations.NotNull
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import android.graphics.drawable.Drawable

import android.graphics.drawable.BitmapDrawable

import androidx.annotation.NonNull

import com.bumptech.glide.request.target.CustomTarget

import android.R
import android.content.Context
import com.bumptech.glide.request.transition.Transition
import org.jetbrains.annotations.Nullable
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.os.Handler
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import java.lang.Exception
import com.bumptech.glide.request.target.SimpleTarget
import kotlin.random.Random


class MainActivity : AppCompatActivity(), RecyclerViewClickInterface {

    private lateinit var mImagesRecyclerView: RecyclerView
    private val API_KEY = "563492ad6f91700001000001482663a850e349329f4c2b4bc6d95e84"
    private var mPageCount = 1
    private val PER_PAGE = 20
    private var mImagesDataList: MutableList<Wallpaper>? = mutableListOf()
    private lateinit var mNestedScrollview: NestedScrollView
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.icondownloader.R.layout.activity_main)

        searchView = findViewById(com.example.icondownloader.R.id.search)

        initRecyclerView()
        mNestedScrollview = findViewById(com.example.icondownloader.R.id.nestedScrollView)
        setupPagination(true)

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getSearchedData(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    getData(mPageCount)
                } else {
                    getSearchedData(newText)
                }
                return true
            }
        })
    }

    private fun initRecyclerView() {
        mImagesRecyclerView = findViewById(com.example.icondownloader.R.id.recycler)
        mImagesRecyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(this, 2)
        mImagesRecyclerView.layoutManager = gridLayoutManager
        getData(mPageCount)
    }

    private fun setupPagination(isPaginationAllowed: Boolean) {
        if (isPaginationAllowed) {
            mNestedScrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    getData(++mPageCount)
                    Toast.makeText(
                        applicationContext,
                        mPageCount.toString() + "",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else mNestedScrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int -> })
    }

    private fun getSearchedData(query: String) {
        val wallpaperResponseCall: Call<WallpaperResponse?>? = ApiClient()
            .getInstance()!!
            .getApi()
            .getSearch(API_KEY, query)
        wallpaperResponseCall!!.enqueue(object : Callback<WallpaperResponse?> {
            override fun onResponse(
                call: Call<WallpaperResponse?>,
                response: Response<WallpaperResponse?>
            ) {
                if (response.isSuccessful) {
                    setupPagination(false)
                    if (!mImagesDataList?.isEmpty()!!) {
                        mImagesDataList?.clear()
                    }
                    mImagesDataList = if (null != response.body()) response.body()!!
                        .getPhotosList()!! else ArrayList()
                    val imageAdapter =
                        ImageAdapter(applicationContext, mImagesDataList, this@MainActivity)
                    mImagesRecyclerView.adapter = imageAdapter
                } else Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<WallpaperResponse?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getData(pageCount: Int) {
        val wallpaperResponseCall: Call<WallpaperResponse?>? = ApiClient()
            .getInstance()!!
            .getApi()
            .getWallpaper(API_KEY, pageCount, PER_PAGE)
        wallpaperResponseCall!!.enqueue(object : Callback<WallpaperResponse?> {
            override fun onResponse(
                call: Call<WallpaperResponse?>,
                response: Response<WallpaperResponse?>
            ) {
                val wallpaperResponse = response.body()
                if (response.isSuccessful && null != wallpaperResponse) {
                    wallpaperResponse.getPhotosList()?.let { mImagesDataList?.addAll(it) }
                    val imageAdapter =
                        ImageAdapter(application, mImagesDataList, this@MainActivity)
                    mImagesRecyclerView.adapter = imageAdapter
                    imageAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WallpaperResponse?>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
        Log.i("here", mImagesDataList?.get(position)?.getSrc()!!.medium)
    }

}