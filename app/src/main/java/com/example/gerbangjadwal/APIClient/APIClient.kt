package com.example.gerbangjadwal.APIClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {


    //Berfungsi Mengakses IP dari hostingan kita
    val BASE_URL = "http://192.168.229.194/gerbangjadwal/"


    //inisialisasi objek retrofit
    private var retrofit: Retrofit? = null

    //sebuah methhod objek retrofit yang berisi perintah untuk menyambungkan aplikasi android ke url lokal ataupun WAN
    fun getApiClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}