package com.example.gerbangjadwal.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.gerbangjadwal.APIClient.APIClient
import com.example.gerbangjadwal.APIClient.APIInterface
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.databinding.ActivityKendaliManualBinding
import com.example.gerbangjadwal.model.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class KendaliManualActivity : AppCompatActivity() {
    private lateinit var binding:ActivityKendaliManualBinding
    private lateinit var apiClient: APIClient
    private lateinit var apiInterface: APIInterface
    private lateinit var waktuSekarang:LocalDateTime
    private lateinit var formatter:DateTimeFormatter
    private lateinit var tanggalDanWaktu:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityKendaliManualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        window.statusBarColor=resources.getColor(R.color.green_main)
        waktuSekarang = LocalDateTime.now()

        formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Format tanggal dan waktu sesuai keinginan
       tanggalDanWaktu= waktuSekarang.format(formatter)


        binding.btnbuka.setOnClickListener {
            if(binding.lblstatuspintu.text=="Gerbang Tertutup")
            {
                bukaGerbang()

                val targetTime = 5000 // 30 detik dalam milidetik
                var timeRemaining = targetTime.toLong()

                val countDownTimer=object :CountDownTimer(timeRemaining, 1000)
                {
                    override fun onTick(p0: Long) {
                        timeRemaining = p0
                        val seconds = p0 / 1000
                        binding.btnbuka.text="00:0${seconds}"
                        binding.btnbuka.isEnabled=false
                    }

                    override fun onFinish() {
                        tutupGerbang()
                        binding.btnbuka.text="Buka Gerbang"
                        binding.btnbuka.isEnabled=true
                    }
                }

                countDownTimer.start()

            }

            else if(binding.lblstatuspintu.text=="Gerbang Terbuka")
            {
                Toast.makeText(this@KendaliManualActivity, "Pintu Sedang Terbuka Mohon Menunggu", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun bukaGerbang()
    {
        apiInterface=APIClient().getApiClient()!!.create(APIInterface::class.java)
        apiInterface.bukaGerbang("buka").enqueue(object: Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                binding.lblstatuspintu.text="Gerbang Terbuka"
                Toast.makeText(this@KendaliManualActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                tambahNotifikasiTerbuka()
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(this@KendaliManualActivity, "Gagal Membuka Gerbang", Toast.LENGTH_SHORT).show()
                Log.d("gagal Buka gerbang",t.message.toString())
            }

        })
    }

    fun tutupGerbang()
    {
        apiInterface=APIClient().getApiClient()!!.create(APIInterface::class.java)
        apiInterface.tutupGerbang("tutup").enqueue(object: Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                binding.lblstatuspintu.text="Gerbang Tertutup"
                Toast.makeText(this@KendaliManualActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                tambahNotifikasiTertutup()
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(this@KendaliManualActivity, "Gagal Menutup Gerbang", Toast.LENGTH_SHORT).show()
                Log.d("gagal tutup gerbang",t.message.toString())
            }

        })
    }

    fun tambahNotifikasiTerbuka(){
        apiInterface=APIClient().getApiClient()!!.create(APIInterface::class.java)
        apiInterface.tambahNotifikasi("Gerbang Di Buka","Gerbang Dibuka Secara Manual",tanggalDanWaktu)
            .enqueue(object: Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                Toast.makeText(this@KendaliManualActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(this@KendaliManualActivity, "Gagal Mengirim Notifikasi", Toast.LENGTH_SHORT).show()
                Log.d("gagal Kirim Notifikas",t.message.toString())
            }

        })
    }

    fun tambahNotifikasiTertutup(){
        apiInterface=APIClient().getApiClient()!!.create(APIInterface::class.java)
        apiInterface.tambahNotifikasi("Gerbang Di Tutup","Gerbang Ditutup Secara Manual",tanggalDanWaktu)
            .enqueue(object: Callback<APIResponse>{
                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    Toast.makeText(this@KendaliManualActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@KendaliManualActivity, "Gagal Mengirim Notifikasi", Toast.LENGTH_SHORT).show()
                    Log.d("gagal Kirim Notifikas",t.message.toString())
                }

            })
    }
}