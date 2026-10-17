package com.jozeluindev.firebaseStorage.ui.xml.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.jozeluindev.firebaseStorage.data.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadXMlViewModel @Inject constructor(private val storageService: StorageService): ViewModel() {
    fun uploadBasicImage(uri: Uri) {
        storageService.uploadBasicImage(uri)
    }
}
