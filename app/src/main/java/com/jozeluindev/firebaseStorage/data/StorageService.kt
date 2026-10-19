package com.jozeluindev.firebaseStorage.data

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

    /**
     * Subir una imagen
     *
     * @param uri
     */
    fun uploadBasicImage(uri: Uri) {
        val reference = storage.reference.child(uri.lastPathSegment.orEmpty())
        reference.putFile(uri)
    }

    suspend fun dowloadBasicImage(): Uri {
        //   val reference=storage.reference.child("$userId/profile.png")
        val reference = storage.reference.child("ejemplo/test1.png")

        reference.downloadUrl.addOnSuccessListener { Log.i("jozeluin", "sucess") }
            .addOnFailureListener { Log.i("jozeluin", "fail") }

        return reference.downloadUrl.await()


    }

    suspend fun uploadAndDownloadImage(uri: Uri): Uri {
        return suspendCancellableCoroutine<Uri> { cancellableContinuation ->
            val reference = storage.reference.child("download/${uri.lastPathSegment}")
            reference.putFile(uri).addOnSuccessListener {
                downloadImage(it, cancellableContinuation)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }


    }

    private  fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot,
        cancellableContinuation: CancellableContinuation<Uri>
    ) {
        uploadTask.storage.downloadUrl
            .addOnSuccessListener { uri -> cancellableContinuation.resume(uri) }
            .addOnFailureListener { cancellableContinuation.resumeWithException(it) }
    }
}