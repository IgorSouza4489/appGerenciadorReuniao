package com.infnet.pb.ui.Participantes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class participanteViewModelFactory(val documentId: String)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ParticipantesViewModel(documentId) as T
    }
}