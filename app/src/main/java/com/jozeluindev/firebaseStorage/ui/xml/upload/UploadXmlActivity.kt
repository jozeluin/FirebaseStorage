package com.jozeluindev.firebaseStorage.ui.xml.upload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jozeluindev.firebaseStorage.R
import com.jozeluindev.firebaseStorage.databinding.ActivityUploadXmlBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadXmlActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent = Intent(context, UploadXmlActivity::class.java)
    }

    private lateinit var binding: ActivityUploadXmlBinding
    private val uploadViewModel: UploadXMlViewModel by viewModels()
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
        TODO("Not yet implemented")
    }
}