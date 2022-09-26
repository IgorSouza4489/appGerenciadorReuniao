package com.infnet.pb.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infnet.pb.R
import com.infnet.pb.model.reuniao

class reuniaoRV(private val reuniaoList : ArrayList<reuniao>, val reuniaoClickInterface: ReuniaoClickInterface,) : RecyclerView.Adapter<reuniaoRV.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): reuniaoRV.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reuniaorecyclerviewitem, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: reuniaoRV.ViewHolder, position: Int) {
        val reuniao: reuniao = reuniaoList[position]
        holder.titulo.text = reuniao.titulo
        holder.data.text = reuniao.data
        holder.hora.text = reuniao.horainicio

        //holder.nome.text = reuniao.nome
        holder.tipo.text = reuniao.tipo

        holder.itemView.setOnClickListener {

            reuniaoClickInterface.onReuniaoClick(reuniaoList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return reuniaoList.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val titulo : TextView = itemView.findViewById(R.id.idTVTitulo)
        val data : TextView = itemView.findViewById(R.id.idTVDate)
        val hora : TextView = itemView.findViewById(R.id.idTVHora)
        //val nome : TextView = itemView.findViewById(R.id.idTvNome)
        val tipo: TextView = itemView.findViewById(R.id.idTVTipo)
    }

    fun updateList(newList: List<reuniao>) {

        reuniaoList.clear()

        reuniaoList.addAll(newList)

        reuniaoList.sortedByDescending { it.titulo }

        notifyDataSetChanged()
        //https://stackoverflow.com/questions/71980733/recycler-view-doesnt-update-list-after-notifydatasetchanged
    }

    interface ReuniaoClickInterface {
        fun onReuniaoClick(reuniao: reuniao)
    }

}