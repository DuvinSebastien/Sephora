package com.example.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.commun.ProductItem
import com.example.commun.SearchBar
import com.example.commun.utils.animatedColor
import com.example.commun.utils.isInternetConnected
import com.example.commun.utils.snapshotStateMapSaver
import com.example.domain.model.Product
import com.example.domain.utils.Resource.IDLE.onEmptyCompose
import com.example.domain.utils.Resource.Loading.onErrorCompose
import com.example.domain.utils.Resource.Loading.onLoadingCompose
import com.example.domain.utils.Resource.Loading.onValueCompose


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier, viewModel: HomeViewModel = hiltViewModel()) {

    val productList by viewModel.productList.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val sortAscending by viewModel.sortAscending.collectAsStateWithLifecycle()
    val sortList = viewModel.sortList

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    Scaffold(modifier = modifier, topBar = {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(searchText = searchText, modifier.weight(1f), onValueChange = {
                viewModel.onSearchTextChange(it)
            })

            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopEnd)
            ) {

                IconButton(onClick = { isDropDownExpanded.value = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "")
                }
                DropdownMenu(expanded = isDropDownExpanded.value, onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                    sortList.forEachIndexed { index, sort ->
                        DropdownMenuItem(text = {
                            Text(text = sort.text)
                        }, leadingIcon = {
                            if (sortAscending == sort) {
                                Icon(Icons.Default.Check, contentDescription = "")
                            }
                        }, onClick = {
                            isDropDownExpanded.value = false
                            viewModel.orderReviews(sort)
                        })
                    }
                }
            }
        }

    }) { padding ->

        productList.onValueCompose { listProduct ->
            // Refresh view
            val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
            val state = rememberPullToRefreshState()

            PullToRefreshBox(modifier = Modifier.padding(padding),
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = {
                    viewModel.refresh()
                },
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        state = state,
                        isRefreshing = isRefreshing,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }) {
                ProductList(
                    listProduct,
                    viewModel = viewModel,
                )
            }
        }
        productList.onEmptyCompose {
            val preloaderLottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    com.example.commun.R.raw.empty_result
                )
            )

            val preloaderProgress by animateLottieCompositionAsState(
                preloaderLottieComposition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = preloaderLottieComposition,
                    progress = preloaderProgress,
                    modifier = Modifier.size(300.dp)
                )
            }
        }
        productList.onLoadingCompose {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(7) {
                    Card(
                        modifier = Modifier
                            .height(height = 375.dp)
                            .fillMaxWidth()
                            .padding(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = animatedColor(),
                        )
                    ) {}
                }
            }
        }
        productList.onErrorCompose {
            val context = LocalContext.current
            val preloaderLottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    com.example.commun.R.raw.error_server
                )
            )

            val preloaderProgress by animateLottieCompositionAsState(
                preloaderLottieComposition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = preloaderLottieComposition,
                    progress = preloaderProgress,
                    modifier = Modifier.size(300.dp)
                )
                Text(
                    text = it.message ?: stringResource(R.string.unknow_error),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
                if (isInternetConnected(context)) {
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            viewModel.refresh()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.retry).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                    }
                    Spacer(Modifier.height(16.dp))

                }
            }
        }

    }
}

@Composable
fun ProductList(products: List<Product>, viewModel: HomeViewModel, modifier: Modifier = Modifier) {

    val reviewsListById by viewModel.reviewsListById.collectAsStateWithLifecycle()
    val sortedReviews by viewModel.sortAscending.collectAsStateWithLifecycle()
    val isExpandedMap = rememberSaveable(saver = snapshotStateMapSaver()) {
        List(products.size) { index: Int -> index to false }.toMutableStateMap()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(products) { index, product ->
            val reviews = reviewsListById[product.id] ?: emptyList()
            LaunchedEffect(sortedReviews) {
                viewModel.getReviewsListByProductId(product.id)
            }

            ProductItem(product = product,
                reviews = reviews,
                isExpanded = isExpandedMap[index] ?: true,
                onExpandClick = {
                    isExpandedMap[index] = !(isExpandedMap[index] ?: true)
                    if (isExpandedMap[index] == true) {
                        viewModel.getReviewsListByProductId(product.id)
                    }
                })
        }
    }
}
