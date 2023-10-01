package com.example.gerbangjadwal.Activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gerbangjadwal.APIClient.APIClient
import com.example.gerbangjadwal.APIClient.APIInterface
import com.example.gerbangjadwal.Helper.Constant
import com.example.gerbangjadwal.PreferenceManager.PreferenceManager
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.databinding.ActivityLoginBinding
import com.example.gerbangjadwal.model.APIResponse
import com.example.gerbangjadwal.model.User
import com.permissionx.guolindev.PermissionX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var apiClient: APIClient
    private lateinit var apiInterface: APIInterface
    private lateinit var user:User
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        window.statusBarColor=resources.getColor(R.color.green_main)

        preferenceManager=PreferenceManager()
        preferenceManager.preferenceManager(this@LoginActivity)

        if(preferenceManager.getString(Constant().IS_LOGIN_PERMITTED)!=null)
        {
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            Toast.makeText(this@LoginActivity, "Selamat Datang", Toast.LENGTH_SHORT).show()
        }

        binding.btnlogin.setOnClickListener {
            email=binding.txtemail.text.toString().trim()
            password=binding.txtpassword.text.toString()
            user=User(email,password)

            if(email=="" || password=="")
            {
                Toast.makeText(this@LoginActivity, "Silahkan Isi Email Atau Password Terlebiih Dahulu", Toast.LENGTH_SHORT).show()
            }

            else
            {
                login(user)
            }

        }

    }

    fun login(user:User)
    {
        PermissionX.init(this@LoginActivity)
            .permissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                } else {
                    Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }


        apiInterface = APIClient().getApiClient()!!.create(APIInterface::class.java)

        val call = apiInterface.loginUser(user.email,user.password)

        call.enqueue(object:Callback<APIResponse>{
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()?.success==1)
                {
                    Toast.makeText(this@LoginActivity, "Selamat Datang", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    preferenceManager.preferenceManager(this@LoginActivity)
                    preferenceManager.putString(Constant().IS_LOGIN_PERMITTED,Constant().TRUE)

                }

                else
                {
                    Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.d("Gagal Login",t.message.toString())
            }

        })

    }
}