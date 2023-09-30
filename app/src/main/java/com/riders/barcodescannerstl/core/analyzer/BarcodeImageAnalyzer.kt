package com.riders.barcodescannerstl.core.analyzer

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.riders.barcodescannerstl.ui.camera.BarcodeBoxView
import timber.log.Timber

class BarcodeImageAnalyzer(
    private val context: Context,
    //private val barcodeBoxView: BarcodeBoxView,
    private val previewViewWidth: Float,
    private val previewViewHeight: Float,
    private val callback: (String) -> Unit
) : ImageAnalysis.Analyzer {

    /**
     * This parameters will handle preview box scaling
     */
    private var scaleX = 1f
    private var scaleY = 1f

    private fun translateX(x: Float) = x * scaleX
    private fun translateY(y: Float) = y * scaleY

    private fun adjustBoundingRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image? = imageProxy.image

        if (null == mediaImage) {
            Timber.e("No media image")
            return
        }

        // Update scale factors
        scaleX = previewViewWidth / mediaImage.height.toFloat()
        scaleY = previewViewHeight / mediaImage.width.toFloat()

        val image: InputImage =
            InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        // Pass image to an ML Kit Vision API

        // Process image searching for barcodes
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = BarcodeScanning.getClient(options)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    for (barcode in barcodes) {
                        // Handle received barcodes...
                        Timber.d("Handle received barcodes: ${barcode.rawValue}")

                        // Update bounding rect
                        barcode.boundingBox?.let { rect ->
                            // barcodeBoxView.setRect(adjustBoundingRect(rect))
                        }

                        barcode.rawValue?.let {
                            callback(it)
                        }
                    }
                } else {
                    // Remove bounding rect
                    // barcodeBoxView.setRect(RectF())
                }
            }
            .addOnFailureListener {
                // Timber.e("Exception caught with class: ${it::class.java.simpleName}, and message: ${it.message}")
                // barcodeBoxView.setRect(RectF())
            }

        imageProxy.close()
    }
}