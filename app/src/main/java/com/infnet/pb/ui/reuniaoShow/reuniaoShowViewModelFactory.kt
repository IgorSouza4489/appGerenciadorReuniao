package com.infnet.pb.ui.reuniaoShow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class reuniaoShowViewModelFactory(val documentId: String)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReuniaoShowViewModel(documentId) as T
    }
}