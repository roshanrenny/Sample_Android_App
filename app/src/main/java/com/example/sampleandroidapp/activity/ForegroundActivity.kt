package com.example.sampleandroidapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.services.Foregroundservice

class ForegroundActivity : AppCompatActivity() {
    private lateinit var startMusicButton: Button
    private lateinit var stopMusicButton: Button
    private var isMusicPlaying = false
    private var musicServiceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground)

        startMusicButton = findViewById(R.id.button_start_music)
        stopMusicButton = findViewById(R.id.button_stop_music)

        musicServiceIntent = Intent(this, Foregroundservice::class.java)

        startMusicButton.setOnClickListener {
            if (!isMusicPlaying) {
                startService(musicServiceIntent)
                isMusicPlaying = true
                Toast.makeText(this,"Playing Music",Toast.LENGTH_SHORT).show()
                Log.d("ForegroundActivity","Music Started")
            }
        }

        stopMusicButton.setOnClickListener {
            if (isMusicPlaying) {
                stopService(musicServiceIntent)
                isMusicPlaying = false
                Toast.makeText(this,"Music Stopped",Toast.LENGTH_SHORT).show()
                Log.d("ForegroundActivity","Music Stopped")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isMusicPlaying = Foregroundservice.isServiceRunning(this)
    }
}
