package com.example.sampleandroidapp.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sampleandroidapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    private var imageView: ImageView? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private val CAMERA_REQUEST = 1888
    private val VIDEO_CAPTURE_REQUEST = 1889
    private var mediaDir: File? = null
    private var capturedBitmap: Bitmap? = null
    private var capturedVideoUri: Uri? = null
    private lateinit var cameraThread : HandlerThread
    private lateinit var camerahandler : Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraThread = HandlerThread("CameraThread").apply { start() }
        camerahandler = Handler(cameraThread.looper)

        imageView = findViewById(R.id.imageView1)
        val photoButton = findViewById<Button>(R.id.button1)
        val videoButton = findViewById<Button>(R.id.button2)

        mediaDir = getExternalFilesDir(null)

        photoButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                startCamera(false)
            }
        }

        videoButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                startCamera(true)
            }
        }
    }

    private fun startCamera(isVideo: Boolean) {
        camerahandler.post {
            val cameraIntent: Intent = if (isVideo) {
                Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            } else {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            }
            startActivityForResult(
                cameraIntent,
                if (isVideo) VIDEO_CAPTURE_REQUEST else CAMERA_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startCamera(false)
            } else {
                showToast("Camera permission denied")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            runOnUiThread {
                when (requestCode) {
                    CAMERA_REQUEST -> {
                        capturedBitmap = data?.extras?.get("data") as Bitmap?
                        imageView?.setImageBitmap(capturedBitmap)
                        showSaveOrCancelDialog(false)
                    }

                    VIDEO_CAPTURE_REQUEST -> {

                        capturedVideoUri = data?.data
                        capturedVideoUri?.let {
                            imageView?.setImageURI(it)
                            showSaveOrCancelDialog(true)
                        }
                    }
                }
            }
        }
    }

    private fun showSaveOrCancelDialog(isVideo: Boolean) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to save the ${if (isVideo) "video" else "photo"}?")
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                if (isVideo) {
                    capturedVideoUri?.let { saveMediaToInternalStorage(it, isVideo) }
                } else {
                    capturedBitmap?.let { saveMediaToInternalStorage(it, isVideo) }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Save Media")
        alert.show()
    }

    private fun saveMediaToInternalStorage(media: Any, isVideo: Boolean) {
        val timestamp = System.currentTimeMillis()
        val fileExtension = if (isVideo) ".mp4" else ".jpg"
        val fileName = "media_$timestamp$fileExtension"
        val mediaFile = File(mediaDir, fileName)
        try {
            val outputStream = FileOutputStream(mediaFile)
            when {
                isVideo && media is Uri -> {
                    val inputStream = contentResolver.openInputStream(media)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                !isVideo && media is Bitmap -> {
                    media.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                else -> {
                    throw IllegalArgumentException("Invalid media type")
                }
            }
            showToast("${if (isVideo) "Video" else "Photo"} saved to ${mediaFile.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Failed to save ${if (isVideo) "video" else "photo"}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraThread.quitSafely()
    }
}
