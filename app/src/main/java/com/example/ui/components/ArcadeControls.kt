package com.example.ui.components

import android.view.MotionEvent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArcadeGold
import com.example.ui.theme.ArcadeSurface
import com.example.ui.theme.ArcadeSurfaceHighlight
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.HazardRed
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.viewmodel.CraneState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ArcadeControls(
    craneX: Float,
    craneState: CraneState,
    effectiveGripStrength: Float,
    onMoveStart: (Int) -> Unit,
    onMoveStop: () -> Unit,
    onDirectPosition: (Float) -> Unit,
    onDropTrigger: () -> Unit,
    onRestockPit: () -> Unit,
    onClaimFreeTokens: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIdle = craneState == CraneState.IDLE

    // Pulse animation for the DROP button
    val infiniteTransition = rememberInfiniteTransition(label = "drop_button_glow")
    val buttonGlow by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .border(2.dp, Brush.horizontalGradient(listOf(CyberCyan, NeonPink)), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        color = ArcadeSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Bar: Win Probability Gauge & Restock Pit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Effective Grip Chance Meter
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "GRIP CHANCE:",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${effectiveGripStrength.toInt()}%",
                            color = when {
                                effectiveGripStrength >= 80f -> NeonGreen
                                effectiveGripStrength >= 60f -> ArcadeGold
                                else -> CyberCyan
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (effectiveGripStrength / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = when {
                            effectiveGripStrength >= 80f -> NeonGreen
                            effectiveGripStrength >= 60f -> ArcadeGold
                            else -> CyberCyan
                        },
                        trackColor = Color(0xFF1E293B)
                    )
                }

                // Restock button
                Button(
                    onClick = onRestockPit,
                    colors = ButtonDefaults.buttonColors(containerColor = ArcadeSurfaceHighlight),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    enabled = isIdle,
                    modifier = Modifier.testTag("restock_pit_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restock Pit",
                        tint = CyberCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Shake Pit",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Joystick & Big Arcade Push Button Area
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Horizontal Motion Controls: Left & Right tactile hold buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT ARROW BUTTON (Press and hold)
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF334155), Color(0xFF0F172A))
                                )
                            )
                            .border(2.dp, CyberCyan, CircleShape)
                            .pointerInteropFilter { motionEvent ->
                                if (isIdle) {
                                    when (motionEvent.action) {
                                        MotionEvent.ACTION_DOWN -> onMoveStart(-1)
                                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onMoveStop()
                                    }
                                }
                                true
                            }
                            .testTag("joystick_left_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Move Claw Left",
                            tint = if (isIdle) CyberCyan else Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // RIGHT ARROW BUTTON (Press and hold)
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF334155), Color(0xFF0F172A))
                                )
                            )
                            .border(2.dp, CyberCyan, CircleShape)
                            .pointerInteropFilter { motionEvent ->
                                if (isIdle) {
                                    when (motionEvent.action) {
                                        MotionEvent.ACTION_DOWN -> onMoveStart(1)
                                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onMoveStop()
                                    }
                                }
                                true
                            }
                            .testTag("joystick_right_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Move Claw Right",
                            tint = if (isIdle) CyberCyan else Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                // BIG GLOWING ARCADE "DROP" BUTTON
                Box(
                    modifier = Modifier
                        .scale(if (isIdle) buttonGlow else 0.95f)
                        .size(80.dp)
                        .clip(CircleShape)
                        .shadow(12.dp, CircleShape, spotColor = if (isIdle) HazardRed else Color.DarkGray)
                        .background(
                            Brush.radialGradient(
                                colors = if (isIdle) listOf(
                                    Color(0xFFFF5252),
                                    Color(0xFFDC2626),
                                    Color(0xFF7F1D1D)
                                ) else listOf(
                                    Color(0xFF475569),
                                    Color(0xFF1E293B)
                                )
                            )
                        )
                        .border(3.dp, if (isIdle) ArcadeGold else Color.Gray, CircleShape)
                        .clickable(enabled = isIdle) { onDropTrigger() }
                        .testTag("arcade_drop_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isIdle) "DROP" else craneState.name,
                            color = Color.White,
                            fontSize = if (isIdle) 16.sp else 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        if (isIdle) {
                            Text(
                                text = "10 COINS",
                                color = ArcadeGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Precision Position Touch Slider (Drag to target directly!)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Touch-Slide to Target",
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "Drop Chute ←   → Pit End",
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(14.dp))
                        .pointerInput(isIdle) {
                            if (isIdle) {
                                detectTapGestures { offset ->
                                    val frac = (offset.x / size.width).coerceIn(0.12f, 0.92f)
                                    onDirectPosition(frac)
                                }
                            }
                        }
                        .pointerInput(isIdle) {
                            if (isIdle) {
                                detectDragGestures { change, _ ->
                                    val frac = (change.position.x / size.width).coerceIn(0.12f, 0.92f)
                                    onDirectPosition(frac)
                                }
                            }
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Position cursor indicator
                    val indicatorWidth = 36.dp
                    Box(
                        modifier = Modifier
                            .padding(start = ((craneX.coerceIn(0.12f, 0.92f) * 280).dp))
                            .size(indicatorWidth, 22.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.horizontalGradient(listOf(CyberCyan, NeonPink)))
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "|||",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
