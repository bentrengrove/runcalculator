package com.bentrengrove.runcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.bentrengrove.runcalculator.ui.RunCalculatorTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}

@Composable
fun App() {
    val screen = remember { mutableStateOf<AppScreen>(AppScreen.TimeToPace)}
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    RunCalculatorTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(screen.value.title) }, navigationIcon = {
                    Icon(
                        Icons.Default.Menu,
                        modifier = Modifier.clickable(onClick = {
                            scaffoldState.drawerState.open()
                        })
                    )
                })
            },
            drawerContent = {
                Text(text = "Run Calculator", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
                ALL_SCREENS.forEach { appScreen ->
                    DrawerRow(
                        appScreen = appScreen,
                        selected = appScreen == screen.value,
                        onClick = {
                            screen.value = appScreen
                            scaffoldState.drawerState.close()
                        })
                }
            },
            scaffoldState = scaffoldState
        ) {
            screen.value.content()
        }
    }
}

@Composable
private fun DrawerRow(appScreen: AppScreen, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else Color.Transparent
    val textColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
    Text(appScreen.title,
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(background)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        style = MaterialTheme.typography.body2.copy(color = textColor, textAlign = TextAlign.Left)
    )
}

private val ALL_SCREENS = listOf(AppScreen.TimeToPace, AppScreen.PaceToTime)
private sealed class AppScreen(val title: String, val content: @Composable () -> Unit) {
    object TimeToPace: AppScreen("Pace Calculator", { TimeToPaceCalculator() })
    object PaceToTime: AppScreen("Time Calculator", { Text("Time To Pace") })
}