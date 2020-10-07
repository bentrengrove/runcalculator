package com.bentrengrove.runcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.bentrengrove.runcalculator.ui.RunCalculatorTheme
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

val distances = listOf(
        "800m" to 0.8f,
        "1500m" to 1.5f,
        "5 km" to 5f,
        "10 km" to 10f,
        "Half Marathon" to 21.1f,
        "Marathon" to 42.2f
)

@Preview
@Composable
fun AppPreview() {
    App()
}

@Composable
fun App() {
    RunCalculatorTheme {
        Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Pace Calculator") })
                },
                drawerContent = {
                    Text(text = "Pace Calculator")
                    Text(text = "Time Calculator")
                }
        ) {
            TimeToPaceCalculator()
        }
    }
}

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
        PaceResultText(timeInSeconds = timeInSeconds, distanceInKm = selectedItem.value.second, modifier = Modifier.wrapContentSize().align(Alignment.Start))
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
                Image(vectorResource(id = R.drawable.ic_baseline_backspace_24), colorFilter = ColorFilter.tint(MaterialTheme.colors.primary))
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
                Image(asset = vectorResource(id = arrowRes), modifier = Modifier.gravity(Alignment.CenterVertically), colorFilter = ColorFilter.tint(MaterialTheme.colors.primary))
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

@Composable
fun PaceResultText(timeInSeconds: Int, distanceInKm: Float, modifier: Modifier = Modifier) {
    val selectedUnit = remember { mutableStateOf(DistanceUnit.KILOMETERS) }
    
    val displayText = if (selectedUnit.value == DistanceUnit.KILOMETERS) {
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
        Text(text = displayText, style = MaterialTheme.typography.h3, modifier = Modifier.gravity(Alignment.CenterHorizontally))
    }
}

@Preview
@Composable
fun PaceResultTextPreview() {
    PaceResultText(timeInSeconds = 6300, distanceInKm = 21.1f)
}

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