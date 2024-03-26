package com.example.sampleandroidapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.apipost.Methods
import com.example.sampleandroidapp.apipost.Model
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiActivity : AppCompatActivity() {
    private lateinit var btnGetData: Button
    private lateinit var btnPostData: Button
    private lateinit var btnUpdateData: Button
    private lateinit var btnDeleteData: Button
    private lateinit var btnSubmit: Button
    private lateinit var editTextUserId: EditText
    private lateinit var editTextTitle: EditText
    private lateinit var editTextBody: EditText
    private lateinit var editTextId: EditText
    private lateinit var textView: TextView
    private lateinit var apiService: Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)

        textView = findViewById(R.id.textResult)
        editTextId = findViewById(R.id.editTextId)
        editTextUserId = findViewById(R.id.editTextUserId)
        editTextTitle = findViewById(R.id.edittextTitle)
        editTextBody = findViewById(R.id.editTextBody)
        btnPostData = findViewById(R.id.btnPostData)
        btnGetData = findViewById(R.id.btnGetData)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnUpdateData = findViewById(R.id.btnUpdateData)
        btnDeleteData = findViewById(R.id.btnDeleteData)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(Methods::class.java)

        btnGetData.setOnClickListener { getPosts() }
        btnPostData.setOnClickListener { postData() }
        btnDeleteData.setOnClickListener { deleteData() }
        btnUpdateData.setOnClickListener { updateData() }
    }

    fun getPosts() {
        val call: Call<List<Model>> = apiService.getPosts()
        call.enqueue(object : Callback<List<Model>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (!response.isSuccessful) {
                    textView.text = "code" + response.code()
                    return
                }
                val posts: List<Model>? = response.body()
                posts?.let {
                    for (post in posts) {
                        var content = ""
                        content += "ID: ${post.id}\n"
                        content += "User ID: ${post.userId}\n"
                        content += "Title: ${post.title}\n"
                        content += "Body: ${post.text}\n\n"
                        textView.append(content)
                    }
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                textView.text = t.message
            }
        })
    }

    private fun postData() {
        textView.text = ""
        editTextBody.setText("")
        editTextUserId.setText("")
        editTextTitle.setText("")
        editTextUserId.visibility = View.VISIBLE
        editTextTitle.visibility = View.VISIBLE
        editTextBody.visibility = View.VISIBLE
        btnSubmit.visibility = View.VISIBLE
        btnSubmit.setOnClickListener { submitData() }
    }

    private fun submitData() {
        val userIdText = editTextUserId.text.toString()
        val userId = userIdText.toInt()
        Log.d("SubmitData", "UserID: $userId")
        val title = editTextTitle.text.toString()
        Log.d("SubmitData", "Title: $title")
        val text = editTextBody.text.toString()
        Log.d("SubmitData", "Text: $text")
        putData(userId, title, text)
    }

    private fun putData(userId: Int, title: String, text: String) {
        editTextUserId.visibility = View.GONE
        editTextTitle.visibility = View.GONE
        editTextBody.visibility = View.GONE
        btnSubmit.visibility = View.GONE
        val call: Call<Model>? = apiService.createPost(userId, title, text)
        call?.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                if (response.isSuccessful) {
                    editTextUserId.visibility = View.GONE
                    editTextTitle.visibility = View.GONE
                    editTextBody.visibility = View.GONE
                    Toast.makeText(this@ApiActivity, "Data posted successfully", Toast.LENGTH_SHORT).show()
                    val postResponse: Model? = response.body()
                    var content = ""
                    content += "Code: ${response.code()}\n"
                    postResponse?.let {
                        content += "ID: ${postResponse.id}\n"
                        content += "User ID: ${postResponse.userId}\n"
                        content += "Title: ${postResponse.title}\n"
                        content += "Body: ${postResponse.text}\n\n"
                    }
                    textView.text = content
                    textView.append("\n\n")
                    getPosts()
                } else {
                    Toast.makeText(this@ApiActivity, "Failed to post data.", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Error:" + response.code())
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                Log.d("TAG", "Error in getting response " + t.message)
                Toast.makeText(this@ApiActivity, "Failed to post data. Error: ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteData() {
        textView.text = ""
        editTextId.setText("")
        editTextId.visibility = View.VISIBLE
        btnSubmit.visibility = View.VISIBLE
        val idText = editTextId.text.toString()
        val id = if (idText.isNotEmpty()) idText.toInt() else -1
        btnSubmit.setOnClickListener { deletePost(id) }
    }

    private fun deletePost(id: Int) {
        btnSubmit.visibility = View.GONE
        editTextId.visibility = View.GONE
        val call: Call<Void> = apiService.deletePost(id)
        call.enqueue(object : Callback<Void> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    textView.text = "code" + response.code()
                    textView.append("\n\nData deleted successfully!!\n\n")
                    getPosts()
                } else {
                    textView.text = "code" + response.code()
                    textView.append("\n\nFailed to delete data.\n\n")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                textView.text = t.message
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateData() {
        textView.text = ""
        editTextId.setText("")
        editTextId.visibility = View.VISIBLE
        editTextUserId.visibility = View.VISIBLE
        editTextTitle.visibility = View.VISIBLE
        editTextBody.visibility = View.VISIBLE
        btnSubmit.visibility = View.VISIBLE
        btnSubmit.text = "Update"
        btnSubmit.setOnClickListener { submitUpdate() }
    }

    private fun submitUpdate() {

        editTextId.visibility = View.GONE
        editTextUserId.visibility = View.GONE
        editTextTitle.visibility = View.GONE
        editTextBody.visibility = View.GONE
        btnSubmit.visibility = View.GONE
        val id = editTextId.text.toString().toInt()
        val userId = editTextUserId.text.toString().toInt()
        val title = editTextTitle.text.toString()
        val text = editTextBody.text.toString()
        val call: Call<Model> = apiService.patchPost(id, Model(userId, title, text))
        call.enqueue(object : Callback<Model> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                if (response.isSuccessful) {
                    val updatedPost: Model? = response.body()
                    textView.text = "Post updated successfully!\n\n"
                    updatedPost?.let {
                        textView.append("ID: ${updatedPost.id}\n")
                        textView.append("User ID: ${updatedPost.userId}\n")
                        textView.append("Title: ${updatedPost.title}\n")
                        textView.append("Body: ${updatedPost.text}\n\n")
                    }
                    getPosts()
                } else {
                    Toast.makeText(this@ApiActivity, "Failed to update post.", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Error:" + response.code())
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                Log.d("TAG", "Error in getting response " + t.message)
                Toast.makeText(this@ApiActivity, "Failed to update post. Error: ", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
