package com.example.feature.model

import com.example.domain.model.Product
import com.example.domain.model.Review

data class ProductWithReviews(
    val product: Product,
    val reviews: List<Review>?
)