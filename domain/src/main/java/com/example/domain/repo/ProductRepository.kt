package com.example.domain.repo

import com.example.domain.R
import com.example.domain.model.Product
import com.example.domain.model.ProductReview
import com.example.domain.service.ProductService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productService: ProductService) {

    suspend fun getProducts(): Flow<List<Product>> = flow {
        try {
            val response = productService.getProducts()
            emit(response)
        } catch (e: Exception) {
            println("DATA_ENSAH Error fetching products: ${e.message}")
            throw Exception("${R.string.error_message_failed_to_fetch_products}", e)
        }
    }

    suspend fun getProductReview(): Flow<List<ProductReview>> = flow {
        try {
            val response = productService.getReviews()
            emit(response)
        } catch (e: Exception) {
            throw Exception("${R.string.error_message_failed_to_fetch_products_reviews}", e)
        }
    }
}