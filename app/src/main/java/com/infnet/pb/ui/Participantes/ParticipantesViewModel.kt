package com.infnet.pb.ui.Participantes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.android.gms.common.config.GservicesValue.value
import com.infnet.pb.dao.authDao
import com.infnet.pb.dao.reuniaoDao
import com.infnet.pb.model.participante
import com.infnet.pb.model.reuniao

class ParticipantesViewModel(val documentId: String) : ViewModel() {

    var participante = MutableLiveData<List<participante>>()
    val reuniao = MutableLiveData<reuniao>()
    val status = MutableLiveData<Boolean>()
    val msg = MutableLiveData<String>()
    val ingressoStatus = MutableLiveData<Boolean>()
    val checkStatus = MutableLiveData<Boolean>()


    init {
        reuniaoDao.exibirParticipantes(documentId)
            .addSnapshotListener{snapshot, error ->
                if (error != null){
                    msg.value = error.message
                }

                if (snapshot != null){
                    participante.value = snapshot.toObjects(com.infnet.pb.model.participante::class.java)
                }
            }

        reuniaoDao.exibir(documentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null)
                    msg.value = error.message
                if (snapshot != null)
                    reuniao.value = snapshot.toObject(com.infnet.pb.model.reuniao::class.java)

            }
    }

    fun sair(){
        val email = authDao.getCurrentUser()!!.email
        reuniaoDao.test().addOnSuccessListener { documento ->
            if (documento.exists()){
                var nome = documento.getString("nome")
                Log.d("TAG", "Documento existe")
                if (email != null && nome != null) {
                    reuniaoDao.sairReuniao(documentId).addOnSuccessListener {
                        msg.value = "Você saiu da reunião com sucesso"
                        status.value = true
                    }.addOnFailureListener{
                        msg.value = "Ocorreu um erro ao sair da reunião"
                    }
                }
            }
            else{
                Log.d("TAG", "Documento NAO existe")
            }
        }.addOnFailureListener{

        }

    }

    fun checkParticipante(){
        reuniaoDao.checkParticipante(documentId).addOnSuccessListener { documento ->
            if (documento.exists()){
                ingressoStatus.value = true
                Log.d("TAG", "VC ESTA PARTICIPANDO")
            }
            else
                Log.d("TAG", "VC NAO ESTA PARTICIPANDO")
                ingressoStatus.value = false
        }
    }

}