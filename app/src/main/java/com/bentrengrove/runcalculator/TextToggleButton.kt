package com.bentrengrove.runcalculator

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun TextToggleGroup(
        items: List<String>,
        selectedIndex: Int,
        onSelectedChanged: (Int) -> Unit,
        modifier: Modifier = Modifier
) {
    Row(modifier) {
        items.forEachIndexed { index, value ->
            val onCheckedChanged: (Boolean) -> Unit = {
                if (it) {
                    onSelectedChanged(index)
                }
            }
            val cornerSize = 4.dp
            val borderShape = if (index == 0) {
                RoundedCornerShape(topLeft = cornerSize, bottomLeft = cornerSize)
            } else if (index == items.size-1) {
                RoundedCornerShape(topRight = cornerSize, bottomRight = cornerSize)
            } else {
                RectangleShape
            }
            TextToggleButton(checked = index == selectedIndex, onCheckedChange = onCheckedChanged, borderShape = borderShape, text = value)
        }
    }
}

@Composable
private fun TextToggleButton(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        borderShape: Shape = RectangleShape,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        text: String
) {
    val selectedIndication = if (checked) Modifier.border(1.dp, MaterialTheme.colors.primary, borderShape) else Modifier.border(1.dp, MaterialTheme.colors.onBackground.copy(alpha = 0.2f), borderShape)
    val textStyle = if (checked) MaterialTheme.typography.button.copy(color = MaterialTheme.colors.primary) else MaterialTheme.typography.button.copy(color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.background).copy(alpha = 0.7f))
    val background = if (checked) MaterialTheme.colors.primarySurface.copy(alpha = 0.2f) else MaterialTheme.colors.background
    Box(
            modifier = modifier.toggleable(
                    value = checked,
                    onValueChange = onCheckedChange,
                    enabled = enabled,
                    indication = RippleIndication(bounded = true)
            )
                    .then(selectedIndication)
                    .background(background, borderShape)
                    .padding(8.dp)
                    .wrapContentSize(),
            gravity = ContentGravity.Center,
            children = { Text(text, style = textStyle) }
    )
}