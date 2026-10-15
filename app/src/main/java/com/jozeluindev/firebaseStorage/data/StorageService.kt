package com.jozeluindev.firebaseStorage.data

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class StorageService @Inject constructor(private val storage: FirebaseStorage) {

    fun basicExample() {
        Firebase.storage

        val reference = Firebase.storage.reference.child("ejemplo/test1.png")
        reference.name // test1.png
        reference.path // ejemplo/test1.png
        reference.parent?.path // ejemplo
        reference.bucket //gs://fir-storage-14d1d.firebasestorage.app

        val reference2 = storage.reference.child("ejemplo/ejemplo2/ejemplo3/test2.png")
        val reference3 =
            Firebase.storage.reference.child("ejemplo/").child("ejemplo2/").child("ejemplo3/")
                .child("test2.png")
    }

    fun uploadBasicImage(uri: Uri) {
        val reference = storage.reference.child(uri.lastPathSegment.orEmpty())
        reference.putFile(uri)
    }
}