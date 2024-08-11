package com.example.nutritioncapture.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DataThresholding
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

enum class MainScreenTab(
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val route: String,
) {
    Home(
        icon = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home,
        route = "Home"
    ),
    Graph(
        icon = Icons.Outlined.DataThresholding,
        iconSelected = Icons.Filled.DataThresholding,
        route = "Graph"
    ),
    Settings(
        icon = Icons.Outlined.Settings,
        iconSelected = Icons.Filled.Settings,
        route = "Settings"
    )
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(MainScreenTab.Home) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.let { entry ->
        selectedItem = MainScreenTab.values().firstOrNull { it.route == entry.destination.route } ?: MainScreenTab.Home
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(80.dp)
            ) {
                MainScreenTab.values().forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == item) item.iconSelected else item.icon,
                                contentDescription = item.route,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.route,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(top = 0.dp)
                            )
                        },
                        selected = selectedItem == item,
                        onClick = {
                            selectedItem = item
                            navController.navigate(item.route) {
                                popUpTo(MainScreenTab.Home.route) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = MainScreenTab.Home.route) {
                composable(MainScreenTab.Home.route) {
                    HomeView()
                }
                composable(MainScreenTab.Graph.route) {
                    GraphView()
                }
                composable(MainScreenTab.Settings.route) {
                    SettingsView()
                }
            }
        }
    }
}