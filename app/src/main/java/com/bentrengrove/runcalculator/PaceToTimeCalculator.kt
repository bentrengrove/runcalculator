package com.bentrengrove.runcalculator

import android.text.format.DateUtils
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
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
import kotlin.math.roundToInt

@Composable
fun PaceToTimeCalculator(modifier: Modifier = Modifier) {
    ScrollableColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        val paceString = remember { mutableStateOf("") }
        val selectedUnit = remember { mutableStateOf(DistanceUnit.KILOMETERS) }

        PaceEntry(paceString = paceString, selectedUnit = selectedUnit)
        Divider()

        val selectedItem = remember { mutableStateOf(distances.first()) }
        DistanceSelector(selectedItem = selectedItem)
        Divider()

        val displayText = formatTimeString(paceString.value)
        val timeInSeconds = convertFormattedTimeStringToSeconds(displayText)
        TimeResultText(paceInSeconds = timeInSeconds, distanceInKm = selectedItem.value.second, selectedUnit, modifier = Modifier.wrapContentSize().align(
                Alignment.Start))
    }
}

@Composable
fun PaceEntry(paceString: MutableState<String>, selectedUnit: MutableState<DistanceUnit>) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = "Pace", style = MaterialTheme.typography.overline)
        val timeValue = paceString.value
        val displayText = formatPaceString(timeValue, selectedUnit.value)
        Column(Modifier.runCalcBorder()) {
            Text(text = displayText, style = MaterialTheme.typography.h3.copy(textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.preferredHeight(8.dp))
            UnitSelector(selectedUnit.value, { selectedUnit.value = it }, Modifier.gravity(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(4.dp))
            NumberPad(modifier = Modifier.fillMaxWidth(), onClick = {
                paceString.value = paceString.value + it.toString()
            }, onDelete = {
                if (paceString.value.isNotEmpty()) {
                    paceString.value = paceString.value.substring(0, paceString.value.length-1)
                }
            })
        }
    }
}

@Composable
fun TimeResultText(paceInSeconds: Int, distanceInKm: Float, selectedUnit: MutableState<DistanceUnit>, modifier: Modifier = Modifier) {
    val displayText = if (distanceInKm == 0f) {
        "??:??"
    } else if (selectedUnit.value == DistanceUnit.KILOMETERS) {
        val totalSeconds = paceInSeconds * distanceInKm
        DateUtils.formatElapsedTime(totalSeconds.toLong())
    } else {
        val totalSeconds = paceInSeconds * (distanceInKm * 0.621371f)
        DateUtils.formatElapsedTime(totalSeconds.toLong())
    }

    Column(modifier = modifier.padding(vertical = 16.dp).fillMaxWidth()) {
        Text(text = "Time", style = MaterialTheme.typography.overline)
        Spacer(modifier = Modifier.preferredHeight(8.dp))
        Text(text = displayText, style = MaterialTheme.typography.h3, modifier = Modifier.gravity(
                Alignment.CenterHorizontally))
    }
}

fun formatPaceString(paceString: String, selectedUnit: DistanceUnit): String {
    val displayBuilder = StringBuilder(paceString)
    if (paceString.length >= 3) {
        displayBuilder.insert(displayBuilder.length - 2, ":")
    } else {
        displayBuilder.insert(0, "0:")
    }

    if (paceString.isNotEmpty()) {
        if (paceString.length == 1) {
            displayBuilder.insert(displayBuilder.length - 1, "0")
        }
    } else {
        displayBuilder.append("00")
    }

    when (selectedUnit) {
        DistanceUnit.KILOMETERS -> displayBuilder.append(" min/km")
        DistanceUnit.MILES -> displayBuilder.append(" min/mile")
    }

    return displayBuilder.toString()
}