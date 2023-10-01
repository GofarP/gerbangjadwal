package com.example.gerbangjadwal.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gerbangjadwal.model.Jadwal
import com.example.gerbangjadwal.Adapter.JadwalAdapter
import com.example.gerbangjadwal.databinding.LayoutJadwalBinding

class JadwalAdapter(val jadwalArrayList:ArrayList<Jadwal>, val itemOnClick: ItemOnClick)
    :RecyclerView.Adapter<JadwalAdapter.ViewHolderJadwal>()
{

    class ViewHolderJadwal(layoutJadwalBinding: LayoutJadwalBinding)
        :RecyclerView.ViewHolder(layoutJadwalBinding.root){
            private val binding=layoutJadwalBinding

            fun bind(jadwal:Jadwal, itemOnClick: ItemOnClick){
                itemView.apply {
                    binding.lbldurasi.text="Durasi ${jadwal.durasi}"
                    binding.lbljadwal.text="Jadwal: ${jadwal.waktu_terbuka} - ${jadwal.waktu_tertutup}"

                    binding.btnedit.setOnClickListener { view->
                        itemOnClick.onUpdate(view, jadwal)
                    }

                    binding.btnhapus.setOnClickListener {view->
                        itemOnClick.onDelete(view, jadwal)
                    }
                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JadwalAdapter.ViewHolderJadwal {
        val binding=LayoutJadwalBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolderJadwal(binding)
    }

    override fun onBindViewHolder(holder: JadwalAdapter.ViewHolderJadwal, position: Int) {
        holder.bind(jadwalArrayList[position],itemOnClick)
    }

    override fun getItemCount(): Int {
        return jadwalArrayList.size
    }

}
    interface  ItemOnClick {
        fun onUpdate(view: View, jadwal: Jadwal)
        fun onDelete(view:View,jadwal: Jadwal)
    }

