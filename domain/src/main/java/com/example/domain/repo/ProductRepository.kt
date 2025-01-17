package com.example.domain.repo

import com.example.domain.model.Product
import com.example.domain.model.ProductReview
import com.example.domain.service.ProductService
import com.example.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productService: ProductService) {

    fun fetchProducts(): Flow<Resource<List<Product>>> = flow {
        try {
            val response = productService.getProducts()
            emit(Resource.Success(response))
        } catch (e: UnknownHostException) {
            emit(Resource.Error(Exception("No internet connection", e)))
        } catch (e: Exception) {
            emit(Resource.Error(Exception("Error fetching products", e)))
        }
    }

    fun fetchProductReview(): Flow<Resource<List<ProductReview>>> = flow {
        try {
            val response = productService.getReviews()
            emit(Resource.Success(response))
        } catch (e: UnknownHostException) {
            emit(Resource.Error(Exception("No internet connection", e)))
        } catch (e: Exception) {
            emit(Resource.Error(Exception("Error fetching product reviews", e)))
        }
    }
}