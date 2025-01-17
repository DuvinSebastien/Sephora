package com.example.domain.utils

import com.example.domain.model.Brand
import com.example.domain.model.ImagesUrl
import com.example.domain.model.Product
import com.example.domain.model.ProductReview
import com.example.domain.model.Review

val sampleProduct = Product(
    id = 1,
    name = "Sample Product",
    description = "A description for the sample product.",
    price = 49.99,
    imagesUrl = ImagesUrl(
        small = "https://example.com/sample_small.jpg",
        large = "https://example.com/sample_large.jpg"
    ),
    brand = Brand(
        id = "BR001",
        name = "Sample Brand"
    ),
    isProductSet = false,
    isSpecialBrand = false
)


val sampleProductReview = ProductReview(
    productId = 12345,
    hide = false,
    reviews = listOf(
        Review(name = "Alice", text = "Amazing product!", rating = 4.5f),
        Review(name = "Bob", text = "Good value for the price", rating = 3.8f),
        Review(name = "Charlie", text = "Not worth the hype", rating = 2.0f)
    )
)
