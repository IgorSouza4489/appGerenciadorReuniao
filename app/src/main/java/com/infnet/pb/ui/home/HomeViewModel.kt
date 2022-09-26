package com.infnet.pb.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infnet.pb.dao.authDao

import com.infnet.pb.dao.reuniaoDao
import com.infnet.pb.model.reuniao

class HomeViewModel : ViewModel() {


    var reunioes = MutableLiveData<List<reuniao>>()

    var msg = MutableLiveData<String>()


    init {
        reuniaoDao.listar()
            .addSnapshotListener{snapshot, error ->
                if (error != null){
                    msg.value = error.message
                }

                if (snapshot != null){
                    reunioes.value = snapshot.toObjects(reuniao::class.java)
                }
            }


    }


    fun todasReunioes(){
        reuniaoDao.listar()
            .addSnapshotListener{snapshot, error ->
                if (error != null){
                    msg.value = error.message
                }

                if (snapshot != null){
                    reunioes.value = snapshot.toObjects(reuniao::class.java)
                }
            }
    }

    fun minhasReunioes(){
        reuniaoDao.minhasReunioes(authDao.getCurrentUser()!!.uid)
            .addSnapshotListener{snapshot, error ->
                if (error != null){
                    msg.value = error.message
                }
                if (snapshot != null){
                    reunioes.value = snapshot.toObjects(reuniao::class.java)
                }
                //monitorar flag
                if (snapshot!!.isEmpty){
                    Log.d("TASD","ASF")
                }
            }
    }



    }
