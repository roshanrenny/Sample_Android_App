package com.example.sampleandroidapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleandroidapp.roomdb_signups.AppDatabase
import com.example.sampleandroidapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var binding: ActivityMainBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)

        binding.signBtn.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passswordInput.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Enter the required credentials")
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    val user = db.userDao().getUserByUsername(username)
                    withContext(Dispatchers.Main) {
                        if (user == null) {
                            showToast("Invalid Username or Password")
                        } else {
                            if (user.username == username && user.password == password) {
                                showToast("Login Success")
                                navigateToDetailsActivity()
                            } else {
                                showToast("Invalid Username or Password")
                            }
                        }
                    }
                }
            }
        }

        binding.facebookBtn.setOnClickListener {
            val intent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/login"))
            startActivity(intent)
            showToast("You are In Facebook Page")
        }

        binding.linkdinBtn.setOnClickListener {
            val intent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://in.linkedin.com/"))
            startActivity(intent)
            showToast("You are In LinkedIn Page")
        }

        binding.githubBtn.setOnClickListener {
            val intent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com"))
            startActivity(intent)
            showToast("You are In GitHub Page")
        }
    }

    private fun navigateToDetailsActivity() {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
