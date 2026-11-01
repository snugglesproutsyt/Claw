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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MonetizationOn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MissionItem
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
fun MissionsScreen(
    viewModel: ClawMachineViewModel,
    modifier: Modifier = Modifier
) {
    val coins by viewModel.repository.coins.collectAsState()
    val tickets by viewModel.repository.tickets.collectAsState()
    val missions by viewModel.repository.missions.collectAsState()
    val totalPlays by viewModel.repository.totalPlays.collectAsState()
    val totalWins by viewModel.repository.totalWins.collectAsState()
    val totalCoinsEarned by viewModel.repository.totalCoinsEarned.collectAsState()
    val equippedClaw = viewModel.repository.getEquippedClaw()

    val winRate = if (totalPlays > 0) (totalWins.toFloat() / totalPlays.toFloat() * 100f).toInt() else 0

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
                // Career Statistics Card
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = ArcadeSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Brush.horizontalGradient(listOf(CyberCyan, NeonPink)), RoundedCornerShape(18.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Leaderboard,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "ARCADE CAREER RECORD",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatBox("Total Drops", "$totalPlays", CyberCyan)
                            StatBox("Prizes Won", "$totalWins", NeonGreen)
                            StatBox("Win Rate", "$winRate%", ArcadeGold)
                            StatBox("Coins Earned", "$totalCoinsEarned", NeonPink)
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = ArcadeGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ARCADE MISSIONS",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }

            items(missions) { mission ->
                MissionCard(
                    mission = mission,
                    onClaim = {
                        val claimed = viewModel.repository.claimMissionReward(mission.id)
                        if (claimed) {
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
private fun StatBox(label: String, value: String, accentColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = accentColor, fontSize = 16.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MissionCard(
    mission: MissionItem,
    onClaim: () -> Unit
) {
    val isReadyToClaim = mission.isCompleted && !mission.isClaimed

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ArcadeSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isReadyToClaim) NeonGreen else if (mission.isClaimed) Color(0xFF334155) else Color(0xFF475569),
                RoundedCornerShape(16.dp)
            )
            .testTag("mission_card_${mission.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mission.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                // Reward badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = ArcadeGold, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "+${mission.rewardCoins}", color = ArcadeGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    if (mission.rewardTickets > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ConfirmationNumber, contentDescription = null, tint = NeonPink, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "+${mission.rewardTickets}", color = NeonPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = mission.description,
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LinearProgressIndicator(
                    progress = { (mission.currentCount.toFloat() / mission.targetCount.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isReadyToClaim) NeonGreen else CyberCyan,
                    trackColor = Color(0xFF1E293B)
                )

                Text(
                    text = "${mission.currentCount} / ${mission.targetCount}",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Claim Button
            if (mission.isClaimed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ArcadeSurfaceHighlight),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "CLAIMED", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (isReadyToClaim) {
                Button(
                    onClick = onClaim,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .testTag("claim_reward_${mission.id}")
                ) {
                    Text(text = "CLAIM REWARD", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F172A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "IN PROGRESS", color = Color(0xFF64748B), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
