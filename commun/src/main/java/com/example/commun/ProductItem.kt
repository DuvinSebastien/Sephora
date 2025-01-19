package com.example.commun

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import com.example.commun.utils.formatPriceToString
import com.example.commun.utils.selectQualityUrlImage
import com.example.domain.model.Product
import com.example.domain.model.Review


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItem(
    product: Product,
    reviews: List<Review>,
    isExpanded: Boolean,
    onExpandClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageUrl = product.imagesUrl?.selectQualityUrlImage()
    val localDensity = LocalDensity.current

    // Create element height in dp state
    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
            .height(if (isExpanded) 375.dp + columnHeightDp else 375.dp)
            .padding(4.dp)
            .clickable { onExpandClick(product.id) },
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                failure = placeholder(R.drawable.empty_product),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = product.name.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = product.price.formatPriceToString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center

                )

                if (isExpanded) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(modifier.onGloballyPositioned { coordinates ->
                        columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                    }) {
                        reviews.forEach { review ->
                            ReviewItem(review = review)
                        }
                    }
                }
            }
        }
    }
}