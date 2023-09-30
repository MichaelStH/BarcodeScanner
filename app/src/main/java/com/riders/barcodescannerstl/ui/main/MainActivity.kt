package com.riders.barcodescannerstl.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
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
import com.riders.barcodescannerstl.ui.base.BaseComposeActivity
import com.riders.barcodescannerstl.ui.check.CheckActivity
import com.riders.barcodescannerstl.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : BaseComposeActivity() {

    private val mViewModel: MainActivityViewModel by viewModels()
    private lateinit var cameraExecutor: ExecutorService

    private var mPermissionLauncher: ActivityResultLauncher<String>? = null

    /*https://www.bizouk.com/bo/events-orders/orders-list-export?event=70464*/

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (CompatibilityManager.isTiramisu()) {
            // init Post notifications
            initPostNotificationsForAndroid13()

            // Handle onBackPressed for Android 13+
            /* onBackInvokedDispatcher
                 .registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                     Timber.e("Android 13+ onBackInvokedDispatcher | OnBackInvokedDispatcher.registerOnBackInvokedCallback()")
                     backPressed()
                 }*/
        } else {
            /*onBackPressedDispatcher
                .addCallback(
                    this,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            Timber.e("Android 13- onBackPressedDispatcher | OnBackPressedCallback.handleOnBackPressed() | finish()")
                            // Back is pressed... Finishing the activity
                            backPressed()
                        }
                    })*/
        }

        initPermissionLauncher()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    BarcodeScannerSTLTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainContent(mViewModel)
                        }
                    }
                }
            }
        }

        checkPermission()
    }

    override fun backPressed() {
        Timber.e("override | backPressed()")
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()

        runCatching {
            cameraExecutor.shutdown()
        }
            .onFailure {
                Timber.e("Exception caught with message: ${it.message}")
            }
    }

    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    private fun initPermissionLauncher() {
        Timber.d("initPermissionLauncher()")
        mPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (!isGranted) {
                    Timber.e("Camera permission is NOT granted")
                } else {
                    Timber.d("Camera permission is granted")
                    mViewModel.updateShowCamera(true)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initPostNotificationsForAndroid13() {
        Timber.d("initPostNotificationsForAndroid13()")
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launchPermissionRequest(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            Timber.d("POST_NOTIFICATIONS Permission granted")
        }
    }

    private fun checkPermission() {
        Timber.d("checkPermission()")
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.e("CAMERA Permission NOT granted")
            launchPermissionRequest(Manifest.permission.CAMERA)
        } else {
            Timber.d("CAMERA Permission granted")
            mViewModel.updateShowCamera(true)
        }
    }

    private fun launchPermissionRequest(permission: String) {
        Timber.e("requestPermission() | permission: $permission")
        mPermissionLauncher?.launch(permission) ?: {
            Timber.e("Permission launcher has NOT been initialized")
        }
    }

    private fun startCamera() {
        Timber.d("startCamera()")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also {
                    // it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

            } catch (exc: Exception) {
                Timber.e("Use case binding failed: ", exc.message)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    fun launchCheckActivity(data: String) =
        Intent(this, CheckActivity::class.java)
            .apply {
                this.putExtra(Constants.EXTRA_BARCODE_DATA, data)
            }
            .runCatching {
                startActivity(this)
            }
            .onFailure {
                Timber.e("runCatching | onFailure | Exception caught with message: ${it.message}")
            }
            .onSuccess {
                Timber.d("runCatching | onSuccess")
            }
}