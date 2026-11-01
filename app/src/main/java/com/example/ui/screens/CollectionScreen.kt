package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PrizeRarity
import com.example.data.model.PrizeType
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
fun CollectionScreen(
    viewModel: ClawMachineViewModel,
    modifier: Modifier = Modifier
) {
    val coins by viewModel.repository.coins.collectAsState()
    val tickets by viewModel.repository.tickets.collectAsState()
    val collection by viewModel.repository.prizeCollection.collectAsState()
    val equippedClaw = viewModel.repository.getEquippedClaw()

    var selectedPrize by remember { mutableStateOf<PrizeType?>(null) }

    val totalUnlockedTypes = PrizeType.values().count { (collection[it.name] ?: 0) > 0 }
    val totalPrizesWon = collection.values.sum()

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Header Stats banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ArcadeSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(ArcadeGold.copy(alpha = 0.2f))
                                .border(1.dp, ArcadeGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = ArcadeGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "TROPHY SHOWCASE",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "$totalUnlockedTypes / ${PrizeType.values().size} Unique Prizes Unlocked",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ArcadeSurfaceHighlight)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$totalPrizesWon WON",
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Grid of Prizes
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(PrizeType.values()) { prizeType ->
                    val count = collection[prizeType.name] ?: 0
                    PrizeCollectionCard(
                        prizeType = prizeType,
                        count = count,
                        onClick = { selectedPrize = prizeType }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }

        // Details / Sell Dialog
        selectedPrize?.let { prize ->
            val count = collection[prize.name] ?: 0
            PrizeDetailDialog(
                prizeType = prize,
                count = count,
                onDismiss = { selectedPrize = null },
                onSellDuplicate = {
                    val earned = viewModel.repository.sellDuplicatePrize(prize)
                    if (earned > 0) {
                        viewModel.soundManager.playCoinInsert()
                    }
                }
            )
        }
    }
}

@Composable
private fun PrizeCollectionCard(
    prizeType: PrizeType,
    count: Int,
    onClick: () -> Unit
) {
    val isUnlocked = count > 0
    val rarityColor = Color(prizeType.rarity.colorHex)
    val primaryColor = Color(prizeType.primaryColorHex)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ArcadeSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isUnlocked) rarityColor.copy(alpha = 0.5f) else Color(0xFF334155),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .testTag("prize_card_${prizeType.name}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Rarity & Count Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(rarityColor.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = prizeType.rarity.displayName.uppercase(),
                        color = rarityColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isUnlocked) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ArcadeSurfaceHighlight)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "x$count",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Prize Icon avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) primaryColor.copy(alpha = 0.25f) else Color(0xFF1E293B)
                    )
                    .border(
                        1.5.dp,
                        if (isUnlocked) primaryColor else Color(0xFF475569),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = prizeType.title,
                        tint = primaryColor,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isUnlocked) prizeType.title else "??? Unknown",
                color = if (isUnlocked) Color.White else Color(0xFF94A3B8),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isUnlocked) "${prizeType.rarity.baseValueCoins} Coins" else "Locked",
                color = if (isUnlocked) ArcadeGold else Color(0xFF64748B),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PrizeDetailDialog(
    prizeType: PrizeType,
    count: Int,
    onDismiss: () -> Unit,
    onSellDuplicate: () -> Unit
) {
    val isUnlocked = count > 0
    val rarityColor = Color(prizeType.rarity.colorHex)
    val sellPrice = (prizeType.rarity.baseValueCoins * 0.75f).toInt()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ArcadeSurface,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, rarityColor, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(rarityColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = prizeType.rarity.displayName.uppercase(),
                            color = rarityColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isUnlocked) prizeType.title else "Undiscovered Prize",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isUnlocked) prizeType.description else "Grab this toy from the claw machine pit to reveal its lore and value!",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats breakdown
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ArcadeSurfaceHighlight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Caught", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text(text = "x$count", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Base Value", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text(text = "${prizeType.rarity.baseValueCoins}c", color = ArcadeGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Tickets", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text(text = "${prizeType.rarity.baseValueTickets}t", color = NeonPink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sell Duplicate Button (only if count > 1)
                if (count > 1) {
                    Button(
                        onClick = onSellDuplicate,
                        colors = ButtonDefaults.buttonColors(containerColor = ArcadeGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Sell, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SELL 1 DUPLICATE (+$sellPrice COINS)",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text(text = "KEEP IN SHOWCASE", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
