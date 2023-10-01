package com.example.gerbangjadwal.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerbangjadwal.databinding.LayoutNotifikasiBinding
import com.example.gerbangjadwal.model.Notifikasi

class NotifikasiAdapter(val notifikasiArrayList:ArrayList<Notifikasi>)
    :RecyclerView.Adapter<NotifikasiAdapter.ViewHolderNotifikasi>(){

        class ViewHolderNotifikasi(layoutNotifikasiBinding: LayoutNotifikasiBinding)
            :RecyclerView.ViewHolder(layoutNotifikasiBinding.root){
            private val binding=layoutNotifikasiBinding

            fun bind(notifikasi: Notifikasi){
                itemView.apply {
                    binding.lbltitlenotifikasi.text=notifikasi.judul
                    binding.lblisinotifikasi.text=notifikasi.isi
                    binding.lblwaktu.text=notifikasi.waktu
                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderNotifikasi {
        val binding= LayoutNotifikasiBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolderNotifikasi(binding)
    }


    override fun getItemCount(): Int {
        return notifikasiArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderNotifikasi, position: Int) {
        holder.bind(notifikasiArrayList[position])
    }
}