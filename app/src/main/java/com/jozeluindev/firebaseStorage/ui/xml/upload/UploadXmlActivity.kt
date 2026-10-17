package com.jozeluindev.firebaseStorage.ui.xml.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.common.internal.Objects
import com.jozeluindev.firebaseStorage.R
import com.jozeluindev.firebaseStorage.databinding.ActivityUploadXmlBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.util.*

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
            uploadViewModel.uploadBasicImage(uri)
        }


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
    }

    private fun initListeners() {
        binding.fabImage.setOnClickListener { takePhoto() }
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

    private fun createFile(): File {
        val name: String = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date()) + "image"
        return File.createTempFile(name, ".jpg", externalCacheDir)
    }
}