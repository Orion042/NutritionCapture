package com.example.nutritioncapture.view

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nutritioncapture.R
import com.example.nutritioncapture.utils.BitmapViewer
import com.example.nutritioncapture.utils.byteArrayToBitmap
import com.example.nutritioncapture.viewmodel.PhotoImageViewModel
import com.example.nutritioncapture.viewmodel.ViewModelOwner
import kotlin.math.log


private val TAG = "ConfirmPhotoView"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPhotoView(navController: NavController) {

    val imageBitmap = byteArrayToBitmap(ViewModelOwner().getPhotoImageViewModel().imageBitMap!!)

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Row {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "前の画面に戻る",
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .size(30.dp)
                        .clickable {
                            navController.navigateUp()
                        }
                )
                
                Text(
                    text = stringResource(id = R.string.confirmphotoview_take_photo_string),
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))

            if(imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "撮影した画像",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(height = 300.dp)
                        .width(380.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            else {
                showLog("imageBitmap == null")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        // 処理 TODO:AIの画像分析
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "AI分析ボタン",
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.White
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(id = R.string.confirmphotoview_ai_analyze_string),
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    },
                    backgroundColor = colorResource(id = R.color.cornflower_blue),
                    modifier = Modifier
                        .width(380.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(10.dp),
                )
            }

            val dishName = rememberSaveable { mutableStateOf("") }

            TextField(
                value = dishName.value,
                onValueChange = { it -> dishName.value = it },
                placeholder = { Text("料理名") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Cyan,
                    unfocusedIndicatorColor = colorResource(id = R.color.cornflower_blue),
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(start = 15.dp, top = 30.dp),
                singleLine = true
            )
        }
    }
}

private fun showLog(logMessage: String) {
    Log.d(TAG, logMessage)
}