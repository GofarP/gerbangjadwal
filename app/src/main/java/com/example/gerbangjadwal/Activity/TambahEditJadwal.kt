package com.example.gerbangjadwal.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gerbangjadwal.APIClient.APIClient
import com.example.gerbangjadwal.APIClient.APIInterface
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.databinding.ActivityTambahEditJadwalBinding
import com.example.gerbangjadwal.model.APIResponse
import com.example.gerbangjadwal.model.Jadwal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


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

        val intent=intent

        if(intent.hasExtra("dataJadwal"))
        {
            val jadwal = intent.getParcelableExtra<Jadwal>("dataJadwal")
            Toast.makeText(this@TambahEditJadwal, jadwal?.id, Toast.LENGTH_SHORT).show()
        }

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

            jamDari="$jam:$min:"

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

            waktuDari="$jamDari$detikDari"
            waktuSampai="$jamSampai:$detikSampai"
            durasi= calculateDurationInMillis(waktuDari,waktuSampai).toString()

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
                    startActivity(Intent(this@TambahEditJadwal,MenuJadwal::class.java))
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

    private fun updateJadwal(terbuka:String, tertutup:String, durasi: String, id:String)
    {
        apiInterface=APIClient().getApiClient()!!.create(APIInterface::class.java)
        val call = apiInterface.updateJadwal(terbuka,tertutup,durasi,id)

        call.enqueue(object :Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()?.success==1){
                    Toast.makeText(this@TambahEditJadwal, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@TambahEditJadwal,MenuJadwal::class.java))
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


    fun calculateDurationInMillis(start: String, end: String): Long {
        val sdf = SimpleDateFormat("HH:mm:ss")

        return try {
            val dateA = sdf.parse(start)
            val dateB = sdf.parse(end)

            val durationInMillis = dateB.time - dateA.time

            durationInMillis
        } catch (e: Exception) {
            // Handle parsing exception
            e.printStackTrace()
            -1 // Return a negative value to indicate an error
        }
    }

    private fun validasiWaktu(start:String, end:String):Boolean{
        var valid=false


        if(jamDari.toString()=="" || jamSampai.toString()=="")
        {
            Toast.makeText(this@TambahEditJadwal, "Silahkan Masukkan Jam buka dan tutup gerbang secara valid", Toast.LENGTH_SHORT).show()
            return true
        }

        else{
            val jamDari = sdf.parse(start)
            val jamSampai = sdf.parse(end)

            if (jamDari <= jamSampai) {
                valid=true
            }
        }



        return valid
    }
}