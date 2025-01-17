package com.example.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commun.utils.SortList
import com.example.domain.model.Product
import com.example.domain.model.ProductReview
import com.example.domain.model.Review
import com.example.domain.repo.ProductRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.Resource.IDLE.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _productList = MutableStateFlow<Resource<List<Product>>>(Resource.IDLE)
    val productList: StateFlow<Resource<List<Product>>> = searchText
        .combine(_productList) { search, products ->
            if (products is Resource.Success) {
                if (search.isBlank()) {
                    products
                } else {
                    val searchList = products.data()
                        ?.filter { product ->
                            product.name.uppercase().contains(search.trim().uppercase())
                        }
                    Resource.Success(searchList)
                }
            } else {
                products
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _productList.value
        )


    private val _reviewsListById = MutableStateFlow<Map<Int, List<Review>>>(emptyMap())
    val reviewsListById: StateFlow<Map<Int, List<Review>>> = _reviewsListById

    private val _reviewsListOfProduct = MutableStateFlow<List<ProductReview>>(emptyList())

    private val _sortAscending = MutableStateFlow(SortList.BEST_TO_WORST)
    val sortAscending: StateFlow<SortList> = _sortAscending

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    val sortList = listOf(SortList.BEST_TO_WORST, SortList.WORST_TO_BEST)

    init {
        fetchProducts()
        fetchReview()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            productRepository.fetchProducts().onStart {
                _productList.value = Resource.Loading
            }.catch {
                _isRefreshing.value = false
                _productList.value = Resource.Error(it)
            }.collect { products ->
                _isRefreshing.value = false
                _productList.value = products
            }

        }
    }

    private fun fetchReview() {
        viewModelScope.launch {
            productRepository.fetchProductReview().collect { reviews ->
                reviews.data()?.let {
                    _reviewsListOfProduct.value = it
                }
            }
        }
    }

    fun refresh() {
        fetchProducts()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun orderReview(value: SortList) {
        _sortAscending.value = value
    }

    fun getReviewsListByProductId(productId: Int) {
        val reviews = _reviewsListOfProduct.value
            .filter { it.productId == productId }
            .flatMap { it.reviews }
            .sortedWith(compareBy { it.rating })
            .let { if (_sortAscending.value == SortList.WORST_TO_BEST) it else it.reversed() }

        _reviewsListById.value = _reviewsListById.value.toMutableMap().apply {
            put(productId, reviews)
        }
    }
}