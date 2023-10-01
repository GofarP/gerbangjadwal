package com.example.gerbangjadwal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Jadwal(val id:String,
                  val waktu_terbuka:String,
                  val waktu_tertutup:String,
                  val durasi:String) : Parcelable
