package com.example.nutritioncapture.view

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaActionSound
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nutritioncapture.R
import com.example.nutritioncapture.utils.BitmapViewer
import com.example.nutritioncapture.utils.byteArrayToBitmap
import com.example.nutritioncapture.utils.imageToByteArray
import com.example.nutritioncapture.viewmodel.PhotoImageViewModel
import com.example.nutritioncapture.viewmodel.ViewModelOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

private val TAG = "CameraView"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraView(navController: NavController) {
    val permissionList = mutableListOf(Manifest.permission.CAMERA)
    var capturedByteArray by remember { mutableStateOf<ByteArray?>(null) }


    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(permissionList)

    LaunchedEffect(multiplePermissionsState) {
        when {
            multiplePermissionsState.allPermissionsGranted -> {
                showLog("すでに許可済み")
            }
            multiplePermissionsState.shouldShowRationale -> {
                showLog("まだ許可設定していない")
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
            else -> {
                showLog("その他の権限")
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
        }
    }

    StartCamera { byteArray ->
        capturedByteArray = byteArray
    }

    if (capturedByteArray != null) {
        // BitmapViewer(bitmap = byteArrayToBitmap(capturedByteArray!!)!!) // デバッグ用確認

        showLog("byteArray: $capturedByteArray")

        ViewModelOwner().getPhotoImageViewModel().setImageByteArray(capturedByteArray)

        navController.navigate("confirmPhoto") {
            launchSingleTop = true
        }
    }
}

@Composable
fun StartCamera(onImageCaptured: (ByteArray?) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember {
        ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
    }
    val previewView = PreviewView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    DisposableEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()

        showLog(cameraProvider.toString())

        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        onDispose {
            cameraProvider.unbindAll()
        }
    }

    Scaffold(
        floatingActionButton = {
            Box(modifier = Modifier.fillMaxSize()) {

                FloatingActionButton(
                    onClick = {
                        showLog("カメラボタンタップ")
                        playShutterSound()
                        takePhotoAndProcessByteArray(imageCapture, ContextCompat.getMainExecutor(context)) { byteArray ->
                            showLog("Image byte array size: ${byteArray.size}")

                            onImageCaptured(byteArray)
                        }
                    },
                    containerColor = colorResource(id = R.color.cornflower_blue),
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        tint = Color.White,
                        contentDescription = "撮影アイコン"
                    )
                }
            }
        }
    ) { paddingValues ->
        AndroidView(
            factory = { previewView }
        )
    }
}

private fun takePhotoAndProcessByteArray(
    imageCapture: ImageCapture,
    executor: Executor,
    onImageCaptured: (ByteArray) -> Unit
) {
    imageCapture.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val byteArray = imageToByteArray(image)
                onImageCaptured(byteArray)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                showLog("Image capture failed: ${exception.message}")
            }
        }
    )
}

private fun takePhotoAndSaveToFile(
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    setCapturedMsg: (Uri) -> Unit
) {

    val imageCapture = imageCapture ?: return


    val filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS"

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.JAPAN).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                val savedUri = Uri.fromFile(photoFile)
                setCapturedMsg(savedUri)
            }
            override fun onError(e: ImageCaptureException) {
                showLog("photo save error: $e")
            }
        })
}

private fun playShutterSound() {
    val sound = MediaActionSound()
    sound.load(MediaActionSound.SHUTTER_CLICK)
    sound.play(MediaActionSound.SHUTTER_CLICK)
}

private fun showLog(logMessage: String) {
    Log.d(TAG, logMessage)
}