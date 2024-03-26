package com.example.sampleandroidapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.services.BackgroundService
import com.google.android.material.navigation.NavigationView

class BackgroundActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var serviceIntent: Intent? = null
    private lateinit var drawerLayout: DrawerLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        val btnStart = findViewById<Button>(R.id.Startservice)
        val btnStop = findViewById<Button>(R.id.Stopservice)
        val btnForeground = findViewById<Button>(R.id.Playservice)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        btnStart.setOnClickListener {
            if (serviceIntent == null) {
                serviceIntent = Intent(this, BackgroundService::class.java)
                startService(serviceIntent)
                Log.d("com.example.sampleandroidapp.activity.BackgroundActivity", "Service Started")
            }
        }

        btnStop.setOnClickListener {
            if (serviceIntent != null) {
                stopService(serviceIntent)
                serviceIntent = null
                Log.d("com.example.sampleandroidapp.activity.BackgroundActivity", "Service Stopped")
            }
        }

        btnForeground.setOnClickListener {
            startActivity(Intent(this, ForegroundActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (BackgroundService.isServiceRunning(this)) {
            serviceIntent = Intent(this, BackgroundService::class.java)
        } else {
            serviceIntent = null
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_drawer -> {
                logout()
            }

            R.id.nav_notify ->{
                startActivity(Intent(this, NotificationActivity::class.java))
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}