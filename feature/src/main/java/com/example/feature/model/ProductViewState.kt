package com.example.feature.model

import com.example.commun.utils.SortList
import com.example.domain.model.Product
import com.example.domain.model.Review

data class ProductViewState(
    val searchText: String = "",
    val products: List<Product> = emptyList(),
    val reviews: Map<Int, List<Review>> = emptyMap(),
    val sortAscending: SortList = SortList.BEST_TO_WORST,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isRefreshing: Boolean = false,
    val sortList: List<SortList> = listOf(SortList.BEST_TO_WORST, SortList.WORST_TO_BEST)
)