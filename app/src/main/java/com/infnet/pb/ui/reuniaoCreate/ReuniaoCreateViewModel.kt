package com.infnet.pb.ui.reuniaoCreate

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.infnet.pb.dao.authDao
import com.infnet.pb.dao.reuniaoDao
import com.infnet.pb.model.reuniao

class ReuniaoCreateViewModel : ViewModel() {

    var reuniao = MutableLiveData<reuniao>()
    var status = MutableLiveData<Boolean>()
    var msg = MutableLiveData<String>()
    var documentId: String? = null



    fun inserir(titulo: String, data: String, horainicio: String, horatermino: String, tipo: String) {
        val userUid = authDao.getCurrentUser()!!.uid
       reuniaoDao.test().addOnSuccessListener { documento ->
            if (documento.exists()){
                val nome = documento.getString("nome")
                Log.d("TAG", "Documento existe")
                val reuniao = reuniao(titulo, data, horainicio, horatermino, userUid, nome, tipo)
                val task = reuniaoDao.inserir(reuniao)

                task.addOnSuccessListener {
                    msg.value = "Reuniao criada com sucesso."
                    status.value = true

                }.addOnFailureListener {
                    msg.value = it.message
                }
            }
            else{
                Log.d("TAG", "Documento NAO existe")
            }
        }.addOnFailureListener{
            
        }
    }


}