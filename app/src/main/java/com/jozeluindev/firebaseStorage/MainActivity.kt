package com.jozeluindev.firebaseStorage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jozeluindev.firebaseStorage.databinding.ActivityMainBinding
import com.jozeluindev.firebaseStorage.ui.compose.upload.UploadComposeActivity
import com.jozeluindev.firebaseStorage.ui.xml.upload.UploadXmlActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNavigateToXml.setOnClickListener {
            startActivity(UploadXmlActivity.create(this))
        }
        binding.btnNavigateCompose.setOnClickListener {
            startActivity(
                UploadComposeActivity.create(
                    this
                )
            )
        }

    }
}

//Acuerdate de cambiar el  namespace y el applicationId en el build.gradle.kts (app)