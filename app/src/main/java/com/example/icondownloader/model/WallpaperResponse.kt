package com.example.icondownloader.model

import com.google.gson.annotations.SerializedName


class WallpaperResponse {
    @SerializedName("photos")
    private var photosList: MutableList<Wallpaper>? = null

    constructor(photosList: MutableList<Wallpaper>?){
        this.photosList = photosList
    }
//    fun WallpaperResponse() {
//
//    }

    fun getPhotosList(): MutableList<Wallpaper>? {
        return photosList
    }

    fun setPhotosList(photosList: MutableList<Wallpaper>?) {
        this.photosList = photosList
    }
}