package com.example.sampleandroidapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sampleandroidapp.fragments.DetailsFrangment
import com.example.sampleandroidapp.fragments.HomeFrangment
import com.example.sampleandroidapp.fragments.LocationFrangment
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.broadcast.BroadcastFragment
import com.example.sampleandroidapp.fragments.SettingsFrangment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailsActivity : AppCompatActivity() {

    private val homeFragment = HomeFrangment()
    private val detailsFragment = DetailsFrangment<Any>()
    private val locationFragment = LocationFrangment<Any>()
    private val settingsFrangment = SettingsFrangment()
    private val broadcastFragment = BroadcastFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_person -> replaceFragment(detailsFragment)
                R.id.ic_location -> replaceFragment(locationFragment)
                R.id.ic_settings -> replaceFragment(settingsFrangment)
                R.id.ic_broadcast -> replaceFragment(broadcastFragment)

            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

}