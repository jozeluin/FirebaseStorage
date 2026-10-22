package com.jozeluindev.firebaseStorage.ui.xml.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.jozeluindev.firebaseStorage.R
import com.jozeluindev.firebaseStorage.databinding.ActivityListXmlBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListXmlActivity : AppCompatActivity() {

    //Ya se puede llamar desde  binding.btnNavigateToList.setOnClickListener { startActivity(ListXmlActivity.create(this)) }
// desde UploadXmlActivity , initListeners(
    companion object{
        fun create(context: Context): Intent = Intent(context, ListXmlActivity::class.java)
    }
    private val listXmlViewModel: ListXmlViewModel by viewModels()
    private lateinit var binding: ActivityListXmlBinding
    private lateinit var galleryAdapter: GalleryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListXmlBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        listXmlViewModel.getAllImages()

    }
    private fun initUI() {
        initUIState()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        galleryAdapter = GalleryAdapter()
        binding.rvGallery.apply {
            layoutManager = GridLayoutManager(this@ListXmlActivity, 2)
            adapter = galleryAdapter
        }

    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                listXmlViewModel.uiState.collect{uIState ->

                    galleryAdapter.updateList( uIState.images)
                    binding.pbGallery.isVisible=uIState.isloading


                }
            }
        }
    }


}