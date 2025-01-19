package com.example.commun

import com.example.commun.utils.selectQualityUrlImage
import com.example.domain.model.ImagesUrl
import org.junit.Assert.assertEquals
import org.junit.Test

class ImagesUrlTest {

    @Test
    fun `selectQualityUrlImage returns large image when large is not blank`() {
        val imagesUrl = ImagesUrl(
            small = "https://example.com/small.jpg",
            large = "https://example.com/large.jpg"
        )
        assertEquals("https://example.com/large.jpg", imagesUrl.selectQualityUrlImage())
    }

    @Test
    fun `selectQualityUrlImage returns small image when large is blank`() {
        val imagesUrl = ImagesUrl(
            small = "https://example.com/small.jpg",
            large = ""
        )
        assertEquals("https://example.com/small.jpg", imagesUrl.selectQualityUrlImage())
    }

    @Test
    fun `selectQualityUrlImage returns null when both large and small are blank`() {
        val imagesUrl = ImagesUrl(
            small = "",
            large = ""
        )
        assertEquals(null, imagesUrl.selectQualityUrlImage())
    }
}
