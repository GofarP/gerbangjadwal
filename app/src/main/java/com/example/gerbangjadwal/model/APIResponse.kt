package com.example.gerbangjadwal.model

import com.google.gson.annotations.SerializedName

data class APIResponse (
    @SerializedName("success")
    val success:Int,

    @SerializedName("message")
    val message:String)