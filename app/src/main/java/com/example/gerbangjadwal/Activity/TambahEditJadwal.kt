package com.example.gerbangjadwal.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gerbangjadwal.APIClient.APIClient
import com.example.gerbangjadwal.APIClient.APIInterface
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.databinding.ActivityTambahEditJadwalBinding
import com.example.gerbangjadwal.model.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class TambahEditJadwal : AppCompatActivity() {

    private lateinit var binding: ActivityTambahEditJadwalBinding
    private lateinit var apiClient: APIClient
    private lateinit var apiInterface: APIInterface
    private lateinit var sdf:SimpleDateFormat

    private var jamDari=""
    private  var jamSampai=""

    private var detikDari=""
    private var detikSampai=""

    private var waktuDari=""
    private var waktuSampai=""

    private var durasi=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTambahEditJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        window.statusBarColor=resources.getColor(R.color.green_main)

        sdf= SimpleDateFormat("HH:mm:ss")

        binding.timepickerMulai.setIs24HourView(true)
        binding.timepickerSampai.setIs24HourView(true)


        binding.numberpickerMulai.minValue=0
        binding.numberpickerMulai.maxValue=59
        binding.numberpickerMulai.value=0

        binding.numberpickerSampai.minValue=0
        binding.numberpickerSampai.maxValue=59
        binding.numberpickerSampai.value=0

        binding.timepickerMulai.setOnTimeChangedListener { _, hourOfDay, minute ->
            var jam=""
            var min=""

            jam = if(hourOfDay< 10) {
                "0$hourOfDay"
            } else {
                "$hourOfDay"
            }

            min = if(minute < 10) {
                "0$minute"
            } else {
                "$minute"
            }


            jamDari="$jam:$min"

            Toast.makeText(this@TambahEditJadwal, jamDari, Toast.LENGTH_SHORT).show()
        }

        binding.timepickerSampai.setOnTimeChangedListener{_, hourOfDay, minute->

            var jam=""
            var min=""

            jam = if(hourOfDay< 10) {
                "0$hourOfDay"
            } else {
                "$hourOfDay"
            }

            min = if(minute < 10) {
                "0$minute"
            } else {
                "$minute"
            }

            jamSampai="$jam:$min"

            Toast.makeText(this@TambahEditJadwal, jamSampai, Toast.LENGTH_SHORT).show()

        }

        binding.numberpickerMulai.setOnValueChangedListener {picker, oldVal, newVal ->
            detikDari = if (newVal < 10) {
                "0$newVal"
            } else {
                newVal.toString()
            }

        }


        binding.numberpickerSampai.setOnValueChangedListener{picker, oldVal, newVal->
            detikSampai=if(newVal<10){
                "0$newVal"
            }else{
                newVal.toString()
            }
        }

        binding.btntambahjadwal.setOnClickListener {

            waktuDari="$jamDari:$detikDari"
            waktuSampai="$jamSampai:$detikSampai"
            durasi=convertToMicroSecond(waktuDari,waktuSampai)

            if(!validasiWaktu(waktuDari,waktuSampai))
            {
                Toast.makeText(this@TambahEditJadwal, "Waktu Mulai Tidak Boleh Lebih Dari Waktu Selesai", Toast.LENGTH_SHORT).show()
            }

            else if((jamDari=="" || detikDari=="") || (jamSampai=="" || detikSampai==""))
            {
                Toast.makeText(this@TambahEditJadwal, "Silahkan masukkan jam,detik buka tutup gerbang", Toast.LENGTH_SHORT).show()
            }

            else
            {
                tambahJadwal(waktuDari, waktuSampai,durasi)
            }
        }

    }

    private fun tambahJadwal(terbuka:String, tertutup:String, durasi: String){
        apiInterface=APIClient().getApiClient()!!.create(APIInterface::class.java)

        val call = apiInterface.tambahJadwal(terbuka,tertutup,durasi)

        call.enqueue(object :Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()?.success==1){
                    Toast.makeText(this@TambahEditJadwal, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@TambahEditJadwal, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.d("Error tambah jadwal:",t.message.toString())
            }

        })

    }


    private fun convertToMicroSecond(start:String, end:String):String{
        val waktuAwal =sdf.parse(start)
        val waktuAkhir =sdf.parse(end)

        val perbedaanDetik = (waktuAkhir.time - waktuAwal.time) / 1000

        val selisihMicroseconds=perbedaanDetik *  1000

        return selisihMicroseconds.toString()
    }

    private fun validasiWaktu(start:String, end:String):Boolean{
        var valid=false

        val jamDari = sdf.parse(start)
        val jamSampai = sdf.parse(end)

        if (jamDari <= jamSampai) {
            valid=true
        }

        return valid
    }
}