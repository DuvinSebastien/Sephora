package com.example.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commun.utils.SortList
import com.example.domain.model.Review
import com.example.domain.utils.Resource
import com.example.feature.model.ProductViewState
import com.example.feature.usecase.FetchProductsWithReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchProductsWithReviewsUseCase: FetchProductsWithReviewsUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ProductViewState())
    val viewState: StateFlow<ProductViewState>
        get() = _viewState.map { productViewState ->
            productViewState.copy(products = productViewState.products.filter { product ->
                product.name.uppercase().contains(productViewState.searchText.trim().uppercase())
            })
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _viewState.value
        )

    init {
        fetchProductsAndReviews()
    }

    fun refresh() {
        fetchProductsAndReviews()
        _viewState.update {
            it.copy(isRefreshing = true)
        }
    }

    private fun fetchProductsAndReviews() {
        viewModelScope.launch {
            fetchProductsWithReviewsUseCase.execute().collect { resource ->
                when (resource) {
                    is Resource.Loading -> _viewState.update {
                        _viewState.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        resource.data?.let { data ->
                            _viewState.update {
                                it.copy(
                                    products = data.map { it.product },
                                    reviews = data.associate {
                                        it.product.id to sortListReview(
                                            SortList.BEST_TO_WORST,
                                            it.reviews!!
                                        )
                                    },
                                    isLoading = false,
                                    isError = false,
                                    isRefreshing = false
                                )
                            }
                        }
                    }

                    is Resource.Error -> _viewState.update {
                        _viewState.value.copy(
                            isError = true, isLoading = false, isRefreshing = false
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    private fun sortListReview(sortAscending: SortList, reviews: List<Review>) =
        reviews.sortedWith(compareBy { it.rating }).let {
            if (sortAscending == SortList.WORST_TO_BEST) it else it.reversed()
        }

    fun onSearchTextChange(text: String) {
        _viewState.update {
            it.copy(searchText = text)
        }
    }

    fun orderReviews(value: SortList) {
        _viewState.update {
            it.copy(sortAscending = value,
                reviews = _viewState.value.reviews.mapValues {
                    sortListReview(
                        sortAscending = value,
                        reviews = it.value
                    )
                }

            )
        }
    }
}