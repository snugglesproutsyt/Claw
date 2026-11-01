package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ClawArsenalScreen
import com.example.ui.screens.CollectionScreen
import com.example.ui.screens.MachineScreen
import com.example.ui.screens.MissionsScreen
import com.example.ui.screens.UpgradesScreen
import com.example.ui.theme.ArcadeBackground
import com.example.ui.theme.ArcadeGold
import com.example.ui.theme.ArcadeSurface
import com.example.ui.theme.ArcadeSurfaceHighlight
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ClawMachineViewModel

enum class ArcadeTab(
    val title: String,
    val icon: ImageVector,
    val tag: String
) {
    PLAY("Arcade", Icons.Default.SportsEsports, "tab_play"),
    CLAWS("Claws", Icons.Default.PanTool, "tab_claws"),
    UPGRADES("Upgrades", Icons.Default.Build, "tab_upgrades"),
    COLLECTION("Prizes", Icons.Default.EmojiEvents, "tab_collection"),
    MISSIONS("Quests", Icons.Default.Assignment, "tab_missions")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: ClawMachineViewModel = viewModel()
                var currentTab by rememberSaveable { mutableStateOf(ArcadeTab.PLAY) }

                // BackHandler returns to the main arcade play screen from other tabs
                BackHandler(enabled = currentTab != ArcadeTab.PLAY) {
                    currentTab = ArcadeTab.PLAY
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ArcadeBackground),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        NavigationBar(
                            containerColor = ArcadeSurface,
                            contentColor = Color.White
                        ) {
                            ArcadeTab.values().forEach { tab ->
                                val isSelected = currentTab == tab
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.soundManager.playButtonClick()
                                        currentTab = tab
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.title,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = ArcadeGold,
                                        selectedTextColor = ArcadeGold,
                                        indicatorColor = ArcadeSurfaceHighlight,
                                        unselectedIconColor = Color(0xFF64748B),
                                        unselectedTextColor = Color(0xFF64748B)
                                    ),
                                    modifier = Modifier.testTag(tab.tag)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = currentTab, label = "tab_fade") { tab ->
                            when (tab) {
                                ArcadeTab.PLAY -> MachineScreen(viewModel = viewModel)
                                ArcadeTab.CLAWS -> ClawArsenalScreen(viewModel = viewModel)
                                ArcadeTab.UPGRADES -> UpgradesScreen(viewModel = viewModel)
                                ArcadeTab.COLLECTION -> CollectionScreen(viewModel = viewModel)
                                ArcadeTab.MISSIONS -> MissionsScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
