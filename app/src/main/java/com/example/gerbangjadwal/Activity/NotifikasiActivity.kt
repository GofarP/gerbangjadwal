package com.example.gerbangjadwal.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerbangjadwal.APIClient.APIClient
import com.example.gerbangjadwal.APIClient.APIInterface
import com.example.gerbangjadwal.Adapter.NotifikasiAdapter
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.databinding.ActivityNotifikasiBinding
import com.example.gerbangjadwal.model.Notifikasi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotifikasiActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNotifikasiBinding
    private lateinit var notifikasiAdapter:NotifikasiAdapter
    private lateinit var notifikasi: Notifikasi
    private lateinit var intent: Intent
    private lateinit var  layoutManager: RecyclerView.LayoutManager
    private lateinit var apiInterface: APIInterface


    private var arrayListNotifikasi=ArrayList<Notifikasi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNotifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiInterface= APIClient().getApiClient()!!.create(APIInterface::class.java)

        supportActionBar?.hide()

        window?.statusBarColor=resources.getColor(R.color.green_main)

        showData()

    }

    private fun showData(){

        val call=apiInterface.getNotifikasi()

        call.enqueue(object: Callback<List<Notifikasi>>{
            override fun onResponse(
                call: Call<List<Notifikasi>>,
                response: Response<List<Notifikasi>>
            ) {
                if(response.isSuccessful){
                    val notifikasiList: List<Notifikasi>? = response.body()

                    if(notifikasiList!=null){
                        for(notifikasiItem in notifikasiList)
                        {
                            notifikasi=Notifikasi(

                                judul = notifikasiItem.judul,
                                isi= notifikasiItem.isi,
                                waktu = notifikasiItem.waktu
                            )
                            arrayListNotifikasi.add(notifikasi)
                        }
                        notifikasiAdapter= NotifikasiAdapter(arrayListNotifikasi)
                        layoutManager= LinearLayoutManager(this@NotifikasiActivity)
                        binding.rvnotifikasi.layoutManager=layoutManager
                        binding.rvnotifikasi.adapter=notifikasiAdapter
                    }

                    else
                    {
                        Toast.makeText(this@NotifikasiActivity, "Notifikasi Masih Kosong", Toast.LENGTH_SHORT).show()
                    }
                }

                else{
                    Toast.makeText(this@NotifikasiActivity, "Gagal Mengambil Data Notifikasi", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Notifikasi>>, t: Throwable) {
                Log.d("Notifikasi Activity:",t.message.toString())
            }
        })

    }


}