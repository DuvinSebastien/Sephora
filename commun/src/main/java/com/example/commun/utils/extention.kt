package com.example.commun.utils

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import com.example.domain.model.ImagesUrl
import java.text.NumberFormat
import java.util.Locale


fun ImagesUrl.selectQualityUrlImage(): String? {
    return when {
        large.isNotBlank() -> large
        small.isNotBlank() -> small
        else -> null
    }
}

fun Double.formatPriceToString(): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.FRANCE)
    numberFormat.minimumFractionDigits = if (this % 1 == 0.0) 0 else 2
    return "${numberFormat.format(this)}€"
}

@Composable
fun animatedColor(): Color {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.background,
        targetValue = Color.LightGray,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "",
    )
    return color
}

fun <V> snapshotStateMapSaver(): Saver<SnapshotStateMap<Int, V>, *> = Saver(
    save = { map ->
        map.entries.associate { it.key.toString() to it.value }
    },
    restore = { map ->
        SnapshotStateMap<Int, V>().apply {
            map.forEach { (key, value) ->
                this[key.toInt()] = value
            }
        }
    }
)