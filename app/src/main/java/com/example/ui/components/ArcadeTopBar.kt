package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.ArcadeGold
import com.example.ui.theme.ArcadeSurface
import com.example.ui.theme.ArcadeSurfaceHighlight
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonPink

@Composable
fun ArcadeTopBar(
    coins: Int,
    tickets: Int,
    equippedClaw: ClawModel,
    onClaimFreeCoins: () -> Unit,
    onOpenRedeem: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "marquee_bulbs")
    val bulbFlash by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flash"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .border(2.dp, Brush.horizontalGradient(listOf(NeonPink, CyberCyan)), RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
        color = ArcadeSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Row of marquee neon light bulbs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val bulbCount = 12
                for (i in 0 until bulbCount) {
                    val isActive = if (bulbFlash > 0.5f) (i % 2 == 0) else (i % 2 != 0)
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) {
                                    if (i % 3 == 0) ArcadeGold else if (i % 3 == 1) CyberCyan else NeonPink
                                } else {
                                    Color(0xFF334155)
                                }
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Bar: Title + Balances
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title & Equipped Claw badge
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CLAW",
                            color = ArcadeGold,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "MASTER",
                            color = CyberCyan,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "Claw: ${equippedClaw.name}",
                        color = Color(equippedClaw.primaryColorHex),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Tokens & Tickets badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Coins Pill with "+" dispenser button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ArcadeSurfaceHighlight)
                            .border(1.dp, ArcadeGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = ArcadeGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$coins",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(ArcadeGold)
                                .clickable { onClaimFreeCoins() }
                                .testTag("claim_free_coins_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Free Tokens",
                                tint = Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Tickets Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ArcadeSurfaceHighlight)
                            .border(1.dp, NeonPink.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = "Tickets",
                            tint = NeonPink,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$tickets",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Redeem Gift Code Button
                    onOpenRedeem?.let { openRedeem ->
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(ArcadeSurfaceHighlight)
                                .border(1.dp, CyberCyan.copy(alpha = 0.6f), CircleShape)
                                .clickable { openRedeem() }
                                .testTag("open_redeem_dialog_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = "Redeem Gift Code",
                                tint = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
