package com.bentrengrove.runcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
    val screen = remember { mutableStateOf<AppScreen>(AppScreen.TimeToPace) }
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
            Crossfade(current = screen.value) { screen ->
                screen.content(Modifier)
            }
        }
    }
}

@Composable
private fun DrawerRow(appScreen: AppScreen, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else Color.Transparent
    val textColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
    ListItem(modifier = Modifier.clickable(onClick = onClick).background(background)) {
        Text(color = textColor, text = appScreen.title)
    }
}

private val ALL_SCREENS = listOf(AppScreen.TimeToPace, AppScreen.PaceToTime)
private sealed class AppScreen(val title: String, val content: @Composable (modifier: Modifier) -> Unit) {
    object TimeToPace: AppScreen("Pace Calculator", { modifier -> TimeToPaceCalculator(modifier) })
    object PaceToTime: AppScreen("Time Calculator", { modifier -> PaceToTimeCalculator(modifier) })
}