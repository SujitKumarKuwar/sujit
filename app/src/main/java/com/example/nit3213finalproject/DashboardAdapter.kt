package com.example.nit3213finalproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class DashboardAdapter(
    private val context: Context,
    private val entities: List<Entity>
) : RecyclerView.Adapter<DashboardAdapter.EntityViewHolder>() {

    // Inner class representing the ViewHolder
    class EntityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val assetTypeTextView: TextView = itemView.findViewById(R.id.assetTypeTextView)
        val tickerTextView: TextView = itemView.findViewById(R.id.tickerTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val dividendYieldTextView: TextView = itemView.findViewById(R.id.dividendYieldTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entity, parent, false)
        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        val entity = entities[position]
        holder.assetTypeTextView.text = entity.assetType
        holder.tickerTextView.text = entity.ticker
        holder.priceTextView.text = "Price: \$${entity.currentPrice}"
        holder.dividendYieldTextView.text = "Dividend Yield: ${entity.dividendYield}%"

        // Handle click event to navigate to DetailsActivity
        holder.itemView.setOnClickListener {
            // Displaying a Toast to check if click is working
            Toast.makeText(context, "Item clicked: ${entity.ticker}", Toast.LENGTH_SHORT).show()

            // Passing entity object to DetailsActivity
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("entityDetails", entity)  // Entity class must implement Serializable
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = entities.size
}

