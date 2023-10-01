package com.example.gerbangjadwal.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gerbangjadwal.PreferenceManager.PreferenceManager
import com.example.gerbangjadwal.R
import com.example.gerbangjadwal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        window.statusBarColor=resources.getColor(R.color.green_main)

        preferenceManager=PreferenceManager()
        preferenceManager.preferenceManager(this@MainActivity)

        binding.ivlogout.setOnClickListener {
            preferenceManager.clear()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            Toast.makeText(this@MainActivity, "Log Out", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.cvkendalimanual.setOnClickListener {
            startActivity(Intent(this@MainActivity,KendaliManualActivity::class.java))
        }

        binding.cvjadwal.setOnClickListener {
            startActivity(Intent(this@MainActivity,MenuJadwal::class.java))
        }

        binding.cvnotifikasi.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotifikasiActivity::class.java))
        }
    }
}