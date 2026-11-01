package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ArcadeCabinetCanvas
import com.example.ui.components.ArcadeControls
import com.example.ui.components.ArcadeTopBar
import com.example.ui.components.WinPrizeDialog
import com.example.ui.theme.ArcadeBackground
import com.example.ui.theme.CyberCyan
import com.example.viewmodel.ClawMachineViewModel

@Composable
fun MachineScreen(
    viewModel: ClawMachineViewModel,
    modifier: Modifier = Modifier
) {
    val coins by viewModel.repository.coins.collectAsState()
    val tickets by viewModel.repository.tickets.collectAsState()
    val equippedClawId by viewModel.repository.equippedClawId.collectAsState()
    val equippedClaw = viewModel.repository.getEquippedClaw()

    val craneX by viewModel.craneX.collectAsState()
    val clawY by viewModel.clawY.collectAsState()
    val clawProngOpen by viewModel.clawProngOpen.collectAsState()
    val craneState by viewModel.craneState.collectAsState()
    val pitPrizes by viewModel.pitPrizes.collectAsState()
    val recentWonPrize by viewModel.recentWonPrize.collectAsState()
    val confetti by viewModel.confetti.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    val effectiveGrip = viewModel.repository.calculateEffectiveGripStrength()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArcadeBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Marquee Status Bar
            ArcadeTopBar(
                coins = coins,
                tickets = tickets,
                equippedClaw = equippedClaw,
                onClaimFreeCoins = {
                    val amount = viewModel.repository.claimFreeTokens()
                    viewModel.soundManager.playCoinInsert()
                }
            )

            // Status message toast
            AnimatedVisibility(
                visible = statusMessage != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                statusMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E293B).copy(alpha = 0.9f))
                            .border(1.dp, CyberCyan.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .clickable { viewModel.clearStatusMessage() }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = msg,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Main Arcade Cabinet Display (Takes available space)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                ArcadeCabinetCanvas(
                    craneX = craneX,
                    clawY = clawY,
                    clawProngOpen = clawProngOpen,
                    equippedClaw = equippedClaw,
                    pitPrizes = pitPrizes,
                    repository = viewModel.repository,
                    confetti = confetti,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Arcade Controls (Joystick, Buttons, Slider, Grip Gauge)
            ArcadeControls(
                craneX = craneX,
                craneState = craneState,
                effectiveGripStrength = effectiveGrip,
                onMoveStart = { dir -> viewModel.startMove(dir) },
                onMoveStop = { viewModel.stopMove() },
                onDirectPosition = { pos -> viewModel.setCranePositionDirect(pos) },
                onDropTrigger = { viewModel.triggerDrop() },
                onRestockPit = {
                    viewModel.soundManager.playButtonClick()
                    viewModel.restockPit()
                },
                onClaimFreeTokens = {
                    viewModel.repository.claimFreeTokens()
                    viewModel.soundManager.playCoinInsert()
                }
            )
        }

        // Win Prize Modal Dialog
        recentWonPrize?.let { (prizeType, rewards) ->
            WinPrizeDialog(
                prizeType = prizeType,
                rewardCoins = rewards.first,
                rewardTickets = rewards.second,
                onDismiss = { viewModel.dismissWinDialog() }
            )
        }
    }
}
