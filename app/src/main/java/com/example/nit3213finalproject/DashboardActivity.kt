package com.example.nit3213finalproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DashboardActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dashboardAdapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Get the keypass from the Intent
        val keypass = intent.getStringExtra("keypass") ?: ""

        // Log and confirm keypass is received
        Toast.makeText(this, "Received keypass: $keypass", Toast.LENGTH_SHORT).show()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (keypass.isNotEmpty()) {
            fetchDashboardData(keypass)
        } else {
            Toast.makeText(this, "Failed to retrieve keypass", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDashboardData(keypass: String) {
        val url = "https://vu-nit3213-api.onrender.com/dashboard/$keypass"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@DashboardActivity, "Failed to load dashboard", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                // Log the API response
                runOnUiThread {
                    Toast.makeText(this@DashboardActivity, "API Response: $responseBody", Toast.LENGTH_LONG).show()
                }

                if (response.isSuccessful && responseBody != null) {
                    val entitiesJsonArray = JSONObject(responseBody).getJSONArray("entities")
                    runOnUiThread {
                        displayEntities(entitiesJsonArray)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@DashboardActivity, "Error loading data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun displayEntities(entitiesJsonArray: JSONArray) {
        val entities = mutableListOf<Entity>()
        for (i in 0 until entitiesJsonArray.length()) {
            val entityJson = entitiesJsonArray.getJSONObject(i)
            val assetType = entityJson.getString("assetType")
            val ticker = entityJson.getString("ticker")
            val currentPrice = entityJson.getDouble("currentPrice")
            val dividendYield = entityJson.getDouble("dividendYield")
            val description = entityJson.getString("description")

            entities.add(Entity(assetType, ticker, currentPrice, dividendYield, description))
        }

        // Confirm data is ready to be displayed
        runOnUiThread {
            Toast.makeText(this, "Displaying ${entities.size} entities", Toast.LENGTH_SHORT).show()
            dashboardAdapter = DashboardAdapter(this, entities)
            recyclerView.adapter = dashboardAdapter
        }
    }
}