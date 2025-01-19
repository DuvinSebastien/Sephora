package com.example.feature.usecase

import com.example.domain.repo.ProductRepository
import com.example.domain.utils.Resource
import com.example.feature.model.ProductWithReviews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject

class FetchProductsWithReviewsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    fun execute(): Flow<Resource<List<ProductWithReviews>>> = flow {
        emit(Resource.Loading)
        try {
            val productsFlow = productRepository.fetchProducts()
            val reviewsFlow = productRepository.fetchProductReview()

            combine(productsFlow, reviewsFlow) { productsResource, reviewsResource ->
                when {
                    productsResource is Resource.Success && reviewsResource is Resource.Success -> {
                        val combinedList = productsResource.data?.map { product ->
                            val matchingReviews =
                                reviewsResource.data?.find { it.productId == product.id }?.reviews
                            ProductWithReviews(product, matchingReviews)
                        }
                        Resource.Success(combinedList)
                    }

                    productsResource is Resource.Error -> productsResource
                    reviewsResource is Resource.Error -> reviewsResource
                    else -> Resource.Error(Exception("Unknown error"))
                }
            }.collect {
                emit(it)
            }
        } catch (e: UnknownHostException) {
            emit(Resource.Error(Exception("No internet connection", e)))
        } catch (e: Exception) {
            emit(Resource.Error(Exception("Error fetching combined data", e)))
        }
    }
}
