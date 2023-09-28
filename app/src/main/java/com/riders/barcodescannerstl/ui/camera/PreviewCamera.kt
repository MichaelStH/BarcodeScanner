package com.riders.barcodescannerstl.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.riders.barcodescannerstl.core.analyzer.BarcodeImageAnalyzer
import com.riders.barcodescannerstl.core.compose.theme.BarcodeScannerSTLTheme
import com.riders.barcodescannerstl.ui.main.MainActivityViewModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import timber.log.Timber
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider
        .getInstance(this)
        .also { future ->
            future.addListener(
                {
                    continuation.resume(future.get())
                },
                ContextCompat.getMainExecutor(this)
            )
        }
}

///////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////
@SuppressLint("OpaqueUnitKey")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraView(modifier: Modifier = Modifier, viewModel: MainActivityViewModel) {
    var barcodeBoxView: BarcodeBoxView?
    val cameraExecutor = Executors.newSingleThreadExecutor()

    BarcodeScannerSTLTheme {
        DisposableEffect(
            /*Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center, unbounded = false)
            ) {*/
            AndroidView(
//                modifier = Modifier.size(width = 250.dp, height = 250.dp),
                modifier = Modifier.then(modifier),
                factory = { context ->
                    barcodeBoxView = BarcodeBoxView(context).apply {
                        this.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                    val previewView = PreviewView(context)
                        .also {
                            it.scaleType = PreviewView.ScaleType.FILL_CENTER
                        }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener(
                        {
                            val cameraProvider: ProcessCameraProvider =
                                cameraProviderFuture.get()

                            val preview = Preview.Builder()
                                .build()
                                .also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                            val imageCapture = ImageCapture.Builder().build()

                            val imageAnalyzer = ImageAnalysis.Builder()
                                .build()
                                .also {
                                    it.setAnalyzer(
                                        cameraExecutor,
                                        BarcodeImageAnalyzer(
                                            context,
                                            barcodeBoxView!!,
                                            previewView.width.toFloat(),
                                            previewView.height.toFloat()
                                        ) { barcodeScanned ->
                                            Toast.makeText(
                                                context,
                                                "Barcode found with value: $barcodeScanned",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()

                                            Timber.d("Barcode found with value: $barcodeScanned")

                                            viewModel.updateDataFound(barcodeScanned)
                                        })
                                }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                // Unbind use cases before rebinding
                                cameraProvider.unbindAll()

                                // Bind use cases to camera
                                cameraProvider.bindToLifecycle(
                                    context as ComponentActivity,
                                    cameraSelector,
                                    preview,
                                    imageCapture,
                                    imageAnalyzer
                                )

                            } catch (exc: Exception) {
                                Timber.e("Use case binding failed", exc)
                            }
                        },
                        ContextCompat.getMainExecutor(context)
                    )
                    (context as ComponentActivity).addContentView(
                        barcodeBoxView,
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                    //previewView.addView(barcodeBoxView)
                    previewView
                }
            )
            /*}*/
        ) {
            onDispose { cameraExecutor.shutdown() }
        }
    }
}

///////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewCameraView() {
    BarcodeScannerSTLTheme {
        CameraView(Modifier, MainActivityViewModel())
    }
}