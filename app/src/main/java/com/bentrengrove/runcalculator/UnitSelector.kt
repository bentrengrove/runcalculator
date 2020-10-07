package com.bentrengrove.runcalculator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class DistanceUnit {
    KILOMETERS, MILES
}

@Composable
fun UnitSelector(selectedUnit: DistanceUnit, onSelectedChanged: (DistanceUnit) -> Unit, modifier: Modifier = Modifier) {
    val onSelectedIndexChanged: (Int) -> Unit = {
        onSelectedChanged(DistanceUnit.values()[it])
    }

    TextToggleGroup(items = listOf("Kilometers", "Miles"), selectedUnit.ordinal, onSelectedIndexChanged, modifier = modifier)
}