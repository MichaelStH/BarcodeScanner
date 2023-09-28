package com.riders.barcodescannerstl.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.riders.barcodescannerstl.core.compose.theme.BarcodeScannerSTLTheme
import com.riders.barcodescannerstl.core.compose.theme.Purple80
import com.riders.barcodescannerstl.core.compose.theme.PurpleGrey40
import com.riders.barcodescannerstl.ui.camera.CameraView
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import timber.log.Timber

///////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////
@Composable
fun NoCaptureAvailable() {
    BarcodeScannerSTLTheme {
        Text(text = "No camera available")
    }
}

@Composable
fun CameraContent(viewModel: MainActivityViewModel) {
    BarcodeScannerSTLTheme {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(this.maxHeight / 1.25f)
                    .align(Alignment.TopCenter)
                    .padding(16.dp), shape = RoundedCornerShape(35.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CameraView(
                        modifier = Modifier
                            .matchParentSize()
                            .align(Alignment.Center),
                        viewModel
                    )
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                    .background(if (!isSystemInDarkTheme()) Purple80 else PurpleGrey40),
                text = viewModel.dataFound,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = if (!isSystemInDarkTheme()) Color.Black else Color.White
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(viewModel: MainActivityViewModel) {
    BarcodeScannerSTLTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = "Barcode Scanner STL") })
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                AnimatedContent(
                    targetState = viewModel.showCamera,
                    label = "show camera content transition"
                ) { targetState ->
                    if (!targetState) {
                        NoCaptureAvailable()
                    } else {
                        CameraContent(viewModel)
                    }
                }
            }
        }
    }

    LaunchedEffect(viewModel.dataFound) {
        Timber.d("MainContent | LaunchedEffect | viewModel.dataFound")
        Timber.d("--> Update viewModel.dataFound ?")
    }
}


///////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewNoCaptureAvailable() {
    BarcodeScannerSTLTheme {
        NoCaptureAvailable()
    }
}

@DevicePreviews
@Composable
private fun PreviewCameraContent() {
    BarcodeScannerSTLTheme {
        CameraContent(MainActivityViewModel())
    }
}

@DevicePreviews
@Composable
private fun PreviewMainContent() {
    BarcodeScannerSTLTheme {
        MainContent(MainActivityViewModel().apply { updateShowCamera(true) })
    }
}