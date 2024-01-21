package com.example.gerbangjadwal.Activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gerbangjadwal.APIClient.APIClient
import com.example.gerbangjadwal.APIClient.APIInterface
import com.example.gerbangjadwal.Helper.Constant
import com.example.gerbangjadwal.PreferenceManager.PreferenceManager
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.databinding.ActivityRegisterBinding
import com.example.gerbangjadwal.model.APIResponse
import com.example.gerbangjadwal.model.User
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var konfirmasiPassword:String
    private lateinit var user:User
    private lateinit var apiInterface: APIInterface
    private lateinit var preferencesManager:PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferencesManager=PreferenceManager()
        preferencesManager.preferenceManager(this@RegisterActivity)


        window.statusBarColor=resources.getColor(R.color.green_main)

        binding.btndaftar.setOnClickListener {
            email=binding.txtemail.text.toString()
            password=binding.txtpassword.text.toString()
            konfirmasiPassword=binding.txtkonfirmasipassword.text.toString()

            if(email=="" || password=="" || konfirmasiPassword=="")
            {
                Toast.makeText(this@RegisterActivity, "Silahkan Isi Kolom Yang Masih Kosong", Toast.LENGTH_SHORT).show()
            }

            else if(password!=konfirmasiPassword)
            {
                Toast.makeText(this@RegisterActivity, "Konfirmasi Password & Password Tidak Sama", Toast.LENGTH_SHORT).show()
            }

            else
            {
                user=User(email,password)
                register(user)
            }
        }

    }


    fun register(user:User)
    {
        PermissionX.init(this@RegisterActivity)
            .permissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE)
            .request{allGranted, grantedList, deniedList ->
                if (allGranted) {
                } else {
                    Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }

        apiInterface = APIClient().getApiClient()!!.create(APIInterface::class.java)

        val call = apiInterface.registerUser(user.email,user.password)

        call.enqueue(object: Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()?.success==1)
                {
                    Toast.makeText(this@RegisterActivity, "Sukses mendaftar akun", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    preferencesManager.putString(Constant().IS_LOGIN_PERMITTED,Constant().TRUE)
                    finish()
                }

                else
                {
                    Toast.makeText(this@RegisterActivity, "Gagal Mendaftarkan Akun: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.d("Register gagal",t.message.toString())
            }

        })

    }
}