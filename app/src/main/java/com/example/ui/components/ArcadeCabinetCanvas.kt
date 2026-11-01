package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.model.ClawModel
import com.example.data.model.PrizeItem
import com.example.data.model.PrizeType
import com.example.data.model.UpgradeType
import com.example.data.repository.GameRepository
import com.example.ui.theme.ArcadeGold
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.NeonPink
import com.example.viewmodel.ConfettiParticle
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ArcadeCabinetCanvas(
    craneX: Float,
    clawY: Float,
    clawProngOpen: Float,
    equippedClaw: ClawModel,
    pitPrizes: List<PrizeItem>,
    repository: GameRepository,
    confetti: List<ConfettiParticle>,
    modifier: Modifier = Modifier
) {
    // Pulse animation for neon marquee and laser
    val infiniteTransition = rememberInfiniteTransition(label = "arcade_lights")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val laserUpgradeLevel = repository.getUpgradeLevel(UpgradeType.LASER_GUIDE)
    val hasLaser = equippedClaw.hasLaserPointer || laserUpgradeLevel > 0

    val chuteUpgradeLevel = repository.getUpgradeLevel(UpgradeType.CHUTE_FUNNEL)
    // Base chute width 18% of cabinet, +2% per upgrade level
    val chuteWidthFraction = (0.18f + (chuteUpgradeLevel * 0.02f)).coerceAtMost(0.30f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(3.dp, Brush.verticalGradient(listOf(CyberCyan, NeonPink)), RoundedCornerShape(16.dp))
            .background(Color(0xFF070913))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Machine Interior Background Gradient & Mirror Glass Backing
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF13172E),
                        Color(0xFF0F1224),
                        Color(0xFF090B16)
                    )
                ),
                size = size
            )

            // Neon Grid Floor at the bottom of the pit
            val pitFloorY = h * 0.96f
            val pitTopY = h * 0.65f
            for (i in 0..10) {
                val gridY = pitTopY + (i / 10f) * (pitFloorY - pitTopY)
                drawLine(
                    color = Color(0x228B5CF6),
                    start = Offset(0f, gridY),
                    end = Offset(w, gridY),
                    strokeWidth = 1f
                )
            }
            for (i in 1..8) {
                val gridX = (i / 9f) * w
                drawLine(
                    color = Color(0x1806B6D4),
                    start = Offset(gridX, pitTopY),
                    end = Offset(gridX, pitFloorY),
                    strokeWidth = 1f
                )
            }

            // 2. Prize Drop Chute on Bottom Left
            val chuteW = w * chuteWidthFraction
            val chuteH = h * 0.32f
            val chuteTopY = h - chuteH

            // Chute background (drop hole)
            drawRoundRect(
                color = Color(0xFF020408),
                topLeft = Offset(4f, chuteTopY),
                size = Size(chuteW - 8f, chuteH - 4f),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Chute Hazard Border / Funnel Lip
            val hazardBarHeight = 16f
            drawRect(
                color = Color(0xFFEAB308),
                topLeft = Offset(4f, chuteTopY),
                size = Size(chuteW - 8f, hazardBarHeight)
            )
            // Diagonal hazard stripes
            val stripeSpacing = 24f
            var sx = 4f
            while (sx < chuteW - 8f) {
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(sx, chuteTopY),
                    end = Offset((sx + 14f).coerceAtMost(chuteW - 8f), chuteTopY + hazardBarHeight),
                    strokeWidth = 6f
                )
                sx += stripeSpacing
            }

            // Glowing Chute Outline
            drawRoundRect(
                color = Color(0xFF06B6D4),
                topLeft = Offset(4f, chuteTopY),
                size = Size(chuteW - 8f, chuteH - 4f),
                cornerRadius = CornerRadius(12f, 12f),
                style = Stroke(width = 3f)
            )

            // Chute label indicator "WIN CHUTE"
            val arrowY = chuteTopY + (chuteH * 0.45f)
            val arrowX = chuteW * 0.45f
            val arrowPath = Path().apply {
                moveTo(arrowX - 16f, arrowY - 14f)
                lineTo(arrowX + 16f, arrowY - 14f)
                lineTo(arrowX + 16f, arrowY + 4f)
                lineTo(arrowX + 26f, arrowY + 4f)
                lineTo(arrowX, arrowY + 28f)
                lineTo(arrowX - 26f, arrowY + 4f)
                lineTo(arrowX - 16f, arrowY + 4f)
                close()
            }
            drawPath(
                path = arrowPath,
                color = Color(0xFF10B981).copy(alpha = pulseAlpha)
            )

            // 3. Draw Prizes lying inside the pit
            for (prize in pitPrizes) {
                val px = prize.x * w
                // Invert Y so 0.0 is floor, 1.0 is higher
                val py = pitFloorY - (prize.y * (pitFloorY - pitTopY)) - 24f
                drawPrizeGraphic(prize.type, px, py, prize.rotation, prize.isGrabbed)
            }

            // 4. Laser Guide Sight (if equipped or upgraded)
            val railY = 32f
            val clawTargetX = craneX * w
            val clawTargetY = railY + 20f + (clawY * (pitFloorY - railY - 60f))

            if (hasLaser) {
                val laserAlpha = (0.45f + (laserUpgradeLevel * 0.10f)).coerceAtMost(0.95f) * pulseAlpha
                // Laser line down to pit floor
                drawLine(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEC4899).copy(alpha = laserAlpha),
                            Color(0xFFF43F5E).copy(alpha = laserAlpha * 0.8f)
                        ),
                        startY = clawTargetY,
                        endY = pitFloorY
                    ),
                    start = Offset(clawTargetX, clawTargetY),
                    end = Offset(clawTargetX, pitFloorY),
                    strokeWidth = 2.5f
                )
                // Laser target reticle at floor
                drawCircle(
                    color = Color(0xFFF43F5E).copy(alpha = laserAlpha),
                    radius = 14f,
                    center = Offset(clawTargetX, pitFloorY - 10f),
                    style = Stroke(width = 2f)
                )
                drawCircle(
                    color = Color(0xFFF43F5E).copy(alpha = laserAlpha),
                    radius = 4f,
                    center = Offset(clawTargetX, pitFloorY - 10f)
                )
            }

            // 5. Magnetic Aura Pulses (if equipped claw has magnet)
            if (equippedClaw.hasMagneticPulse) {
                val auraRadius = 36f * pulseAlpha
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF06B6D4).copy(alpha = 0.35f * pulseAlpha),
                            Color.Transparent
                        ),
                        center = Offset(clawTargetX, clawTargetY + 20f),
                        radius = auraRadius
                    ),
                    center = Offset(clawTargetX, clawTargetY + 20f),
                    radius = auraRadius
                )
            }

            // 6. Cosmic Titan Glow (if Quantum Titan claw)
            if (equippedClaw.isTitanCosmic) {
                val titanRadius = 48f * pulseAlpha
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEC4899).copy(alpha = 0.40f * pulseAlpha),
                            Color(0xFF8B5CF6).copy(alpha = 0.25f * pulseAlpha),
                            Color.Transparent
                        ),
                        center = Offset(clawTargetX, clawTargetY + 20f),
                        radius = titanRadius
                    ),
                    center = Offset(clawTargetX, clawTargetY + 20f),
                    radius = titanRadius
                )
            }

            // 7. Steel Cable
            drawLine(
                color = Color(0xFFCBD5E1),
                start = Offset(clawTargetX, railY + 8f),
                end = Offset(clawTargetX, clawTargetY),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
            // Cable texture stripes
            val cableLength = clawTargetY - (railY + 8f)
            if (cableLength > 10f) {
                var cy = railY + 12f
                while (cy < clawTargetY - 4f) {
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(clawTargetX - 2f, cy),
                        end = Offset(clawTargetX + 2f, cy + 2f),
                        strokeWidth = 1.5f
                    )
                    cy += 8f
                }
            }

            // 8. Top Rail & Gantry Carriage
            // Background rail bar
            drawRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(0f, railY),
                size = Size(w, 14f)
            )
            drawLine(
                color = Color(0xFF475569),
                start = Offset(0f, railY + 14f),
                end = Offset(w, railY + 14f),
                strokeWidth = 2f
            )
            // Trolley Carriage
            val carriageW = 44f
            val carriageH = 18f
            drawRoundRect(
                color = Color(0xFF334155),
                topLeft = Offset(clawTargetX - (carriageW / 2f), railY - 2f),
                size = Size(carriageW, carriageH),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Wheels
            drawCircle(
                color = Color(0xFF0F172A),
                radius = 5f,
                center = Offset(clawTargetX - 14f, railY + 4f)
            )
            drawCircle(
                color = Color(0xFF0F172A),
                radius = 5f,
                center = Offset(clawTargetX + 14f, railY + 4f)
            )
            // Carriage Status LED
            drawCircle(
                color = if (clawY > 0.05f) Color(0xFFEF4444) else Color(0xFF10B981),
                radius = 3.5f,
                center = Offset(clawTargetX, railY + 6f)
            )

            // 9. Claw Mechanism (Hub and Prongs)
            drawClawMechanism(
                equippedClaw = equippedClaw,
                centerX = clawTargetX,
                centerY = clawTargetY,
                openFraction = clawProngOpen
            )

            // 10. Front Glass Reflection Accents (giving depth)
            val glassHighlight = Path().apply {
                moveTo(w * 0.85f, 0f)
                lineTo(w * 0.98f, 0f)
                lineTo(w * 0.75f, h)
                lineTo(w * 0.62f, h)
                close()
            }
            drawPath(
                path = glassHighlight,
                color = Color.White.copy(alpha = 0.04f)
            )

            // 11. Confetti Explosions
            for (p in confetti) {
                val cx = p.x * w
                val cy = p.y * h
                drawCircle(
                    color = Color(p.colorHex),
                    radius = p.size / 2f,
                    center = Offset(cx, cy)
                )
            }
        }
    }
}

// Function to draw the articulated mechanical claw
private fun DrawScope.drawClawMechanism(
    equippedClaw: ClawModel,
    centerX: Float,
    centerY: Float,
    openFraction: Float
) {
    val primaryColor = Color(equippedClaw.primaryColorHex)
    val accentColor = Color(equippedClaw.accentColorHex)

    // Hub Housing
    val hubW = 28f
    val hubH = 16f
    drawRoundRect(
        color = primaryColor,
        topLeft = Offset(centerX - (hubW / 2f), centerY),
        size = Size(hubW, hubH),
        cornerRadius = CornerRadius(6f, 6f)
    )
    drawRoundRect(
        color = accentColor,
        topLeft = Offset(centerX - (hubW / 4f), centerY + 3f),
        size = Size(hubW / 2f, hubH - 6f),
        cornerRadius = CornerRadius(3f, 3f)
    )

    // Center pivot joint
    val pivotY = centerY + hubH
    drawCircle(
        color = Color(0xFF334155),
        radius = 6f,
        center = Offset(centerX, pivotY)
    )
    drawCircle(
        color = accentColor,
        radius = 2.5f,
        center = Offset(centerX, pivotY)
    )

    // Claw Prongs geometry
    // openFraction: 0.0f (closed inward), 1.0f (flared wide)
    val prongLength = 34f * equippedClaw.reachMultiplier
    val angleSpread = 22f + (openFraction * 38f) // degrees outward

    // Left Prong
    val leftRad = Math.toRadians((180f - angleSpread).toDouble())
    val leftKneeX = centerX + (cos(leftRad) * prongLength * 0.65f).toFloat()
    val leftKneeY = pivotY - (sin(leftRad) * prongLength * 0.65f).toFloat() + 20f

    val leftTipRad = Math.toRadians((180f - angleSpread + 35f).toDouble())
    val leftTipX = leftKneeX + (cos(leftTipRad) * prongLength * 0.5f).toFloat()
    val leftTipY = leftKneeY - (sin(leftTipRad) * prongLength * 0.5f).toFloat() + 15f

    val leftPath = Path().apply {
        moveTo(centerX - 4f, pivotY)
        lineTo(leftKneeX, leftKneeY)
        lineTo(leftTipX, leftTipY)
        lineTo(leftTipX + 4f, leftTipY - 2f)
        lineTo(leftKneeX + 3f, leftKneeY - 2f)
        close()
    }
    drawPath(path = leftPath, color = primaryColor)
    // Left prong rubber tip
    drawCircle(color = accentColor, radius = 3.5f, center = Offset(leftTipX, leftTipY))

    // Right Prong
    val rightRad = Math.toRadians((angleSpread).toDouble())
    val rightKneeX = centerX + (cos(rightRad) * prongLength * 0.65f).toFloat()
    val rightKneeY = pivotY + (sin(rightRad) * prongLength * 0.65f).toFloat() + 20f - (sin(rightRad) * prongLength * 0.65f).toFloat() + 20f

    val rightTipRad = Math.toRadians((angleSpread - 35f).toDouble())
    val rightTipX = rightKneeX + (cos(rightTipRad) * prongLength * 0.5f).toFloat()
    val rightTipY = rightKneeY + 15f

    val rightPath = Path().apply {
        moveTo(centerX + 4f, pivotY)
        lineTo(rightKneeX, rightKneeY)
        lineTo(rightTipX, rightTipY)
        lineTo(rightTipX - 4f, rightTipY - 2f)
        lineTo(rightKneeX - 3f, rightKneeY - 2f)
        close()
    }
    drawPath(path = rightPath, color = primaryColor)
    // Right prong rubber tip
    drawCircle(color = accentColor, radius = 3.5f, center = Offset(rightTipX, rightTipY))

    // Center / 3rd / 4th Prongs if 3 or 4 prongs
    if (equippedClaw.prongsCount >= 3) {
        val centerProngTipY = pivotY + prongLength + 6f - (openFraction * 10f)
        drawLine(
            color = primaryColor.copy(alpha = 0.85f),
            start = Offset(centerX, pivotY),
            end = Offset(centerX, centerProngTipY),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        drawCircle(color = accentColor, radius = 3.5f, center = Offset(centerX, centerProngTipY))
    }
}

// Function to render crisp, colorful arcade prizes
private fun DrawScope.drawPrizeGraphic(
    type: PrizeType,
    centerX: Float,
    centerY: Float,
    rotation: Float,
    isGrabbed: Boolean
) {
    val primaryColor = Color(type.primaryColorHex)
    val secondaryColor = Color(type.secondaryColorHex)
    val scale = if (isGrabbed) 1.08f else 1.0f

    when (type.shapeType) {
        "bear" -> {
            // Teddy Barnaby Bear
            val r = 16f * scale
            // Ears
            drawCircle(color = primaryColor, radius = 6f * scale, center = Offset(centerX - (r * 0.8f), centerY - (r * 0.8f)))
            drawCircle(color = secondaryColor, radius = 3f * scale, center = Offset(centerX - (r * 0.8f), centerY - (r * 0.8f)))
            drawCircle(color = primaryColor, radius = 6f * scale, center = Offset(centerX + (r * 0.8f), centerY - (r * 0.8f)))
            drawCircle(color = secondaryColor, radius = 3f * scale, center = Offset(centerX + (r * 0.8f), centerY - (r * 0.8f)))
            // Body / Head
            drawCircle(color = primaryColor, radius = r, center = Offset(centerX, centerY))
            // Snout
            drawOval(color = secondaryColor, topLeft = Offset(centerX - 8f, centerY - 2f), size = Size(16f, 12f))
            // Nose & Eyes
            drawCircle(color = Color(0xFF1E293B), radius = 2f, center = Offset(centerX, centerY + 2f))
            drawCircle(color = Color(0xFF1E293B), radius = 2f, center = Offset(centerX - 5f, centerY - 5f))
            drawCircle(color = Color(0xFF1E293B), radius = 2f, center = Offset(centerX + 5f, centerY - 5f))
        }

        "duck" -> {
            // Captain Quack Rubber Ducky
            val r = 15f * scale
            // Body
            drawOval(color = primaryColor, topLeft = Offset(centerX - 16f, centerY - 6f), size = Size(32f, 20f))
            // Head
            drawCircle(color = primaryColor, radius = 10f * scale, center = Offset(centerX - 4f, centerY - 12f))
            // Orange Beak
            val beak = Path().apply {
                moveTo(centerX - 12f, centerY - 14f)
                lineTo(centerX - 24f, centerY - 11f)
                lineTo(centerX - 12f, centerY - 8f)
                close()
            }
            drawPath(path = beak, color = secondaryColor)
            // Sailor Cap
            drawRect(color = Color.White, topLeft = Offset(centerX - 8f, centerY - 22f), size = Size(12f, 4f))
            drawCircle(color = Color(0xFF1E3A8A), radius = 2f, center = Offset(centerX - 2f, centerY - 23f))
            // Eye
            drawCircle(color = Color(0xFF1E293B), radius = 2f, center = Offset(centerX - 6f, centerY - 13f))
        }

        "cat" -> {
            // Lucky Maneki Neko
            val r = 16f * scale
            // Ears
            val earL = Path().apply {
                moveTo(centerX - 12f, centerY - 10f)
                lineTo(centerX - 16f, centerY - 24f)
                lineTo(centerX - 4f, centerY - 16f)
                close()
            }
            drawPath(path = earL, color = primaryColor)
            val earR = Path().apply {
                moveTo(centerX + 12f, centerY - 10f)
                lineTo(centerX + 16f, centerY - 24f)
                lineTo(centerX + 4f, centerY - 16f)
                close()
            }
            drawPath(path = earR, color = primaryColor)
            // Head
            drawCircle(color = primaryColor, radius = r, center = Offset(centerX, centerY))
            // Red Collar
            drawRoundRect(color = secondaryColor, topLeft = Offset(centerX - 12f, centerY + 10f), size = Size(24f, 6f), cornerRadius = CornerRadius(3f, 3f))
            // Golden Bell
            drawCircle(color = Color(0xFFFDE047), radius = 3.5f, center = Offset(centerX, centerY + 14f))
            // Eyes
            drawCircle(color = Color(0xFF1E293B), radius = 2.5f, center = Offset(centerX - 6f, centerY - 4f))
            drawCircle(color = Color(0xFF1E293B), radius = 2.5f, center = Offset(centerX + 6f, centerY - 4f))
        }

        "robot" -> {
            // Mecha Bot 3000
            val size = 26f * scale
            // Antenna
            drawLine(color = secondaryColor, start = Offset(centerX, centerY - (size / 2f)), end = Offset(centerX, centerY - (size / 2f) - 8f), strokeWidth = 3f)
            drawCircle(color = Color(0xFFEF4444), radius = 3f, center = Offset(centerX, centerY - (size / 2f) - 8f))
            // Head
            drawRoundRect(color = primaryColor, topLeft = Offset(centerX - (size / 2f), centerY - (size / 2f)), size = Size(size, size), cornerRadius = CornerRadius(6f, 6f))
            // Visor
            drawRoundRect(color = secondaryColor, topLeft = Offset(centerX - (size * 0.35f), centerY - 6f), size = Size(size * 0.7f, 10f), cornerRadius = CornerRadius(3f, 3f))
        }

        "boba" -> {
            // Taro Boba
            val cupW = 22f * scale
            val cupH = 28f * scale
            // Straw
            drawLine(color = Color(0xFFF43F5E), start = Offset(centerX + 2f, centerY - (cupH / 2f)), end = Offset(centerX + 8f, centerY - (cupH / 2f) - 10f), strokeWidth = 4f)
            // Cup body
            drawRoundRect(color = primaryColor, topLeft = Offset(centerX - (cupW / 2f), centerY - (cupH / 2f)), size = Size(cupW, cupH), cornerRadius = CornerRadius(6f, 6f))
            // Tapioca pearls
            drawCircle(color = Color(0xFF1E1B4B), radius = 2.5f, center = Offset(centerX - 4f, centerY + 6f))
            drawCircle(color = Color(0xFF1E1B4B), radius = 2.5f, center = Offset(centerX + 4f, centerY + 7f))
            drawCircle(color = Color(0xFF1E1B4B), radius = 2.5f, center = Offset(centerX, centerY + 10f))
        }

        "gem" -> {
            // Prism Emerald
            val r = 18f * scale
            val gem = Path().apply {
                moveTo(centerX, centerY - r)
                lineTo(centerX + r, centerY - (r * 0.3f))
                lineTo(centerX + (r * 0.6f), centerY + r)
                lineTo(centerX - (r * 0.6f), centerY + r)
                lineTo(centerX - r, centerY - (r * 0.3f))
                close()
            }
            drawPath(path = gem, color = primaryColor)
            drawPath(path = gem, color = secondaryColor, style = Stroke(width = 2.5f))
            // Facet line
            drawLine(color = secondaryColor, start = Offset(centerX, centerY - r), end = Offset(centerX, centerY + r), strokeWidth = 1.5f)
        }

        "egg" -> {
            // Cosmic Egg
            val eggW = 22f * scale
            val eggH = 30f * scale
            drawOval(color = primaryColor, topLeft = Offset(centerX - (eggW / 2f), centerY - (eggH / 2f)), size = Size(eggW, eggH))
            // Center cosmic stripe
            drawRoundRect(color = secondaryColor, topLeft = Offset(centerX - (eggW / 2f), centerY - 3f), size = Size(eggW, 6f), cornerRadius = CornerRadius(2f, 2f))
        }

        "crown" -> {
            // Monarch Crown
            val crownW = 32f * scale
            val crownH = 22f * scale
            val crownPath = Path().apply {
                moveTo(centerX - (crownW / 2f), centerY + (crownH / 2f))
                lineTo(centerX + (crownW / 2f), centerY + (crownH / 2f))
                lineTo(centerX + (crownW / 2f), centerY - (crownH / 3f))
                lineTo(centerX + (crownW / 4f), centerY)
                lineTo(centerX, centerY - (crownH / 2f))
                lineTo(centerX - (crownW / 4f), centerY)
                lineTo(centerX - (crownW / 2f), centerY - (crownH / 3f))
                close()
            }
            drawPath(path = crownPath, color = primaryColor)
            // Ruby jewels on peaks
            drawCircle(color = secondaryColor, radius = 3f, center = Offset(centerX, centerY - (crownH / 2f)))
            drawCircle(color = secondaryColor, radius = 2.5f, center = Offset(centerX - (crownW / 2f), centerY - (crownH / 3f)))
            drawCircle(color = secondaryColor, radius = 2.5f, center = Offset(centerX + (crownW / 2f), centerY - (crownH / 3f)))
        }

        "unicorn" -> {
            // Stardust Jackpot Unicorn
            val r = 18f * scale
            // Golden Rainbow Horn
            val horn = Path().apply {
                moveTo(centerX - 2f, centerY - r)
                lineTo(centerX + 6f, centerY - r - 16f)
                lineTo(centerX + 4f, centerY - r)
                close()
            }
            drawPath(path = horn, color = ArcadeGold)
            // Head
            drawCircle(color = primaryColor, radius = r, center = Offset(centerX, centerY))
            // Mane
            drawCircle(color = secondaryColor, radius = 8f, center = Offset(centerX + (r * 0.8f), centerY - 6f))
            drawCircle(color = NeonPink, radius = 7f, center = Offset(centerX + (r * 0.9f), centerY + 6f))
            // Eye
            drawCircle(color = Color(0xFF1E293B), radius = 2.5f, center = Offset(centerX - 4f, centerY - 2f))
        }

        else -> {
            // Default ball
            drawCircle(color = primaryColor, radius = 14f * scale, center = Offset(centerX, centerY))
        }
    }
}
