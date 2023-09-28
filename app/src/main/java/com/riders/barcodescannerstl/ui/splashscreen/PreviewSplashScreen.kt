package com.riders.barcodescannerstl.ui.splashscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.barcodescannerstl.R
import com.riders.barcodescannerstl.core.compose.theme.BarcodeScannerSTLTheme
import com.riders.barcodescannerstl.core.utils.findActivity
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

///////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////
@DevicePreviews
@Composable
fun SplashScreenContent() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val progressBarVisibility = remember { mutableStateOf(false) }
    val expanded = remember { mutableStateOf(false) }

    BarcodeScannerSTLTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Barcode Scanner",
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.W600)
                )

                AnimatedVisibility(visible = if (LocalInspectionMode.current) true else expanded.value) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "By",
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.W300)
                        )
                        Image(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(id = R.drawable.stl_fm_logo),
                            contentDescription = "stl fm logo"
                        )
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp),
                visible = if (LocalInspectionMode.current) false else progressBarVisibility.value
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(2.dp),

                    )
            }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(250L)
            expanded.value = true
            delay(500L)
            progressBarVisibility.value = true
            delay(2500L)
            (context.findActivity() as SplashScreenActivity).goToMainActivity()
        }
    }
}
