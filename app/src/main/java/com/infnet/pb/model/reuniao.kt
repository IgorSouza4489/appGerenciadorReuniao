package com.infnet.pb.model
import com.google.firebase.firestore.DocumentId

class reuniao(

    val titulo: String? = null,
    val data: String? = null,
    val horainicio: String? = null,
    val horatermino: String? = null,
    val userUid: String? = null,
    val nome: String? = null,
    val tipo: String? = null,
    //val listauserUid: MutableList<String>? = arrayListOf()

    @DocumentId
    val documentId: String? = null


) {

    override fun toString() = "${titulo}"
}