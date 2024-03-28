package com.example.sampleandroidapp.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sampleandroidapp.roomdb_signups.AppDatabase
import com.example.sampleandroidapp.roomdb_signups.User
import com.example.sampleandroidapp.databinding.ActivityRegistrationBinding
import com.example.sampleandroidapp.testing.RegistrationTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(applicationContext)

        binding.hoverusername.setOnClickListener {
            showAlertDialog("Username requirement", "Username must start with an uppercase letter contain at least one digit and be at least 8 characters long : Example - Roshan23")
        }
        binding.hoverpassword.setOnClickListener {
            showAlertDialog("Password requirement","Password must contain at least one uppercase letter one digit one special character and be at least 8 characters long : Example - Roshan@123")
        }

        binding.signupBtn.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passswordInput.text.toString()

            if (isValidUsername(username) && isValidPassword(password)) {
                val user = User(username = username, password = password)
                registerUser(user)
            } else {
                showToast("Enter username or password as per requirements")
            }
        }


        binding.Facebookbutton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/login"))
            startActivity(intent)
            showToast("You are In Facebook Page")
        }

        binding.Linkdinbutton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://in.linkedin.com/"))
            startActivity(intent)
            showToast("You are In LinkDin Page")
        }

        binding.Githubbutton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com"))
            startActivity(intent)
            showToast("You are In GitHub Page")
        }
    }

    private fun isValidUsername(username: String): Boolean {
        return RegistrationTesting.isValidUsername(username)
    }

    private fun isValidPassword(password: String): Boolean {
        return RegistrationTesting.isValidPassword(password)
    }

    private fun registerUser(user: User) {
        lifecycleScope.launch {
            val existingUser = getUserByUsername(user.username)
            if (existingUser == null) {
                insertUser(user)
            } else {
                showToast("Username already taken")
            }
        }
    }

    private suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDao().insert(user)
        }
        showToast("Registration Success")
        navigateToMainActivity()
    }

    private suspend fun getUserByUsername(username: String): User? {
        return withContext(Dispatchers.IO) {
            db.userDao().getUserByUsername(username)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}
