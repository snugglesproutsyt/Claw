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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.data.model.ClawModel
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
fun ClawArsenalScreen(
    viewModel: ClawMachineViewModel,
    modifier: Modifier = Modifier
) {
    val coins by viewModel.repository.coins.collectAsState()
    val tickets by viewModel.repository.tickets.collectAsState()
    val equippedClawId by viewModel.repository.equippedClawId.collectAsState()
    val unlockedClawIds by viewModel.repository.unlockedClawIds.collectAsState()
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
                        imageVector = Icons.Default.PanTool,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "CLAW ARSENAL",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Buy specialized claws with high grip & unique perks",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            items(viewModel.repository.clawCatalog) { claw ->
                val isUnlocked = unlockedClawIds.contains(claw.id)
                val isEquipped = claw.id == equippedClawId
                val canAfford = coins >= claw.costCoins && tickets >= claw.costTickets

                ClawCard(
                    claw = claw,
                    isUnlocked = isUnlocked,
                    isEquipped = isEquipped,
                    canAfford = canAfford,
                    onEquip = {
                        viewModel.soundManager.playButtonClick()
                        viewModel.repository.equipClaw(claw.id)
                    },
                    onBuy = {
                        val success = viewModel.repository.buyClaw(claw)
                        if (success) {
                            viewModel.soundManager.playUpgradePurchase()
                        } else {
                            viewModel.soundManager.playSlipMiss()
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
private fun ClawCard(
    claw: ClawModel,
    isUnlocked: Boolean,
    isEquipped: Boolean,
    canAfford: Boolean,
    onEquip: () -> Unit,
    onBuy: () -> Unit
) {
    val primaryColor = Color(claw.primaryColorHex)
    val accentColor = Color(claw.accentColorHex)

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ArcadeSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isEquipped) 2.5.dp else 1.dp,
                brush = if (isEquipped) {
                    Brush.horizontalGradient(listOf(ArcadeGold, CyberCyan))
                } else {
                    Brush.horizontalGradient(listOf(primaryColor.copy(alpha = 0.5f), Color(0xFF334155)))
                },
                shape = RoundedCornerShape(18.dp)
            )
            .testTag("claw_card_${claw.id}")
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
                // Left: Claw icon preview badge & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(primaryColor.copy(alpha = 0.2f))
                            .border(2.dp, primaryColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PanTool,
                            contentDescription = claw.name,
                            tint = accentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column {
                        Text(
                            text = claw.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = claw.subtitle,
                            color = accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Equipped / Status badge
                if (isEquipped) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ArcadeGold.copy(alpha = 0.2f))
                            .border(1.dp, ArcadeGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "EQUIPPED",
                            color = ArcadeGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = claw.description,
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row: Base Grip & Prongs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Grip bar
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Base Grip Strength",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "${claw.baseGripPercent}%",
                            color = primaryColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { claw.baseGripPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = primaryColor,
                        trackColor = Color(0xFF1E293B)
                    )
                }

                // Prongs Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ArcadeSurfaceHighlight)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${claw.prongsCount}-PRONGS",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Special Perk Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, primaryColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = claw.specialPerk,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action Row (Equip / Buy)
            if (isEquipped) {
                Button(
                    onClick = { /* already equipped */ },
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(disabledContainerColor = ArcadeSurfaceHighlight),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = ArcadeGold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Currently In Use", color = ArcadeGold, fontWeight = FontWeight.Bold)
                }
            } else if (isUnlocked) {
                Button(
                    onClick = onEquip,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("equip_claw_${claw.id}")
                ) {
                    Text(text = "EQUIP CLAW", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                }
            } else {
                // Buy Button with price
                Button(
                    onClick = onBuy,
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canAfford) NeonGreen else Color(0xFF334155),
                        disabledContainerColor = Color(0xFF1E293B)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("buy_claw_${claw.id}")
                ) {
                    Icon(
                        imageVector = if (canAfford) Icons.Default.ShoppingCart else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (canAfford) Color.Black else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "UNLOCK FOR ${claw.costCoins} COINS" + if (claw.costTickets > 0) " + ${claw.costTickets} TICKETS" else "",
                        color = if (canAfford) Color.Black else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
