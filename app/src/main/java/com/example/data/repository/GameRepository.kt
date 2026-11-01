package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ClawModel
import com.example.data.model.MissionItem
import com.example.data.model.PrizeRarity
import com.example.data.model.PrizeType
import com.example.data.model.UpgradeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

class GameRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("claw_master_prefs", Context.MODE_PRIVATE)

    // Master Catalog of Claws
    val clawCatalog = listOf(
        ClawModel(
            id = "claw_classic",
            name = "Arcade Standard",
            subtitle = "The Classic 3-Prong",
            description = "The familiar arcade claw. Modest grip and honest chances.",
            costCoins = 0,
            costTickets = 0,
            baseGripPercent = 40,
            prongsCount = 3,
            reachMultiplier = 1.0f,
            ticketBonusMultiplier = 1.0f,
            coinBonusMultiplier = 1.0f,
            specialPerk = "Standard reach & classic arcade feel",
            primaryColorHex = 0xFF94A3B8,
            accentColorHex = 0xFFEF4444
        ),
        ClawModel(
            id = "claw_steel",
            name = "Steel Talon",
            subtitle = "Heavy Duty Quad-Claw",
            description = "4 reinforced carbon-steel claws for superior grip hold on heavy toys.",
            costCoins = 150,
            costTickets = 0,
            baseGripPercent = 60,
            prongsCount = 4,
            reachMultiplier = 1.15f,
            ticketBonusMultiplier = 1.1f,
            coinBonusMultiplier = 1.1f,
            specialPerk = "+20% Grip Strength & 4-point pincher",
            primaryColorHex = 0xFF64748B,
            accentColorHex = 0xFF38BDF8
        ),
        ClawModel(
            id = "claw_magnet",
            name = "Electro-Magnetizer",
            subtitle = "Flux Core Attraction",
            description = "Electromagnetic pulse pulls metallic and capsule prizes directly into the claw!",
            costCoins = 380,
            costTickets = 15,
            baseGripPercent = 72,
            prongsCount = 3,
            reachMultiplier = 1.25f,
            ticketBonusMultiplier = 1.25f,
            coinBonusMultiplier = 1.25f,
            specialPerk = "Magnetic field draws metallic prizes into center",
            primaryColorHex = 0xFF0284C7,
            accentColorHex = 0xFF22D3EE,
            hasMagneticPulse = true
        ),
        ClawModel(
            id = "claw_gold",
            name = "Gilded Sovereign",
            subtitle = "24K Gold Plated",
            description = "Lavish golden finish with diamond-cut tips. Generates extra tickets on every win!",
            costCoins = 850,
            costTickets = 30,
            baseGripPercent = 80,
            prongsCount = 3,
            reachMultiplier = 1.2f,
            ticketBonusMultiplier = 1.75f,
            coinBonusMultiplier = 1.5f,
            specialPerk = "+75% Tickets & +50% Coins on won prizes",
            primaryColorHex = 0xFFF59E0B,
            accentColorHex = 0xFFFDE047
        ),
        ClawModel(
            id = "claw_cyber",
            name = "Cyberpunk Neo",
            subtitle = "Laser-Guided Hydraulic",
            description = "Precision laser targeting beam and zero-wobble high-torque servos.",
            costCoins = 1800,
            costTickets = 60,
            baseGripPercent = 88,
            prongsCount = 4,
            reachMultiplier = 1.35f,
            ticketBonusMultiplier = 1.4f,
            coinBonusMultiplier = 1.4f,
            specialPerk = "Built-in laser targeting reticle & ultra-grip",
            primaryColorHex = 0xFFD946EF,
            accentColorHex = 0xFF06B6D4,
            hasLaserPointer = true
        ),
        ClawModel(
            id = "claw_dragon",
            name = "Dragon's Maw",
            subtitle = "Massive Wide Scoop",
            description = "Spanned dragon talon prongs designed to envelop massive jackpot plushies.",
            costCoins = 3500,
            costTickets = 120,
            baseGripPercent = 94,
            prongsCount = 4,
            reachMultiplier = 1.55f,
            ticketBonusMultiplier = 1.5f,
            coinBonusMultiplier = 1.6f,
            specialPerk = "Massive grab radius can snatch multiple prizes",
            primaryColorHex = 0xFFDC2626,
            accentColorHex = 0xFFF59E0B
        ),
        ClawModel(
            id = "claw_quantum",
            name = "Quantum Titan",
            subtitle = "Celestial Void Gripper",
            description = "Harnesses quantum gravitational anchors. The ultimate claw in the arcade universe.",
            costCoins = 7500,
            costTickets = 250,
            baseGripPercent = 98,
            prongsCount = 4,
            reachMultiplier = 1.6f,
            ticketBonusMultiplier = 2.0f,
            coinBonusMultiplier = 2.0f,
            specialPerk = "Near 100% Grip & Quantum cosmic aura",
            primaryColorHex = 0xFF8B5CF6,
            accentColorHex = 0xFFEC4899,
            hasLaserPointer = true,
            hasMagneticPulse = true,
            isTitanCosmic = true
        )
    )

    // Observable States
    private val _coins = MutableStateFlow(prefs.getInt("user_coins", 250))
    val coins: StateFlow<Int> = _coins.asStateFlow()

    private val _tickets = MutableStateFlow(prefs.getInt("user_tickets", 15))
    val tickets: StateFlow<Int> = _tickets.asStateFlow()

    private val _equippedClawId = MutableStateFlow(prefs.getString("equipped_claw_id", "claw_classic") ?: "claw_classic")
    val equippedClawId: StateFlow<String> = _equippedClawId.asStateFlow()

    private val _unlockedClawIds = MutableStateFlow(
        prefs.getStringSet("unlocked_claws", setOf("claw_classic")) ?: setOf("claw_classic")
    )
    val unlockedClawIds: StateFlow<Set<String>> = _unlockedClawIds.asStateFlow()

    // Upgrades level map: UpgradeType.id -> level
    private val _upgradeLevels = MutableStateFlow<Map<String, Int>>(loadUpgrades())
    val upgradeLevels: StateFlow<Map<String, Int>> = _upgradeLevels.asStateFlow()

    // Prize collection map: PrizeType.name -> count caught
    private val _prizeCollection = MutableStateFlow<Map<String, Int>>(loadCollection())
    val prizeCollection: StateFlow<Map<String, Int>> = _prizeCollection.asStateFlow()

    // Statistics
    private val _totalPlays = MutableStateFlow(prefs.getInt("stat_total_plays", 0))
    val totalPlays: StateFlow<Int> = _totalPlays.asStateFlow()

    private val _totalWins = MutableStateFlow(prefs.getInt("stat_total_wins", 0))
    val totalWins: StateFlow<Int> = _totalWins.asStateFlow()

    private val _totalCoinsEarned = MutableStateFlow(prefs.getInt("stat_total_coins_earned", 0))
    val totalCoinsEarned: StateFlow<Int> = _totalCoinsEarned.asStateFlow()

    // Daily / Progression Missions
    private val _missions = MutableStateFlow<List<MissionItem>>(loadMissions())
    val missions: StateFlow<List<MissionItem>> = _missions.asStateFlow()

    private fun loadUpgrades(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        for (type in UpgradeType.values()) {
            map[type.id] = prefs.getInt("upgrade_${type.id}", 0)
        }
        return map
    }

    private fun loadCollection(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        for (type in PrizeType.values()) {
            map[type.name] = prefs.getInt("collection_${type.name}", 0)
        }
        return map
    }

    private fun loadMissions(): List<MissionItem> {
        return listOf(
            MissionItem(
                id = "m_play_3",
                title = "Arcade Warmup",
                description = "Play 3 rounds of the claw machine.",
                rewardCoins = 100,
                rewardTickets = 5,
                targetCount = 3,
                currentCount = prefs.getInt("mission_m_play_3", 0),
                isClaimed = prefs.getBoolean("mission_claimed_m_play_3", false)
            ),
            MissionItem(
                id = "m_win_1",
                title = "First Catch",
                description = "Successfully grab and drop 1 prize into the chute.",
                rewardCoins = 150,
                rewardTickets = 10,
                targetCount = 1,
                currentCount = prefs.getInt("mission_m_win_1", 0),
                isClaimed = prefs.getBoolean("mission_claimed_m_win_1", false)
            ),
            MissionItem(
                id = "m_upgrade_2",
                title = "Workshop Tinkerer",
                description = "Purchase or upgrade 2 machine improvements.",
                rewardCoins = 200,
                rewardTickets = 12,
                targetCount = 2,
                currentCount = prefs.getInt("mission_m_upgrade_2", 0),
                isClaimed = prefs.getBoolean("mission_claimed_m_upgrade_2", false)
            ),
            MissionItem(
                id = "m_win_rare",
                title = "Rare Hunter",
                description = "Snag a Rare or higher rarity prize.",
                rewardCoins = 300,
                rewardTickets = 20,
                targetCount = 1,
                currentCount = prefs.getInt("mission_m_win_rare", 0),
                isClaimed = prefs.getBoolean("mission_claimed_m_win_rare", false)
            ),
            MissionItem(
                id = "m_win_5",
                title = "Claw Virtuoso",
                description = "Win a total of 5 prizes from the pit.",
                rewardCoins = 500,
                rewardTickets = 35,
                targetCount = 5,
                currentCount = prefs.getInt("mission_m_win_5", 0),
                isClaimed = prefs.getBoolean("mission_claimed_m_win_5", false)
            )
        )
    }

    // Currency Actions
    fun addCoins(amount: Int) {
        val updated = _coins.value + amount
        _coins.value = updated
        _totalCoinsEarned.value += amount
        prefs.edit()
            .putInt("user_coins", updated)
            .putInt("stat_total_coins_earned", _totalCoinsEarned.value)
            .apply()
    }

    fun spendCoins(amount: Int): Boolean {
        if (_coins.value >= amount) {
            val updated = _coins.value - amount
            _coins.value = updated
            prefs.edit().putInt("user_coins", updated).apply()
            return true
        }
        return false
    }

    fun addTickets(amount: Int) {
        val updated = _tickets.value + amount
        _tickets.value = updated
        prefs.edit().putInt("user_tickets", updated).apply()
    }

    fun spendTickets(amount: Int): Boolean {
        if (_tickets.value >= amount) {
            val updated = _tickets.value - amount
            _tickets.value = updated
            prefs.edit().putInt("user_tickets", updated).apply()
            return true
        }
        return false
    }

    // Free Coin Allowance (Arcade Token Dispenser)
    fun claimFreeTokens(): Int {
        val freeTokens = 100
        addCoins(freeTokens)
        return freeTokens
    }

    // Claw Actions
    fun getEquippedClaw(): ClawModel {
        val currentId = _equippedClawId.value
        return clawCatalog.find { it.id == currentId } ?: clawCatalog.first()
    }

    fun buyClaw(claw: ClawModel): Boolean {
        if (_unlockedClawIds.value.contains(claw.id)) return false
        if (_coins.value >= claw.costCoins && _tickets.value >= claw.costTickets) {
            spendCoins(claw.costCoins)
            if (claw.costTickets > 0) spendTickets(claw.costTickets)

            val updatedSet = _unlockedClawIds.value + claw.id
            _unlockedClawIds.value = updatedSet
            _equippedClawId.value = claw.id

            prefs.edit()
                .putStringSet("unlocked_claws", updatedSet)
                .putString("equipped_claw_id", claw.id)
                .apply()
            return true
        }
        return false
    }

    fun equipClaw(clawId: String) {
        if (_unlockedClawIds.value.contains(clawId)) {
            _equippedClawId.value = clawId
            prefs.edit().putString("equipped_claw_id", clawId).apply()
        }
    }

    // Upgrade Actions
    fun getUpgradeLevel(type: UpgradeType): Int {
        return _upgradeLevels.value[type.id] ?: 0
    }

    fun getUpgradeCost(type: UpgradeType): Int {
        val currentLevel = getUpgradeLevel(type)
        if (currentLevel >= type.maxLevel) return -1
        return (type.baseCostCoins * Math.pow(type.costMultiplier.toDouble(), currentLevel.toDouble())).toInt()
    }

    fun upgradeMachine(type: UpgradeType): Boolean {
        val currentLevel = getUpgradeLevel(type)
        if (currentLevel >= type.maxLevel) return false
        val cost = getUpgradeCost(type)
        if (spendCoins(cost)) {
            val newLevel = currentLevel + 1
            val updatedMap = _upgradeLevels.value.toMutableMap()
            updatedMap[type.id] = newLevel
            _upgradeLevels.value = updatedMap
            prefs.edit().putInt("upgrade_${type.id}", newLevel).apply()

            incrementMission("m_upgrade_2", 1)
            return true
        }
        return false
    }

    // Record Play
    fun recordPlay() {
        val plays = _totalPlays.value + 1
        _totalPlays.value = plays
        prefs.edit().putInt("stat_total_plays", plays).apply()
        incrementMission("m_play_3", 1)
    }

    // Record Win
    fun recordPrizeWon(prizeType: PrizeType): Pair<Int, Int> {
        val wins = _totalWins.value + 1
        _totalWins.value = wins
        prefs.edit().putInt("stat_total_wins", wins).apply()

        // Update prize collection
        val currentCount = _prizeCollection.value[prizeType.name] ?: 0
        val updatedColl = _prizeCollection.value.toMutableMap()
        updatedColl[prizeType.name] = currentCount + 1
        _prizeCollection.value = updatedColl
        prefs.edit().putInt("collection_${prizeType.name}", currentCount + 1).apply()

        // Calculate reward with upgrades and claw multipliers
        val claw = getEquippedClaw()
        val coinMultiplierLevel = getUpgradeLevel(UpgradeType.COIN_MULTIPLIER)
        val upgradeCoinBonus = 1.0f + (coinMultiplierLevel * 0.20f)

        val totalCoinsAwarded = (prizeType.rarity.baseValueCoins * claw.coinBonusMultiplier * upgradeCoinBonus).toInt()
        val totalTicketsAwarded = (prizeType.rarity.baseValueTickets * claw.ticketBonusMultiplier).toInt()

        addCoins(totalCoinsAwarded)
        if (totalTicketsAwarded > 0) addTickets(totalTicketsAwarded)

        // Missions check
        incrementMission("m_win_1", 1)
        incrementMission("m_win_5", 1)
        if (prizeType.rarity != PrizeRarity.COMMON) {
            incrementMission("m_win_rare", 1)
        }

        return Pair(totalCoinsAwarded, totalTicketsAwarded)
    }

    // Sell Duplicate Prize for extra coins
    fun sellDuplicatePrize(prizeType: PrizeType): Int {
        val count = _prizeCollection.value[prizeType.name] ?: 0
        if (count > 1) {
            val updated = _prizeCollection.value.toMutableMap()
            updated[prizeType.name] = count - 1
            _prizeCollection.value = updated
            prefs.edit().putInt("collection_${prizeType.name}", count - 1).apply()

            val sellValue = (prizeType.rarity.baseValueCoins * 0.75f).toInt()
            addCoins(sellValue)
            return sellValue
        }
        return 0
    }

    private fun incrementMission(missionId: String, amount: Int) {
        val updated = _missions.value.map { mission ->
            if (mission.id == missionId && !mission.isClaimed) {
                val newCount = (mission.currentCount + amount).coerceAtMost(mission.targetCount)
                prefs.edit().putInt("mission_$missionId", newCount).apply()
                mission.copy(currentCount = newCount)
            } else {
                mission
            }
        }
        _missions.value = updated
    }

    fun claimMissionReward(missionId: String): Boolean {
        val mission = _missions.value.find { it.id == missionId } ?: return false
        if (mission.isCompleted && !mission.isClaimed) {
            addCoins(mission.rewardCoins)
            addTickets(mission.rewardTickets)

            val updated = _missions.value.map {
                if (it.id == missionId) {
                    prefs.edit().putBoolean("mission_claimed_$missionId", true).apply()
                    it.copy(isClaimed = true)
                } else it
            }
            _missions.value = updated
            return true
        }
        return false
    }

    // Effective Win Rate Probability calculation
    fun calculateEffectiveGripStrength(): Float {
        val claw = getEquippedClaw()
        val gripLevel = getUpgradeLevel(UpgradeType.GRIP_PRESSURE)
        val damperLevel = getUpgradeLevel(UpgradeType.ANTI_SLIP_DAMPER)

        val base = claw.baseGripPercent.toFloat()
        val hydraulicBonus = gripLevel * 5.0f
        val damperBonus = damperLevel * 2.5f

        val total = (base + hydraulicBonus + damperBonus).coerceIn(35f, 99f)
        return total
    }

    // Redeem Promo Codes
    fun redeemPromoCode(inputCode: String): RedeemResult {
        val code = inputCode.trim().uppercase()
        val redeemedSet = prefs.getStringSet("redeemed_codes", emptySet()) ?: emptySet()
        if (redeemedSet.contains(code)) {
            return RedeemResult.AlreadyRedeemed(code)
        }

        val result = when (code) {
            "CLAW2026" -> {
                addCoins(500)
                addTickets(25)
                RedeemResult.Success(500, 25, null, "Welcome bonus! +500 Coins & +25 Tickets")
            }
            "LUCKYDROP" -> {
                addCoins(300)
                addTickets(15)
                RedeemResult.Success(300, 15, null, "Lucky drop fortune! +300 Coins & +15 Tickets")
            }
            "ARCADEVIP" -> {
                addCoins(1000)
                addTickets(50)
                RedeemResult.Success(1000, 50, null, "VIP High Roller pass! +1,000 Coins & +50 Tickets")
            }
            "FREECOINS" -> {
                addCoins(250)
                RedeemResult.Success(250, 0, null, "Token dispenser cracked! +250 Coins")
            }
            "CYBERBOOST" -> {
                addCoins(500)
                addTickets(20)
                val cyberClaw = clawCatalog.find { it.id == "claw_cyber" }
                val clawName = if (cyberClaw != null && !_unlockedClawIds.value.contains("claw_cyber")) {
                    val updatedSet = _unlockedClawIds.value + "claw_cyber"
                    _unlockedClawIds.value = updatedSet
                    _equippedClawId.value = "claw_cyber"
                    prefs.edit()
                        .putStringSet("unlocked_claws", updatedSet)
                        .putString("equipped_claw_id", "claw_cyber")
                        .apply()
                    cyberClaw.name
                } else null
                RedeemResult.Success(500, 20, clawName, "Cyberpunk Overdrive! +500 Coins, +20 Tickets & Cyber Claw Unlocked!")
            }
            "TITANPOWER" -> {
                addCoins(2500)
                addTickets(100)
                RedeemResult.Success(2500, 100, null, "Quantum Singularity Unleashed! +2,500 Coins & +100 Tickets")
            }
            else -> RedeemResult.InvalidCode(code)
        }

        if (result is RedeemResult.Success) {
            prefs.edit().putStringSet("redeemed_codes", redeemedSet + code).apply()
        }
        return result
    }
}

sealed class RedeemResult {
    data class Success(
        val coins: Int,
        val tickets: Int,
        val unlockedClawName: String? = null,
        val message: String
    ) : RedeemResult()
    data class AlreadyRedeemed(val code: String) : RedeemResult()
    data class InvalidCode(val code: String) : RedeemResult()
}

