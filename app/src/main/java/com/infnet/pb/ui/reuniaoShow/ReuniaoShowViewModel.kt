package com.infnet.pb.ui.reuniaoShow

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.infnet.pb.dao.authDao
import com.infnet.pb.dao.reuniaoDao
import com.infnet.pb.model.participante
import com.infnet.pb.model.reuniao

class ReuniaoShowViewModel(val documentId: String) : ViewModel() {

    val reuniao = MutableLiveData<reuniao>()
    val status = MutableLiveData<Boolean>()
    val msg = MutableLiveData<String>()
    val ingressoStatus = MutableLiveData<Boolean>()
    val checkStatus = MutableLiveData<Boolean>()


    init {
        reuniaoDao.exibir(documentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null)
                    msg.value = error.message
                if (snapshot != null)
                    reuniao.value = snapshot.toObject(com.infnet.pb.model.reuniao::class.java)

            }
    }

    //excluir se o criador da reuniao for o mesmo logado
    fun excluir()  {
        if(reuniao.value!!.userUid == authDao.getCurrentUser()!!.uid){

            val task = reuniaoDao.excluir(documentId)
            task.addOnSuccessListener {
                status.value = true
            }.addOnFailureListener {
                msg.value = it.message
            }
        }
        else{
            msg.value = "Apenas o criador da reunião pode apagá-la."
        }
    }

    fun join(documentId: String, email: String){
        authDao.getCurrentUser()!!.email
    }

    fun atualizar(titulo: String, data: String, horainicio: String, horatermino: String, tipo: String){

        val userUid = authDao.getCurrentUser()!!.uid
        if(reuniao.value!!.userUid == userUid){
            reuniaoDao.test().addOnSuccessListener { documento ->
                if (documento.exists()){
                    val nome = documento.getString("nome")
                    Log.d("TAG", "Documento existe")
                    val reunioes = reuniao(titulo, data, horainicio, horatermino, userUid, nome, tipo)
                    val task = reuniaoDao.atualizar(documentId!!, reunioes)

                    task.addOnSuccessListener {
                        msg.value = "Reunião atualizada com sucesso."
                        status.value = true
                    }.addOnFailureListener {
                        msg.value = "Reunião não foi atualizada"
                    }
                }
                else{
                    Log.d("TAG", "Documento NAO existe")
                }
            }.addOnFailureListener{

            }
        }
        else
                 msg.value = "Apenas o criador da reunião pode alterar seus dados."

    }

    fun checkParticipante(){
        reuniaoDao.checkParticipante(documentId).addOnSuccessListener { documento ->
            if (documento.exists()){
                Log.d("TAG", "Documento existe")
                ingressoStatus.value = true
            }

        }
    }

    fun checkIngressar(){
        reuniaoDao.checkParticipante(documentId).addOnSuccessListener { documento ->
            if (documento.exists()){
                Log.d("TAG", "Documento existe")
                checkStatus.value = true
            }
            else{
                ingressar()
            }
        }
    }



    fun ingressar(){
        val email = authDao.getCurrentUser()!!.email
        reuniaoDao.test().addOnSuccessListener { documento ->
            if (documento.exists()){
                var nome = documento.getString("nome")
                Log.d("TAG", "Documento existe")
                val participantes = participante(email, nome)

                if (email != null && nome != null) {
                    reuniaoDao.entrarReuniao(documentId, participantes)
                    ingressoStatus.value = true
                }
            }
            else{
                Log.d("TAG", "Documento NAO existe")
            }
        }.addOnFailureListener{

        }

    }


//    fun checkIfAlreadyIn(){
//        reuniaoDao.test().addOnSuccessListener { documento ->
//        if (documento.exists()){
//            var nome = documento.getString("nome")
//            Log.d("TAG", "Documento existe")
//            val participantes = participante(email, nome)
//
//            if (email != null && nome != null) {
//                reuniaoDao.entrarReuniao(documentId, participantes)
//            }
//        }
//        else{
//            Log.d("TAG", "Documento NAO existe")
//        }
//    }



}