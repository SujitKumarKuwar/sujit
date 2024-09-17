package com.example.nit3213finalproject

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get the entity details from the Intent
        val entity = intent.getSerializableExtra("entityDetails") as? Entity

        if (entity != null) {
            // Display entity details (you should have these TextViews in activity_detail.xml)
            val assetTypeTextView: TextView = findViewById(R.id.assetTypeTextView)
            val tickerTextView: TextView = findViewById(R.id.tickerTextView)
            val priceTextView: TextView = findViewById(R.id.priceTextView)
            val dividendYieldTextView: TextView = findViewById(R.id.dividendYieldTextView)
            val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)

            assetTypeTextView.text = entity.assetType
            tickerTextView.text = entity.ticker
            priceTextView.text = "Price: \$${entity.currentPrice}"
            dividendYieldTextView.text = "Dividend Yield: ${entity.dividendYield}%"
            descriptionTextView.text = entity.description
        } else {
            // Show an error message if the entity is null
            Toast.makeText(this, "Failed to load entity details", Toast.LENGTH_SHORT).show()
        }
    }
}
