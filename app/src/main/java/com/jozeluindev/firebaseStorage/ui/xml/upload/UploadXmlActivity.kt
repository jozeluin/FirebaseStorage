package com.jozeluindev.firebaseStorage.ui.xml.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.jozeluindev.firebaseStorage.R
import com.jozeluindev.firebaseStorage.databinding.ActivityUploadXmlBinding
import com.jozeluindev.firebaseStorage.databinding.DialogImageSelectorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class UploadXmlActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent = Intent(context, UploadXmlActivity::class.java)
    }

    private lateinit var binding: ActivityUploadXmlBinding
    private val uploadViewModel: UploadXMlViewModel by viewModels()

    private lateinit var uri: Uri
    private var intentCameraLauncher = registerForActivityResult(TakePicture()) {
        if (it && uri.path?.isNotEmpty() == true) //comprobar la uri
        {
            uploadViewModel.uploadAndGetImag(uri) { downloadUri ->
                clearText()
                showNewImage(downloadUri)
            }
        }


    }

    private var intentGalleryLauncher = registerForActivityResult(GetContent()) { uri ->
        uri?.let {
            uploadViewModel.uploadAndGetImag(it) { downloadUri ->
                showNewImage(downloadUri)
            }
        }

    }

    private fun showNewImage(downloadUri: Uri) {
        Glide.with(this).load(downloadUri).into(binding.ivImage)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadXmlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initUI() {
        initListeners()
        initUIState()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uploadViewModel.islLoading.collect {
                    binding.pbImage.isVisible = it
                    if (it) {
                        binding.ivPlaceHolder.isVisible = false
                        binding.ivImage.setImageDrawable(null)
                    }

                }
            }
        }
    }

    private fun initListeners() {
        binding.fabImage.setOnClickListener { showImageDialog() }
    }

    fun showImageDialog() {
        val dialogBinding = DialogImageSelectorBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).apply {

            setView(dialogBinding.root)

        }.create()
        dialogBinding.btnTakePhoto.setOnClickListener {
            takePhoto()
            dialog.dismiss()
        }
        dialogBinding.btnGallery.setOnClickListener {
            getImageFromGallery()
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

    fun getImageFromGallery() {
        intentGalleryLauncher.launch("image/*")
    }

    private fun takePhoto() {
        generateUri()
        intentCameraLauncher.launch(uri)
    }

    private fun generateUri() {
        uri = FileProvider.getUriForFile(
            java.util.Objects.requireNonNull(this),
            "com.jozeluindev.firebaseStorage.provider",
            createFile()

        )
    }

    private fun clearText(){
        binding.etTitle.setText("")
        binding.etTitle.text.clear()
    }

    private fun createFile(): File {
        val userTitle=binding.etTitle.text.toString()

        val name: String = userTitle.ifEmpty{ SimpleDateFormat("yyyyMMdd_hhmmss").format(Date()) + "image" }
        return File.createTempFile(name, ".jpg", externalCacheDir)
    }
}