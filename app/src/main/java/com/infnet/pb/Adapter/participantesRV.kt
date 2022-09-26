package com.infnet.pb.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infnet.pb.R
import com.infnet.pb.model.participante

class participantesRV(private val participanteList : ArrayList<participante>,) : RecyclerView.Adapter<participantesRV.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): participantesRV.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.participanterecyclerviewitem, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: participantesRV.ViewHolder, position: Int) {
        val participante: participante = participanteList[position]
        holder.email.text = participante.email
        //holder.nome.text =participante.nome
//        holder.titulo.text = reuniao.titulo
//        holder.data.text = reuniao.data
//        holder.hora.text = reuniao.horainicio
//
//        holder.nome.text = reuniao.nome
//        holder.tipo.text = reuniao.tipo


    }

    override fun getItemCount(): Int {
        return participanteList.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        //val nome : TextView = itemView.findViewById(R.id.idTvNome)
        val email : TextView = itemView.findViewById(R.id.idTVEmail)

    }

    fun updateList(newList: List<participante>) {

        participanteList.clear()

        participanteList.addAll(newList)



        notifyDataSetChanged()
        //https://stackoverflow.com/questions/71980733/recycler-view-doesnt-update-list-after-notifydatasetchanged
    }


}