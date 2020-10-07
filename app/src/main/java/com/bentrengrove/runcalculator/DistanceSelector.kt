package com.bentrengrove.runcalculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.unit.dp

val distances = listOf(
    "800m" to 0.8f,
    "1500m" to 1.5f,
    "5 km" to 5f,
    "10 km" to 10f,
    "Half Marathon" to 21.1f,
    "Marathon" to 42.2f
)

@Composable
fun DistanceSelector(selectedItem: MutableState<Pair<String, Float>>, modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }

    Column(modifier.padding(vertical = 16.dp)) {
        Text(text = "Distance", style = MaterialTheme.typography.overline)
        DropdownMenu(toggle = {
            val anno = annotatedString { append(selectedItem.value.first) }
            val arrowRes = if (expanded.value) R.drawable.ic_baseline_arrow_drop_up_24 else R.drawable.ic_baseline_arrow_drop_down_24
            Row(Modifier.clickable(onClick = { expanded.value = true }).padding(horizontal = 8.dp)) {
                Text(anno, modifier = Modifier.background(MaterialTheme.colors.surface).weight(1f), style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onSurface))
                Image(asset = vectorResource(id = arrowRes), modifier = Modifier.gravity(Alignment.CenterVertically), colorFilter = ColorFilter.tint(
                    MaterialTheme.colors.primary))
            }
        },
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            toggleModifier = Modifier.runCalcBorder()) {
            distances.forEach {
                DropdownMenuItem(onClick = {
                    selectedItem.value = it
                    expanded.value = false
                }) {
                    Text(text = it.first)
                }
            }
        }
    }
}