package com.riders.barcodescannerstl.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.barcodescannerstl.R
import com.riders.barcodescannerstl.core.compose.theme.BarcodeScannerSTLTheme
import com.riders.barcodescannerstl.core.compose.theme.Blue40
import com.riders.barcodescannerstl.core.compose.theme.Pink80
import com.riders.barcodescannerstl.core.compose.theme.Purple40
import com.riders.barcodescannerstl.core.compose.theme.PurpleGrey40
import com.riders.barcodescannerstl.core.utils.findActivity
import com.riders.barcodescannerstl.data.local.model.MainUiState
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
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                shape = RoundedCornerShape(35.dp)
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

            Icon(
                modifier = Modifier
                    .fillMaxSize(.35f)
                    .align(Alignment.Center)
                    .alpha(.85f),
                imageVector = Icons.Filled.CropFree,
                contentDescription = "qr code overlay"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(viewModel: MainActivityViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.mainUiState.collectAsStateWithLifecycle()

    // Register lifecycle events
    viewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

    val buttonShape = RoundedCornerShape(24.dp)

    BarcodeScannerSTLTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Barcode Scanner STL") },
                    actions = {
                        Button(
                            modifier = Modifier.border(
                                width = 2.dp,
                                brush = Brush.sweepGradient(
                                    colors =
                                    listOf(
                                        PurpleGrey40,
                                        Purple40,
                                        Pink80,
                                        Blue40
                                    ),
                                    center = Offset(x = 10f, y = 5f),
                                ),
                                shape = buttonShape
                            ),
                            onClick = { (context.findActivity() as MainActivity).finish() }
                        ) {
                            Text(text = stringResource(id = R.string.action_exit))
                        }
                    })
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                AnimatedContent(
                    targetState = viewModel.showCamera,
                    label = "main content transition"
                ) { targetState ->
                    if (!targetState) {
                        NoCaptureAvailable()
                    } else {
                        AnimatedContent(
                            targetState = uiState,
                            label = "show camera content transition"
                        ) { uiTargetState ->
                            when (uiTargetState) {
                                is MainUiState.DataSet -> {
                                    CameraContent(viewModel)
                                }

                                else -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(text = viewModel.message)
                                    }
                                }
                            }
                        }
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
    val viewModel: MainActivityViewModel = hiltViewModel()
    BarcodeScannerSTLTheme {
        CameraContent(viewModel)
    }
}

@DevicePreviews
@Composable
private fun PreviewMainContent() {
    val viewModel: MainActivityViewModel = hiltViewModel()
    BarcodeScannerSTLTheme {
        MainContent(viewModel)
    }
}