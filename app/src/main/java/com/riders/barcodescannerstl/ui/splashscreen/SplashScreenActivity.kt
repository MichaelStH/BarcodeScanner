package com.riders.barcodescannerstl.ui.splashscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.barcodescannerstl.core.compose.theme.BarcodeScannerSTLTheme
import com.riders.barcodescannerstl.core.utils.CompatibilityManager
import com.riders.barcodescannerstl.ui.main.MainActivity
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {

    private val permissionRequestLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            Timber.d("registerForActivityResult(ActivityResultContracts.RequestPermission()) | $granted")
        }

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        super.onCreate(savedInstanceState)

        if (CompatibilityManager.isTiramisu()) {
            requestPermissionForAndroid13()
        }

        requestCameraPermission()

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    BarcodeScannerSTLTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SplashScreenContent()
                        }
                    }
                }
            }
        }
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    @SuppressLint("InlinedApi")
    fun requestPermissionForAndroid13() {
        Timber.d("requestPermissionForAndroid13()")

        if (ContextCompat.checkSelfPermission(
                this@SplashScreenActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("All permissions are granted. Continue workflow")

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val packageName = this@SplashScreenActivity.packageName
                val uri: Uri = Uri.fromParts(packageName, packageName, null)
                data = uri
            }.run {
                startActivity(this)
            }
        } else {
            Timber.e("Launch permissionRequestLauncher")
            launchPermissionRequest(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.e("CAMERA Permission NOT granted")
            launchPermissionRequest(Manifest.permission.CAMERA)
        }
    }

    private fun launchPermissionRequest(permission: String) {
        Timber.e("requestPermission() | permission: $permission")
        permissionRequestLauncher.launch(permission)
    }

    fun goToMainActivity() = Intent(this, MainActivity::class.java).run {
        startActivity(this)
        finish()
    }
}