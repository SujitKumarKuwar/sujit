package com.example.nit3213finalproject

import java.io.Serializable

data class Entity(
    val assetType: String,
    val ticker: String,
    val currentPrice: Double,
    val dividendYield: Double,
    val description: String
) : Serializable // Ensures that Entity can be passed between activities
