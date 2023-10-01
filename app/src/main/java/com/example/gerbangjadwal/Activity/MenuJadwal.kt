package com.example.gerbangjadwal.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerbangjadwal.APIClient.APIClient
import com.example.gerbangjadwal.APIClient.APIInterface
import com.example.gerbangjadwal.Adapter.ItemOnClick
import com.example.gerbangjadwal.Helper.Constant
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.Adapter.JadwalAdapter
import com.example.gerbangjadwal.databinding.ActivityMenuJadwalBinding
import com.example.gerbangjadwal.model.APIResponse
import com.example.gerbangjadwal.model.Jadwal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuJadwal : AppCompatActivity(), ItemOnClick {
    private lateinit var binding:ActivityMenuJadwalBinding
    private lateinit var intent:Intent
    private lateinit var jadwalAdapter:JadwalAdapter
    private lateinit var  layoutManager: RecyclerView.LayoutManager
    private lateinit var jadwal:Jadwal
    private lateinit var apiInterface: APIInterface

    private var jadwalArrayList=ArrayList<Jadwal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMenuJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiInterface= APIClient().getApiClient()!!.create(APIInterface::class.java)

        supportActionBar?.hide()
        window.statusBarColor=resources.getColor(R.color.green_main)

        getDataJadwal()

        binding.btnaddjadwal.setOnClickListener {
            intent=Intent(this@MenuJadwal,TambahEditJadwal::class.java)
            intent.putExtra(Constant().TAMBAH, Constant().TAMBAH)
            startActivity(intent)
        }


    }


    private fun getDataJadwal(){

        val call = apiInterface.getJadwal()

        call.enqueue(object : Callback<List<Jadwal>> {
            override fun onResponse(call: Call<List<Jadwal>>, response: Response<List<Jadwal>>) {
                if (response.isSuccessful)
                {
                    val jadwalList: List<Jadwal>? = response.body()
                    // Process the jadwalList as needed
                    if (jadwalList != null)
                    {
                        for (jadwalItem in jadwalList)
                        {
                            jadwal=Jadwal(
                                durasi=jadwalItem.durasi,
                                id=jadwalItem.id,
                                waktu_terbuka = jadwalItem.waktu_terbuka,
                                waktu_tertutup = jadwalItem.waktu_tertutup
                            )

                            jadwalArrayList.add(jadwal)
                        }

                        jadwalAdapter= JadwalAdapter(jadwalArrayList, this@MenuJadwal)
                        layoutManager=LinearLayoutManager(this@MenuJadwal)
                        binding.rvjadwal.layoutManager=layoutManager
                        binding.rvjadwal.adapter=jadwalAdapter

                    }
                }

                else
                {
                    Toast.makeText(this@MenuJadwal, "Menu Jadwal Masih Kosong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Jadwal>>, t: Throwable) {
                Log.d("Menu Jadwal:",t.message.toString())
            }
        })


    }

    private fun hapusJadwal(id:String){
        val call = apiInterface.hapusJadwal(id)
        call.enqueue(object:Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()?.success==1)
                {
                    Toast.makeText(this@MenuJadwal, response.body()?.message, Toast.LENGTH_SHORT).show()
                }

                else{
                    Toast.makeText(this@MenuJadwal, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(this@MenuJadwal, "", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onUpdate(view: View, jadwal: Jadwal) {
        val intent=Intent(this@MenuJadwal, TambahEditJadwal::class.java)
            .putExtra("dataJadwal", jadwal)
        startActivity(intent)
        Toast.makeText(this@MenuJadwal, jadwal.waktu_terbuka, Toast.LENGTH_SHORT).show()
    }

    override fun onDelete(view: View, jadwal: Jadwal) {
        val position = jadwalArrayList.indexOf(jadwal)

        if (position != -1) {
            // Remove the jadwal object from the list
            jadwalArrayList.removeAt(position)

            // Notify the adapter of the dataset change
            jadwalAdapter.notifyDataSetChanged()
        }

        // Call your deletion function, e.g., hapusJadwal(jadwal.id)
        hapusJadwal(jadwal.id)
    }



}