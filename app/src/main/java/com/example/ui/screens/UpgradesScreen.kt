package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UpgradeType
import com.example.ui.components.ArcadeTopBar
import com.example.ui.theme.ArcadeBackground
import com.example.ui.theme.ArcadeGold
import com.example.ui.theme.ArcadeSurface
import com.example.ui.theme.ArcadeSurfaceHighlight
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.viewmodel.ClawMachineViewModel

@Composable
fun UpgradesScreen(
    viewModel: ClawMachineViewModel,
    modifier: Modifier = Modifier
) {
    val coins by viewModel.repository.coins.collectAsState()
    val tickets by viewModel.repository.tickets.collectAsState()
    val upgradeLevels by viewModel.repository.upgradeLevels.collectAsState()
    val equippedClaw = viewModel.repository.getEquippedClaw()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArcadeBackground)
    ) {
        // Top Marquee Status Bar
        ArcadeTopBar(
            coins = coins,
            tickets = tickets,
            equippedClaw = equippedClaw,
            onClaimFreeCoins = {
                viewModel.repository.claimFreeTokens()
                viewModel.soundManager.playCoinInsert()
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = ArcadeGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "MACHINE WORKSHOP",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Upgrade machine parts to maximize your winning odds",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            items(UpgradeType.values()) { upgradeType ->
                val currentLevel = upgradeLevels[upgradeType.id] ?: 0
                val cost = viewModel.repository.getUpgradeCost(upgradeType)
                val isMaxLevel = currentLevel >= upgradeType.maxLevel
                val canAfford = coins >= cost && !isMaxLevel

                UpgradeCard(
                    upgradeType = upgradeType,
                    currentLevel = currentLevel,
                    cost = cost,
                    isMaxLevel = isMaxLevel,
                    canAfford = canAfford,
                    onUpgrade = {
                        val success = viewModel.repository.upgradeMachine(upgradeType)
                        if (success) {
                            viewModel.soundManager.playUpgradePurchase()
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun UpgradeCard(
    upgradeType: UpgradeType,
    currentLevel: Int,
    cost: Int,
    isMaxLevel: Boolean,
    canAfford: Boolean,
    onUpgrade: () -> Unit
) {
    val icon = when (upgradeType) {
        UpgradeType.GRIP_PRESSURE -> Icons.Default.TrendingUp
        UpgradeType.GANTRY_MOTOR -> Icons.Default.FastForward
        UpgradeType.LASER_GUIDE -> Icons.Default.FilterCenterFocus
        UpgradeType.CHUTE_FUNNEL -> Icons.Default.VerticalAlignBottom
        UpgradeType.ANTI_SLIP_DAMPER -> Icons.Default.Shield
        UpgradeType.FORTUNE_LUCK -> Icons.Default.Star
        UpgradeType.COIN_MULTIPLIER -> Icons.Default.MonetizationOn
    }

    val accentColor = when (upgradeType) {
        UpgradeType.GRIP_PRESSURE -> NeonGreen
        UpgradeType.GANTRY_MOTOR -> CyberCyan
        UpgradeType.LASER_GUIDE -> NeonPink
        UpgradeType.CHUTE_FUNNEL -> ArcadeGold
        UpgradeType.ANTI_SLIP_DAMPER -> Color(0xFF38BDF8)
        UpgradeType.FORTUNE_LUCK -> Color(0xFFA855F7)
        UpgradeType.COIN_MULTIPLIER -> Color(0xFFFDE047)
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ArcadeSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.5f), Color(0xFF334155))), RoundedCornerShape(18.dp))
            .testTag("upgrade_card_${upgradeType.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.2f))
                            .border(1.5.dp, accentColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = upgradeType.title,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = upgradeType.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = upgradeType.statLabel,
                            color = accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Level Tag (e.g. Lv 3/10)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ArcadeSurfaceHighlight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isMaxLevel) "MAX" else "LV $currentLevel / ${upgradeType.maxLevel}",
                        color = if (isMaxLevel) ArcadeGold else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = upgradeType.description,
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Level Progress Bar
            LinearProgressIndicator(
                progress = { currentLevel.toFloat() / upgradeType.maxLevel.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = accentColor,
                trackColor = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button
            if (isMaxLevel) {
                Button(
                    onClick = { },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(disabledContainerColor = ArcadeSurfaceHighlight),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = ArcadeGold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "FULLY UPGRADED", color = ArcadeGold, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onUpgrade,
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canAfford) accentColor else Color(0xFF334155),
                        disabledContainerColor = Color(0xFF1E293B)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("upgrade_btn_${upgradeType.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = if (canAfford) Color.Black else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "UPGRADE TO LV ${currentLevel + 1} ($cost COINS)",
                        color = if (canAfford) Color.Black else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
