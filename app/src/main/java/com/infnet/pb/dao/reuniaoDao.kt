package com.infnet.pb.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.infnet.pb.dao.authDao.Companion.getCurrentUser
import com.infnet.pb.dao.reuniaoDao.Companion.collection
import com.infnet.pb.model.participante
import com.infnet.pb.model.reuniao
import java.text.SimpleDateFormat
import java.util.*


class reuniaoDao {

    companion object {
        private val collection = Firebase
            .firestore.collection("reunioes")
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())


        fun test() = Firebase.firestore.collection("profile").document(getCurrentUser()!!.uid).get()




        fun listar() = collection.whereGreaterThanOrEqualTo("data", currentDate )

        fun inserir(reuniao: reuniao) = collection.add(reuniao)

        fun exibir(documentId: String) = collection.document(documentId)

        fun exibirParticipantes(documentId: String): CollectionReference {

            val ref = collection.document(documentId)
            return ref.collection("participantes")
        }


        fun excluir(documentId: String) = collection.document(documentId).delete()

        fun atualizar(documentId: String, reunioes: reuniao) = collection.document(documentId).set(reunioes)

        fun entrarReuniao(documentId: String, participantes: participante){

            val ref = collection.document(documentId)
            ref.collection("participantes").document(getCurrentUser()!!.uid).set(participantes)
        }

        fun minhasReunioes(userUid: String) = collection.whereEqualTo("userUid", userUid)


        fun sairReuniao(documentId: String): Task<Void> {

            val ref = collection.document(documentId)
           return ref.collection("participantes").document(getCurrentUser()!!.uid).delete()


        }

        fun checkParticipante(documentId: String): Task<DocumentSnapshot> {
            val ref = collection.document(documentId)
            return ref.collection("participantes").document(getCurrentUser()!!.uid).get()
        }

//        fun checkAlreadyIn(documentId: String, participantes: participante){
//            val ref = collection.document(documentId)
//            ref.collection(participantes)
//        }




        //fun exibir(documentId: String) = collection.document(documentId).get()
        //fun exibir(documentId: String) = collection.document(documentId)
        //fun excluir(documentId: String) = collection.document(documentId).delete()

        //fun atualizar(documentId: String, carro: Carro) =
        //collection.document(documentId).set(carro)

    }

}