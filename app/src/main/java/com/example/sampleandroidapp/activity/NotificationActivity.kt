package com.example.sampleandroidapp.activity


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleandroidapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class NotificationActivity : AppCompatActivity() {
    private lateinit var etToken : EditText

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)

        etToken = findViewById(R.id.etToken)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                print("Fetching FCM registration Token failed")
                return@OnCompleteListener
            }


            val token = task.result

            print(token)
            Toast.makeText(this, "Your device registration token is"+ token, Toast.LENGTH_SHORT).show()

            etToken.setText(token)
            Log.d("Token",token)

        })

    }
}
