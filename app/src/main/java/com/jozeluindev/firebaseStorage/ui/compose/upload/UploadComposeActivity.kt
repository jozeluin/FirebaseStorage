package com.jozeluindev.firebaseStorage.ui.compose.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import com.jozeluindev.firebaseStorage.R
import com.jozeluindev.firebaseStorage.databinding.ActivityUploadComposeBinding
import com.jozeluindev.firebaseStorage.ui.compose.list.ListComposeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun UploadScreen() {
        val uploadViewModel: UploadComposeViewModel by viewModels()


        var uri: Uri? by remember { mutableStateOf(null) }
        var showImageDialog: Boolean by remember { mutableStateOf(false) }
        var resultUri: Uri? by remember { mutableStateOf(null) }
        val loading by uploadViewModel.isLoading.collectAsState()
        var userTitle: String by remember { mutableStateOf("") }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current


        val intentCameraLauncher = rememberLauncherForActivityResult(TakePicture()) {
            if (it && uri?.path?.isNotEmpty() == true) //
            {
                uploadViewModel.uploadAndGetImag(uri!!) { newUri ->
                    userTitle = ""
                    focusManager.clearFocus()
                    resultUri = newUri
                }
            }

        }

        val intentGalleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
                if (it?.path?.isNotEmpty() == true) //
                {
                    uploadViewModel.uploadAndGetImag(it) { newUri -> resultUri = newUri }
                }
            }

        if (showImageDialog) {
            Dialog(onDismissRequest = { showImageDialog = false }) {
                Card(shape = RoundedCornerShape(12), elevation = 12.dp) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedButton(
                            onClick = {
                                uri = generateUri(userTitle)
                                intentCameraLauncher.launch(uri)
                                showImageDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(CenterHorizontally),
                            border = BorderStroke(2.dp, colorResource(id = R.color.green)),
                            shape = RoundedCornerShape(42)
                        ) {
                            Text(text = "Camera", color = colorResource(id = R.color.green))


                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                intentGalleryLauncher.launch("image/*")
                                showImageDialog = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(CenterHorizontally),
                            border = BorderStroke(2.dp, colorResource(id = R.color.green)),
                            shape = RoundedCornerShape(42)
                        ) {
                            Text(text = "From Gallery", color = colorResource(id = R.color.green))


                        }

                    }

                }

            }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(36.dp))
            Card(
                elevation = 12.dp,
                shape = RoundedCornerShape(12),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 36.dp)
            ) {

                if (resultUri != null) {
                    AsyncImage(
                        model = resultUri,
                        contentDescription = "image selected by user",
                        contentScale = ContentScale.Crop
                    )

                }
                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = colorResource(id = R.color.green)
                        )
                    }
                }

                if (!loading && resultUri == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_upload_image),
                            contentDescription = null,
                            tint = colorResource(id = R.color.green),
                            modifier = Modifier.size(100.dp)

                        )

                    }

                }


            }
            Spacer(modifier = Modifier.weight(1f))
            TextField(
                value = userTitle,
                onValueChange = { userTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .padding(horizontal = 36.dp)
                    .border(2.dp, colorResource(id = R.color.green), RoundedCornerShape(22)),

                colors = textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    ),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                FloatingActionButton(onClick = {
//                uri = generateUri()
//                intentCameraLauncher.launch(uri)
                    showImageDialog = true
                }, backgroundColor = colorResource(id = R.color.green)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            val context=LocalContext.current
            OutlinedButton(
                onClick = {startActivity(ListComposeActivity.create(context))},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
                    .align(CenterHorizontally),
                border = BorderStroke(2.dp, colorResource(id = R.color.green)),
                shape = RoundedCornerShape(42)
            ) {
                
                Text("Navigate to List", color = colorResource(id = R.color.green))
            }

        }


    }

    private fun generateUri(userTitle: String): Uri {
        return FileProvider.getUriForFile(
            java.util.Objects.requireNonNull(this),
            "com.jozeluindev.firebaseStorage.provider",
            createFile(userTitle)

        )
    }

    private fun createFile(userTitle: String): File {
        val name: String =
            userTitle.ifEmpty { SimpleDateFormat("yyyyMMdd_hhmmss").format(Date()) + "image" }
        return File.createTempFile(name, ".jpg", externalCacheDir)
    }


}