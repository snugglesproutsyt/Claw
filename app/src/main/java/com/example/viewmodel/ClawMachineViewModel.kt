package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.ArcadeSoundManager
import com.example.data.model.ClawModel
import com.example.data.model.PrizeItem
import com.example.data.model.PrizeRarity
import com.example.data.model.PrizeType
import com.example.data.model.UpgradeType
import com.example.data.repository.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random

enum class CraneState {
    IDLE,
    DESCENDING,
    GRABBING,
    ASCENDING,
    RETURNING,
    DROPPING,
    RESETTING
}

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val colorHex: Long,
    val size: Float,
    val rotation: Float,
    val vr: Float
)

class ClawMachineViewModel(application: Application) : AndroidViewModel(application) {

    val repository = GameRepository(application)
    val soundManager = ArcadeSoundManager(application)

    // Game Crane Position:
    // craneX: 0.0f (left / chute) to 1.0f (right)
    private val _craneX = MutableStateFlow(0.5f)
    val craneX: StateFlow<Float> = _craneX.asStateFlow()

    // clawY: 0.0f (top rail) to 1.0f (deep pit floor)
    private val _clawY = MutableStateFlow(0.0f)
    val clawY: StateFlow<Float> = _clawY.asStateFlow()

    // clawProngOpen: 0.0f (closed tight) to 1.0f (wide open)
    private val _clawProngOpen = MutableStateFlow(0.65f)
    val clawProngOpen: StateFlow<Float> = _clawProngOpen.asStateFlow()

    private val _craneState = MutableStateFlow(CraneState.IDLE)
    val craneState: StateFlow<CraneState> = _craneState.asStateFlow()

    // Currently grabbed prize (null if none or slipped)
    private val _grabbedPrize = MutableStateFlow<PrizeItem?>(null)
    val grabbedPrize: StateFlow<PrizeItem?> = _grabbedPrize.asStateFlow()

    // Pit Prize List
    private val _pitPrizes = MutableStateFlow<List<PrizeItem>>(emptyList())
    val pitPrizes: StateFlow<List<PrizeItem>> = _pitPrizes.asStateFlow()

    // Winning Prize Dialog State
    private val _recentWonPrize = MutableStateFlow<Pair<PrizeType, Pair<Int, Int>>?>(null) // Prize, Pair(coins, tickets)
    val recentWonPrize: StateFlow<Pair<PrizeType, Pair<Int, Int>>?> = _recentWonPrize.asStateFlow()

    // Confetti particles for celebrations
    private val _confetti = MutableStateFlow<List<ConfettiParticle>>(emptyList())
    val confetti: StateFlow<List<ConfettiParticle>> = _confetti.asStateFlow()

    // Floating notification / status toast
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // Moving Left / Right hold flags
    private var moveDirection = 0 // -1 left, 1 right, 0 stop
    private var moveJob: Job? = null
    private var animationLoopJob: Job? = null

    init {
        restockPit()
        startLoop()
    }

    private fun startLoop() {
        animationLoopJob?.cancel()
        animationLoopJob = viewModelScope.launch {
            while (isActive) {
                // Update movement if holding directional buttons in IDLE
                if (_craneState.value == CraneState.IDLE && moveDirection != 0) {
                    val motorSpeedLevel = repository.getUpgradeLevel(UpgradeType.GANTRY_MOTOR)
                    val speed = 0.02f + (motorSpeedLevel * 0.003f)
                    val nextX = (_craneX.value + (moveDirection * speed)).coerceIn(0.12f, 0.92f)
                    _craneX.value = nextX
                }

                // Update Confetti physics
                if (_confetti.value.isNotEmpty()) {
                    val updated = _confetti.value.mapNotNull { p ->
                        val ny = p.y + p.vy
                        val nx = p.x + p.vx
                        val nr = p.rotation + p.vr
                        if (ny < 1.1f) {
                            p.copy(x = nx, y = ny, vy = p.vy + 0.001f, rotation = nr)
                        } else null
                    }
                    _confetti.value = updated
                }

                delay(16) // ~60 FPS loop
            }
        }
    }

    // Directional controls
    fun startMove(dir: Int) {
        if (_craneState.value != CraneState.IDLE) return
        moveDirection = dir
        soundManager.playClawMove()
    }

    fun stopMove() {
        moveDirection = 0
    }

    fun setCranePositionDirect(targetX: Float) {
        if (_craneState.value != CraneState.IDLE) return
        _craneX.value = targetX.coerceIn(0.12f, 0.92f)
    }

    // Restock the prize pit
    fun restockPit() {
        val count = 14
        val luckLevel = repository.getUpgradeLevel(UpgradeType.FORTUNE_LUCK)
        val newPrizes = mutableListOf<PrizeItem>()

        val types = PrizeType.values()
        for (i in 0 until count) {
            // Prize rarity selection influenced by luck level
            val roll = Random.nextInt(100) + (luckLevel * 4)
            val selectedType = when {
                roll >= 115 -> PrizeType.RAINBOW_UNICORN // Jackpot
                roll >= 98 -> PrizeType.ROYAL_CROWN // Legendary
                roll >= 80 -> if (Random.nextBoolean()) PrizeType.EMERALD_CRYSTAL else PrizeType.MYSTERY_EGG // Epic
                roll >= 50 -> listOf(PrizeType.FORTUNE_CAT, PrizeType.ROBO_BUDDY, PrizeType.KAWAII_BOBA).random() // Rare
                else -> if (Random.nextBoolean()) PrizeType.TEDDY_BEAR else PrizeType.RUBBER_DUCK // Common
            }

            // Distribute across pit horizontally (0.15f to 0.90f) and layers
            val px = 0.15f + (i.toFloat() / count.toFloat()) * 0.72f + Random.nextFloat() * 0.06f - 0.03f
            val py = 0.15f + (Random.nextFloat() * 0.35f) // elevation in pit
            val rot = Random.nextFloat() * 40f - 20f

            newPrizes.add(
                PrizeItem(
                    id = UUID.randomUUID().toString(),
                    type = selectedType,
                    x = px.coerceIn(0.15f, 0.92f),
                    y = py,
                    rotation = rot
                )
            )
        }
        _pitPrizes.value = newPrizes
        _statusMessage.value = "Pit restocked with fresh prizes!"
    }

    // The Arcade Drop Trigger
    fun triggerDrop() {
        if (_craneState.value != CraneState.IDLE) return

        val costPerPlay = 10
        if (!repository.spendCoins(costPerPlay)) {
            _statusMessage.value = "Not enough coins! Claim free coins below."
            return
        }

        soundManager.playCoinInsert()
        repository.recordPlay()

        viewModelScope.launch {
            _craneState.value = CraneState.DESCENDING
            soundManager.playDropInitiated()

            // 1. Descend claw towards bottom
            _clawProngOpen.value = 1.0f // open wide while dropping
            val motorLevel = repository.getUpgradeLevel(UpgradeType.GANTRY_MOTOR)
            val descendSteps = (35 - motorLevel).coerceAtLeast(18)

            for (step in 1..descendSteps) {
                _clawY.value = step.toFloat() / descendSteps.toFloat() * 0.85f
                delay(22)
            }

            // 2. Grabbing phase
            _craneState.value = CraneState.GRABBING
            val claw = repository.getEquippedClaw()

            // Check if claw tips encompass any prize
            val currentX = _craneX.value
            val reachMargin = 0.075f * claw.reachMultiplier

            // Find candidates
            var candidate = _pitPrizes.value
                .filter { !it.isGrabbed }
                .minByOrNull { abs(it.x - currentX) }

            // Magnetic attraction perk check
            if (claw.hasMagneticPulse && candidate != null && abs(candidate.x - currentX) < 0.14f) {
                if (candidate.type.isMetallic || candidate.type == PrizeType.MYSTERY_EGG) {
                    // Pull candidate closer
                    candidate.x = currentX
                    _statusMessage.value = "Magnetic Pulse locked onto ${candidate.type.title}!"
                }
            }

            var prizeCaught: PrizeItem? = null
            if (candidate != null && abs(candidate.x - currentX) <= reachMargin) {
                // Calculate grab success probability
                val baseGrip = claw.baseGripPercent
                val hydraulicBonus = repository.getUpgradeLevel(UpgradeType.GRIP_PRESSURE) * 5
                val antiSlipBonus = repository.getUpgradeLevel(UpgradeType.ANTI_SLIP_DAMPER) * 2

                // Prize weight penalty
                val weightFactor = candidate.type.rarity.weight // 1.0 to 3.0
                val effectiveChance = ((baseGrip + hydraulicBonus + antiSlipBonus) / weightFactor).coerceIn(15f, 98f)

                val roll = Random.nextInt(100)
                if (roll < effectiveChance) {
                    prizeCaught = candidate
                }
            }

            // Animate claw prongs clamping
            soundManager.playClawGrab()
            for (step in 1..10) {
                _clawProngOpen.value = 1.0f - (step * 0.07f)
                delay(30)
            }

            if (prizeCaught != null) {
                prizeCaught.isGrabbed = true
                _grabbedPrize.value = prizeCaught
            }

            delay(200)

            // 3. Ascending phase
            _craneState.value = CraneState.ASCENDING
            val startY = _clawY.value
            val ascendSteps = (30 - motorLevel).coerceAtLeast(16)

            for (step in 1..ascendSteps) {
                val progress = step.toFloat() / ascendSteps.toFloat()
                _clawY.value = startY * (1.0f - progress)

                // If holding prize, it follows the claw
                _grabbedPrize.value?.let {
                    it.x = _craneX.value
                    it.y = 0.15f + (_clawY.value * 0.7f)
                }

                // Mid-ascent slip check (for heavier prizes if grip is weak)
                if (_grabbedPrize.value != null && step == (ascendSteps / 2)) {
                    val slipRisk = (candidate?.type?.rarity?.weight ?: 1.0f) * 12f - (repository.getUpgradeLevel(UpgradeType.ANTI_SLIP_DAMPER) * 3f)
                    if (slipRisk > 0 && Random.nextInt(100) < slipRisk && !claw.isTitanCosmic) {
                        // Slipped during ascent!
                        soundManager.playSlipMiss()
                        val dropped = _grabbedPrize.value
                        dropped?.isGrabbed = false
                        dropped?.y = 0.15f
                        _grabbedPrize.value = null
                        _clawProngOpen.value = 0.4f
                        _statusMessage.value = "Prize slipped from the claws!"
                    }
                }
                delay(24)
            }
            _clawY.value = 0.0f

            // Top rail tension jolt slip check
            if (_grabbedPrize.value != null) {
                val topSlipChance = (20 - (repository.getUpgradeLevel(UpgradeType.ANTI_SLIP_DAMPER) * 3)).coerceAtLeast(0)
                if (Random.nextInt(100) < topSlipChance && !claw.isTitanCosmic) {
                    soundManager.playSlipMiss()
                    val dropped = _grabbedPrize.value
                    dropped?.isGrabbed = false
                    dropped?.y = 0.15f
                    _grabbedPrize.value = null
                    _statusMessage.value = "Claw jolted at the top and dropped the prize!"
                }
            }

            delay(200)

            // 4. Returning to Drop Chute (x -> 0.10f)
            _craneState.value = CraneState.RETURNING
            val returnTargetX = 0.10f
            val returnStartX = _craneX.value
            val returnSteps = 28

            for (step in 1..returnSteps) {
                val t = step.toFloat() / returnSteps.toFloat()
                _craneX.value = returnStartX + (returnTargetX - returnStartX) * t
                _grabbedPrize.value?.let {
                    it.x = _craneX.value
                }
                delay(20)
            }

            // 5. Dropping into Chute
            _craneState.value = CraneState.DROPPING
            _clawProngOpen.value = 0.9f
            delay(150)

            val wonItem = _grabbedPrize.value
            if (wonItem != null) {
                // Drop item down chute
                for (step in 1..15) {
                    wonItem.y = 0.7f - (step * 0.05f)
                    delay(15)
                }

                // Remove won item from pit
                _pitPrizes.value = _pitPrizes.value.filter { it.id != wonItem.id }
                _grabbedPrize.value = null

                // Award rewards and register win!
                val reward = repository.recordPrizeWon(wonItem.type)
                soundManager.playPrizeWon()
                spawnConfetti()

                _recentWonPrize.value = Pair(wonItem.type, reward)
                _statusMessage.value = "WINNER! You won ${wonItem.type.title}!"
            } else {
                _statusMessage.value = "Better luck next drop!"
            }

            delay(600)

            // 6. Resetting to Idle position
            _craneState.value = CraneState.RESETTING
            _clawProngOpen.value = 0.65f

            // Return to center
            val resetStartX = _craneX.value
            val centerTargetX = 0.50f
            for (step in 1..20) {
                val t = step.toFloat() / 20.toFloat()
                _craneX.value = resetStartX + (centerTargetX - resetStartX) * t
                delay(15)
            }

            _craneState.value = CraneState.IDLE

            // Auto restock if pit has fewer than 5 prizes
            if (_pitPrizes.value.size < 5) {
                restockPit()
            }
        }
    }

    private fun spawnConfetti() {
        val colors = listOf(0xFFFBBF24, 0xFFEC4899, 0xFF38BDF8, 0xFF10B981, 0xFFF43F5E, 0xFF8B5CF6)
        val particles = mutableListOf<ConfettiParticle>()
        for (i in 0 until 50) {
            particles.add(
                ConfettiParticle(
                    x = 0.12f + (Random.nextFloat() * 0.35f),
                    y = 0.10f + (Random.nextFloat() * 0.20f),
                    vx = (Random.nextFloat() - 0.45f) * 0.02f,
                    vy = -(Random.nextFloat() * 0.018f + 0.005f),
                    colorHex = colors.random(),
                    size = 12f + Random.nextFloat() * 14f,
                    rotation = Random.nextFloat() * 360f,
                    vr = (Random.nextFloat() - 0.5f) * 16f
                )
            )
        }
        _confetti.value = particles
    }

    fun dismissWinDialog() {
        _recentWonPrize.value = null
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
