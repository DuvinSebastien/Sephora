package com.example.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.repo.ProductRepository
import com.example.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productList = MutableStateFlow<Resource<List<Product>>>(Resource.IDLE)
    val productList: StateFlow<Resource<List<Product>>> = _productList

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            productRepository.getProducts().onStart {
                _productList.value = Resource.Loading
            }.catch {
                println("DATA_ENSAH Error fetching products VM : ${it.message}")
                _productList.value = Resource.Error(it)
            }.collect { products ->
                _productList.value = Resource.Success(products)
            }

        }
    }
}