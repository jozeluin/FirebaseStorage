package com.jozeluindev.firebaseStorage.ui.xml.upload

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozeluindev.firebaseStorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadXMlViewModel @Inject constructor(private val storageService: StorageService): ViewModel() {

    private var _isLoading= MutableStateFlow<Boolean>(false)
    val islLoading: StateFlow<Boolean> =_isLoading


    fun uploadBasicImage(uri: Uri) {
        storageService.uploadBasicImage(uri)
    }

    fun uploadAndGetImag(uri: Uri,onSucessDownload:(Uri)->(Unit)){

        viewModelScope.launch {
            _isLoading.value=true

            try{
                val result= withContext(Dispatchers.IO) { storageService.uploadAndDownloadImage(uri) }
                onSucessDownload(result)
            }catch (e: Exception){
                Log.i("Error",e.message.orEmpty())

            }
            _isLoading.value=false


        }



    }
}
