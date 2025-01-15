package com.example.domain.model

data class ProductReview(
    val product_id: Long,
    val hide: Boolean? = null,
    val reviews: List<Review>
)

data class Review(
    val name: String? = null, // Optional, since "name" is not always present
    val text: String? = null, // Optional, since "text" might not always be present
    val rating: Float? = null // Optional, since "rating" can be null
)
