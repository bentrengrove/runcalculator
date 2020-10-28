package com.bentrengrove.runcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.*
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
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

    RunCalculatorTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(SCREEN_MAP[currentRoute]?.title ?: "") }, navigationIcon = {
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
                ALL_SCREENS.forEach { screen ->
                    DrawerRow(
                        title = screen.title,
                        selected = currentRoute == screen.route,
                        onClick = {
                            // This is the equivalent to popUpTo the start destination
                            navController.popBackStack(navController.graph.startDestination, false)

                            // This if check gives us a "singleTop" behavior where we do not create a
                            // second instance of the composable if we are already on that destination
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route)
                            }

                            scaffoldState.drawerState.close()
                        })
                }
            },
            scaffoldState = scaffoldState
        ) {
            NavHost(navController = navController, startDestination = AppScreen.TimeToPace.route) {
                ALL_SCREENS.forEach { composable(it.route, content = it.content) }
            }
        }
    }
}

@Composable
private fun DrawerRow(title: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else Color.Transparent
    val textColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
    ListItem(modifier = Modifier.clickable(onClick = onClick).background(background)) {
        Text(color = textColor, text = title)
    }
}

private sealed class AppScreen(val route: String, val title: String, val content: @Composable (backStackEntry: NavBackStackEntry) -> Unit) {
    object TimeToPace: AppScreen("timetopace", "Pace Calculator", { backStackEntry -> TimeToPaceCalculator(backStackEntry) })
    object PaceToTime: AppScreen("pacetotime", "Time Calculator", { backStackEntry -> PaceToTimeCalculator(backStackEntry) })
}
private val ALL_SCREENS = listOf(AppScreen.TimeToPace, AppScreen.PaceToTime)
private val SCREEN_MAP = ALL_SCREENS.associateBy { it.route }