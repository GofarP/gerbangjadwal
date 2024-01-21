package com.example.gerbangjadwal.APIClient

import com.example.gerbangjadwal.model.APIResponse
import com.example.gerbangjadwal.model.Jadwal
import com.example.gerbangjadwal.model.Notifikasi
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {
    //untuk menghubungkan aplikasi android dengan file php yang
    //ada di laragon

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(@Field("email") email: String, @Field("password") password:String): Call<APIResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(@Field("email") email: String, @Field("password") password:String): Call<APIResponse>

    @FormUrlEncoded
    @POST("bukagerbang.php")
    fun bukaGerbang(@Field("status") status:String):Call<APIResponse>

    @FormUrlEncoded
    @POST("tutupgerbang.php")
    fun tutupGerbang(@Field("status") status:String):Call<APIResponse>


    @GET("getjadwal.php")
    fun getJadwal(): Call<List<Jadwal>>

    @GET("getnotifikasi.php")
    fun getNotifikasi(): Call<List<Notifikasi>>

    @FormUrlEncoded
    @POST("tambahjadwal.php")
    fun tambahJadwal(@Field("waktu_terbuka") waktuTerbuka:String,
                     @Field("waktu_tertutup") waktuTertutup:String,
                     @Field("durasi") durasi: String
    ):Call<APIResponse>

    @FormUrlEncoded
    @POST("tambahnotifikasi.php")
    fun tambahNotifikasi(@Field("judul") judul:String,
                         @Field("isi") isi:String,
                         @Field("waktu") waktu:String,
    ):Call<APIResponse>

    @FormUrlEncoded
    @POST("edit.php")
    fun updateJadwal(@Field("waktu_terbuka") waktuTerbuka:String,
                     @Field("waktu_tertutup") waktuTertutup:String,
                     @Field("durasi") durasi:String,
                   @Field("id") id:String
    ):Call<APIResponse>

    @FormUrlEncoded
    @POST("hapusjadwal.php")
    fun hapusJadwal(@Field("id") id:String):Call<APIResponse>






}