package com.bentrengrove.runcalculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun TimeToPaceCalculator() {
    ScrollableColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        val timeString = remember { mutableStateOf("") }
        TimeEntry(timeString = timeString)
        Divider()

        val selectedItem = remember { mutableStateOf(distances.first()) }
        DistanceSelector(selectedItem = selectedItem)
        Divider()

        val displayText = formatTimeString(timeString.value)
        val timeInSeconds = convertFormattedTimeStringToSeconds(displayText)
        PaceResultText(timeInSeconds = timeInSeconds, distanceInKm = selectedItem.value.second, modifier = Modifier.wrapContentSize().align(
            Alignment.Start))
    }
}

@Composable
fun Modifier.runCalcBorder() = border(BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(alpha = 0.2f)), RoundedCornerShape(4.dp))

@Composable
fun TimeEntry(timeString: MutableState<String>) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = "Time", style = MaterialTheme.typography.overline)
        val timeValue = timeString.value
        val displayText = formatTimeString(timeValue)
        Text(text = displayText, style = MaterialTheme.typography.h3.copy(textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth().runCalcBorder())
        Spacer(modifier = Modifier.height(4.dp))
        NumberPad(modifier = Modifier.fillMaxWidth().runCalcBorder(), onClick = {
            timeString.value = timeString.value + it.toString()
        }, onDelete = {
            if (timeString.value.isNotEmpty()) {
                timeString.value = timeString.value.substring(0, timeString.value.length-1)
            }
        })
    }
}

@Composable
fun PaceResultText(timeInSeconds: Int, distanceInKm: Float, modifier: Modifier = Modifier) {
    val selectedUnit = remember { mutableStateOf(DistanceUnit.KILOMETERS) }

    val displayText = if (distanceInKm == 0f) {
        "??:??"
    } else if (selectedUnit.value == DistanceUnit.KILOMETERS) {
        val pace = timeInSeconds / distanceInKm
        val seconds = pace % 60
        val minutes = (pace - seconds) / 60

        String.format("%d:%02d min/km", minutes.toInt(), seconds.roundToInt())
    } else {
        val pace = timeInSeconds / (distanceInKm * 0.621371f)
        val seconds = pace % 60
        val minutes = (pace - seconds) / 60

        String.format("%d:%02d min/mile", minutes.toInt(), seconds.roundToInt())
    }

    Column(modifier = modifier.padding(vertical = 16.dp).fillMaxWidth()) {
        Text(text = "Pace", style = MaterialTheme.typography.overline)
        Spacer(modifier = Modifier.preferredHeight(8.dp))
        UnitSelector(selectedUnit.value, { selectedUnit.value = it }, Modifier.gravity(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.preferredHeight(8.dp))
        Text(text = displayText, style = MaterialTheme.typography.h3, modifier = Modifier.gravity(
            Alignment.CenterHorizontally))
    }
}

@Preview
@Composable
fun PaceResultTextPreview() {
    PaceResultText(timeInSeconds = 6300, distanceInKm = 21.1f)
}

fun formatTimeString(timeString: String): String {
    val displayBuilder = StringBuilder(timeString)
    if (timeString.length >= 5) {
        if (timeString.length == 5) {
            displayBuilder.insert(0, "0")
        }
        displayBuilder.insert(displayBuilder.length - 4, "h ")
    } else {
        displayBuilder.insert(0, "00h ")
    }
    if (timeString.length >= 3) {
        if (timeString.length == 3) {
            displayBuilder.insert(displayBuilder.length - 3, "0")
        }
        displayBuilder.insert(displayBuilder.length - 2, "m ")

    } else {
        displayBuilder.insert("00h ".length, "00m ")
    }
    if (timeString.isNotEmpty()) {
        if (timeString.length == 1) {
            displayBuilder.insert(displayBuilder.length - 1, "0")
        }
        displayBuilder.append("s")
    } else {
        displayBuilder.append("00s")
    }

    return displayBuilder.toString()
}

fun convertFormattedTimeStringToSeconds(formattedTimeString: String): Int {
    var timeInSeconds = 0

    val displayComponents = formattedTimeString.split(" ").toMutableList()
    if (displayComponents[0].contains("h")) {
        val hoursString = displayComponents[0].substringBefore("h")
        timeInSeconds += ((hoursString.toIntOrNull() ?: 0) * 60 * 60)
        displayComponents.removeAt(0)
    }
    if (displayComponents[0].contains("m")) {
        val minutesString = displayComponents[0].substringBefore("m")
        timeInSeconds += ((minutesString.toIntOrNull() ?: 0) * 60)
        displayComponents.removeAt(0)
    }
    if (displayComponents[0].contains("s")) {
        val secondsStrings = displayComponents[0].substringBefore("s")
        timeInSeconds += secondsStrings.toIntOrNull() ?: 0
        displayComponents.removeAt(0)
    }

    return timeInSeconds
}