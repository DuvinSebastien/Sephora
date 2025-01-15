package com.example.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun HomeScreen(modifier: Modifier, viewModel: HomeViewModel = hiltViewModel()) {

    val productList by viewModel.productList.collectAsStateWithLifecycle()

    println("DATA_ENSAH = $productList")

    Column(modifier = modifier){
        Text(text = "Hello")

    }
}