package com.example.domain


import app.cash.turbine.test
import com.example.domain.model.Product
import com.example.domain.model.ProductReview
import com.example.domain.repo.ProductRepository
import com.example.domain.service.ProductService
import com.example.domain.utils.Resource
import com.example.domain.utils.sampleProduct
import com.example.domain.utils.sampleProductReview
import com.google.gson.JsonSyntaxException
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.time.Duration.Companion.seconds

class ProductRepositoryTest {
    private lateinit var productService: ProductService
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        productService = mockk()
        repository = ProductRepository(productService)
    }

    // Product Tests
    @Test
    fun `fetchProducts returns success with products`() = runTest {
        // Given
        val mockProducts = listOf(sampleProduct)
        coEvery { productService.getProducts() } returns mockProducts

        // When & Then
        repository.fetchProducts().test(timeout = 5.seconds) {
            val item = awaitItem()
            assert(item is Resource.Success)
            assertEquals(mockProducts, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProducts returns empty list successfully`() = runTest {
        // Given
        val emptyList = emptyList<Product>()
        coEvery { productService.getProducts() } returns emptyList()

        // When & Then
        repository.fetchProducts().test(timeout = 5.seconds) {
            val item = awaitItem()
            assert(item is Resource.Success)
            assertEquals(emptyList, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProducts returns network error`() = runTest {
        // Given
        coEvery { productService.getProducts() } throws UnknownHostException()

        // When & Then
        repository.fetchProducts().test(timeout = 5.seconds) {
            val error = awaitItem()
            assert(error is Resource.Error)
            assertEquals("No internet connection", (error as Resource.Error).exception.message)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProducts handles HTTP error`() = runTest {
        // Given
        val httpException =
            HttpException(Response.error<List<Product>>(404, ResponseBody.create(null, "")))
        coEvery { productService.getProducts() } throws httpException

        // When & Then
        repository.fetchProducts().test(timeout = 5.seconds) {
            val error = awaitItem()
            assert(error is Resource.Error)
            assertEquals("Error fetching products", (error as Resource.Error).exception.message)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProducts handles malformed JSON response`() = runTest {
        // Given
        coEvery { productService.getProducts() } throws JsonSyntaxException("Malformed JSON")

        // When & Then
        repository.fetchProducts().test(timeout = 5.seconds) {
            val error = awaitItem()
            assert(error is Resource.Error)
            assertEquals("Error fetching products", (error as Resource.Error).exception.message)
            awaitComplete()
        }
    }

    // Review Tests
    @Test
    fun `fetchProductReview returns success with reviews`() = runTest {
        // Given
        val mockReviews = listOf(sampleProductReview)
        coEvery { productService.getReviews() } returns mockReviews

        // When & Then
        repository.fetchProductReview().test(timeout = 5.seconds) {
            val item = awaitItem()
            assert(item is Resource.Success)
            assertEquals(mockReviews, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProductReview returns empty reviews list successfully`() = runTest {
        // Given
        val emptyReviews = emptyList<ProductReview>()
        coEvery { productService.getReviews() } returns emptyReviews

        // When & Then
        repository.fetchProductReview().test(timeout = 5.seconds) {
            val item = awaitItem()
            assert(item is Resource.Success)
            assertEquals(emptyReviews, (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProductReview returns empty reviews list in ProductReview successfully`() = runTest {
        // Given
        val reviewWithEmptyList = ProductReview(
            productId = 1,
            hide = false,
            reviews = emptyList()
        )
        coEvery { productService.getReviews() } returns listOf(reviewWithEmptyList)

        // When & Then
        repository.fetchProductReview().test(timeout = 5.seconds) {
            val item = awaitItem()
            assert(item is Resource.Success)
            assertEquals(listOf(reviewWithEmptyList), (item as Resource.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProductReview returns network error`() = runTest {
        // Given
        coEvery { productService.getReviews() } throws UnknownHostException()

        // When & Then
        repository.fetchProductReview().test(timeout = 5.seconds) {
            val error = awaitItem()
            assert(error is Resource.Error)
            assertEquals("No internet connection", (error as Resource.Error).exception.message)
            awaitComplete()
        }
    }

    @Test
    fun `fetchProductReview handles HTTP error`() = runTest {
        // Given
        val httpException =
            HttpException(Response.error<List<ProductReview>>(500, ResponseBody.create(null, "")))
        coEvery { productService.getReviews() } throws httpException

        // When & Then
        repository.fetchProductReview().test(timeout = 5.seconds) {
            val error = awaitItem()
            assert(error is Resource.Error)
            assertEquals(
                "Error fetching product reviews",
                (error as Resource.Error).exception.message
            )
            awaitComplete()
        }
    }

    @Test
    fun `fetchProductReview handles timeout error`() = runTest {
        // Given
        coEvery { productService.getReviews() } throws SocketTimeoutException("Timeout")

        // When & Then
        repository.fetchProductReview().test(timeout = 5.seconds) {
            val error = awaitItem()
            assert(error is Resource.Error)
            assertEquals(
                "Error fetching product reviews",
                (error as Resource.Error).exception.message
            )
            awaitComplete()
        }
    }
}