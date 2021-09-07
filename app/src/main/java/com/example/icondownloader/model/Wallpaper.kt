package com.example.icondownloader.model

import com.google.gson.annotations.SerializedName

class Wallpaper {
    @SerializedName("src")
    private var src: ImagesDimensions? = null

    constructor(src: ImagesDimensions?){
        this.src = src
    }
//    fun Wallpaper() {
//
//    }

    fun getSrc(): ImagesDimensions? {
        return src
    }

    fun setSrc(src: ImagesDimensions?) {
        this.src = src
    }


    class ImagesDimensions(
        @field:SerializedName("medium") var medium: String, @field:SerializedName("large")
        var large: String
    )
}