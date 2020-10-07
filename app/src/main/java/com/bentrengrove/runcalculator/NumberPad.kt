package com.bentrengrove.runcalculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview

@Composable
fun NumberPad(modifier: Modifier = Modifier, onClick: (Int) -> Unit, onDelete: () -> Unit) {
    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            NumberPadButton(number = 7, modifier = Modifier.weight(1f), onClick = onClick)
            NumberPadButton(number = 8, modifier = Modifier.weight(1f), onClick = onClick)
            NumberPadButton(number = 9, modifier = Modifier.weight(1f), onClick = onClick)
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            NumberPadButton(number = 4, modifier = Modifier.weight(1f), onClick = onClick)
            NumberPadButton(number = 5, modifier = Modifier.weight(1f), onClick = onClick)
            NumberPadButton(number = 6, modifier = Modifier.weight(1f), onClick = onClick)
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            NumberPadButton(number = 1, modifier = Modifier.weight(1f), onClick = onClick)
            NumberPadButton(number = 2, modifier = Modifier.weight(1f), onClick = onClick)
            NumberPadButton(number = 3, modifier = Modifier.weight(1f), onClick = onClick)
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            NumberPadButton(number = 0, modifier = Modifier.weight(1f), onClick = onClick)
            IconButton(modifier = Modifier.weight(1f), onClick = onDelete) {
                Image(
                    vectorResource(id = R.drawable.ic_baseline_backspace_24), colorFilter = ColorFilter.tint(
                        MaterialTheme.colors.primary))
            }
        }
    }
}

@Composable
fun NumberPadButton(number: Int, modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    TextButton(onClick = { onClick(number) }, modifier = modifier) {
        Text(number.toString(), style = MaterialTheme.typography.h5)
    }
}

@Preview
@Composable
fun NumberPadPreview() {
    NumberPad(onClick = {}, onDelete = {})
}