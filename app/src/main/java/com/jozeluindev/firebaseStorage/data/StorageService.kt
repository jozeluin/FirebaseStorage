package com.jozeluindev.firebaseStorage.data

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
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

        readMetadataAdvance()
        return Uri.EMPTY
//        return suspendCancellableCoroutine<Uri> { cancellableContinuation ->
//            val reference = storage.reference.child("download/${uri.lastPathSegment}")
//            reference.putFile(uri,createMetadata()).addOnSuccessListener {
//                downloadImage(it, cancellableContinuation)
//            }.addOnFailureListener {
//                cancellableContinuation.resumeWithException(it)
//            }
//        }


    }

    private suspend fun readMetadata(){
        val reference=storage.reference.child("download/metadataExample2349835371437161247.jpg")
        val response = reference.metadata.await()
       val metaInfo= response.getCustomMetadata("jozeluin")
        Log.i("jozeluin MetaInfo",metaInfo.orEmpty())
    }

    private suspend fun readMetadataAdvance(){
        val reference=storage.reference.child("download/metadataExample2349835371437161247.jpg")
        val response = reference.metadata.await()

        response.customMetadataKeys.forEach {key->
           response.getCustomMetadata(key)?.let{value->
               Log.i("jozeluin MetaInfo", "Para la key:$key el valor es: $value")

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


    private fun createMetadata(): StorageMetadata{
        val metadata= storageMetadata {
            contentType="image/jpeg"
            setCustomMetadata("user","jozeluin")
            setCustomMetadata("jozeluin","Eterno aprendiz informatico")
        }
        return metadata
        }


}