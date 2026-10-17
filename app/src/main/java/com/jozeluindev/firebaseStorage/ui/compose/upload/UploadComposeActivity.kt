package com.jozeluindev.firebaseStorage.ui.compose.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jozeluindev.firebaseStorage.R
import com.jozeluindev.firebaseStorage.databinding.ActivityUploadComposeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint

class UploadComposeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadComposeBinding


    companion object {
        fun create(context: Context): Intent = Intent(context, UploadComposeActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadComposeBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.composeView.setContent {//Todo lo que haya aqui podra ser compose

            UploadScreen()
        }


    }

    @Composable
    fun UploadScreen() {
        val uploadViewModel: UploadComposeViewModel by viewModels()


        var uri: Uri? by remember {mutableStateOf(null)}

        val intentCameraLauncher = rememberLauncherForActivityResult(TakePicture()) {
            if (it && uri?.path?.isNotEmpty() == true) //
            {
                uploadViewModel.uploadBasicImage(uri!!)
            }

        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            FloatingActionButton(onClick = {
                            uri=generateUri()
                            intentCameraLauncher.launch(uri)
            }, backgroundColor = colorResource(id = R.color.green) ) {
                Icon(painter = painterResource(id = R.drawable.ic_camera), contentDescription = null, tint = Color.White)
            }
        }





    }

    private fun generateUri(): Uri {
        return  FileProvider.getUriForFile(
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