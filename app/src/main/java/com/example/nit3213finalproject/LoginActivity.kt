package com.example.nit3213finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Set click listener for login button
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(username: String, password: String) {
        val url = "https://vu-nit3213-api.onrender.com/sydney/auth"
        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        // Make API call
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Login Failed: Network Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                // Check if the login is successful
                if (response.isSuccessful) {
                    val json = JSONObject(responseBody!!)
                    val keypass = json.getString("keypass")

                    // Log the keypass and ensure it's not empty
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login Successful, keypass: $keypass", Toast.LENGTH_SHORT).show()

                        // Start DashboardActivity and pass the keypass
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("keypass", keypass)  // Pass the keypass to the next activity
                        startActivity(intent)
                    }
                } else {
                    // Handle invalid credentials
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Invalid credentials: ${responseBody}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}