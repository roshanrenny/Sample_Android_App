package com.example.sampleandroidapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.otp.OTPReceiver


class OTPActivity : AppCompatActivity() {
    private var editText_otp: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        editText_otp = findViewById(R.id.editText_otp)
        requestPermissions()
        OTPReceiver().setEditText_otp(editText_otp)
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this@OTPActivity, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@OTPActivity, arrayOf(Manifest.permission.RECEIVE_SMS), 100
            )
        }
    }
}