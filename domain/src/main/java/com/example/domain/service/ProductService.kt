package com.example.domain.service

import com.example.domain.model.Product
import com.example.domain.model.ProductReview
import retrofit2.http.GET

interface ProductService {

    @GET("items.json")
    suspend fun getProducts(): List<Product>

    @GET("reviews.json")
    suspend fun getReviews(): List<ProductReview>
}