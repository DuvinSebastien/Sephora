package com.example.feature

import app.cash.turbine.test
import com.example.domain.model.Product
import com.example.domain.model.ProductReview
import com.example.domain.model.Review
import com.example.domain.repo.ProductRepository
import com.example.domain.utils.Resource
import com.example.feature.model.ProductWithReviews
import com.example.feature.usecase.FetchProductsWithReviewsUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException


@ExperimentalCoroutinesApi
class FetchProductsWithReviewsUseCaseTest {

    private val productRepository: ProductRepository = mockk()
    private lateinit var useCase: FetchProductsWithReviewsUseCase

    @Before
    fun setUp() {
        useCase = FetchProductsWithReviewsUseCase(productRepository)
    }

    @Test
    fun `execute emits loading followed by success when both products and reviews succeed`() =
        runTest {
            // Arrange
            val products = listOf(
                Product(
                    1461267310, "Mascara", "Description", 140.0,
                    null, null, false, false
                ),
                Product(
                    1461267311, "Kit Bain", "Description", 120.0,
                    null, null, true, false
                )
            )
            val reviews = listOf(
                ProductReview(
                    1461267310,
                    hide = false,
                    reviews = listOf(Review("User1", "Great!", 5f))
                ),
                ProductReview(
                    1461267311,
                    hide = false,
                    reviews = listOf(Review("User2", "Not bad", 4f))
                )
            )

            val productsFlow = flowOf(Resource.Success(products))
            val reviewsFlow = flowOf(Resource.Success(reviews))

            every { productRepository.fetchProducts() } returns productsFlow
            every { productRepository.fetchProductReview() } returns reviewsFlow

            val expected = listOf(
                ProductWithReviews(
                    product = products[0],
                    reviews = reviews[0].reviews
                ),
                ProductWithReviews(
                    product = products[1],
                    reviews = reviews[1].reviews
                )
            )

            // Act & Assert
            useCase.execute().test {
                assertEquals(Resource.Loading, awaitItem())
                assertEquals(Resource.Success(expected), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `execute emits loading followed by error when products fail`() = runTest {
        // Arrange
        val reviews = listOf(
            ProductReview(
                1461267310, hide = false, reviews = listOf(
                    Review(
                        "User1",
                        "Great!", 5f
                    )
                )
            )
        )

        val productsFlow = flowOf(Resource.Error(Exception("Products fetch failed")))
        val reviewsFlow = flowOf(Resource.Success(reviews))

        every { productRepository.fetchProducts() } returns productsFlow
        every { productRepository.fetchProductReview() } returns reviewsFlow

        // Act & Assert
        useCase.execute().test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem() as Resource.Error
            assertEquals("Products fetch failed", error.exception.message)
            awaitComplete()
        }
    }

    @Test
    fun `execute emits loading followed by error when reviews fail`() = runTest {
        // Arrange
        val products = listOf(
            Product(
                1461267310, "Mascara", "Description", 140.0,
                null, null, false, false
            )
        )

        val productsFlow = flowOf(Resource.Success(products))
        val reviewsFlow = flowOf(Resource.Error(Exception("Reviews fetch failed")))

        every { productRepository.fetchProducts() } returns productsFlow
        every { productRepository.fetchProductReview() } returns reviewsFlow

        // Act & Assert
        useCase.execute().test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem() as Resource.Error
            assertEquals("Reviews fetch failed", error.exception.message)
            awaitComplete()
        }
    }

    @Test
    fun `execute emits error on UnknownHostException`() = runTest {
        // Arrange
        every { productRepository.fetchProducts() } throws
                UnknownHostException("No internet connection")
        every { productRepository.fetchProductReview() } throws
                UnknownHostException("No internet connection")

        // Act & Assert
        useCase.execute().test {
            assertEquals(Resource.Loading, awaitItem())
            val error = awaitItem() as Resource.Error
            assertEquals("No internet connection", error.exception.message)
            awaitComplete()
        }
    }
}
