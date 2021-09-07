package com.example.icondownloader.api

import com.example.icondownloader.model.WallpaperResponse
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {

    @GET("curated")
    fun getWallpaper(
        @Header("Authorization") credentials: String?,
        @Query("page") pageCount: Int,
        @Query("per_page") perPage: Int
    ): Call<WallpaperResponse?>?

    @GET("search")
    fun getSearch(
        @Header("Authorization") credentials: String?,
        @Query("query") queryText: String?
    ): Call<WallpaperResponse?>?
}