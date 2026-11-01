package com.example.data.model

enum class PrizeRarity(
    val displayName: String,
    val colorHex: Long,
    val baseValueCoins: Int,
    val baseValueTickets: Int,
    val weight: Float // 1.0 (light) to 3.0 (heavy)
) {
    COMMON("Common", 0xFF10B981, 60, 2, 1.0f),
    RARE("Rare", 0xFF3B82F6, 160, 8, 1.4f),
    EPIC("Epic", 0xFF8B5CF6, 400, 25, 1.8f),
    LEGENDARY("Legendary", 0xFFF59E0B, 1000, 70, 2.4f),
    JACKPOT("Jackpot", 0xFFEF4444, 2500, 180, 3.0f)
}

enum class PrizeType(
    val title: String,
    val description: String,
    val rarity: PrizeRarity,
    val shapeType: String, // "bear", "duck", "cat", "robot", "gem", "boba", "egg", "unicorn", "crown"
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val isMetallic: Boolean = false
) {
    TEDDY_BEAR(
        title = "Barnaby Bear",
        description = "A warm, cuddly vintage teddy with stitched heart.",
        rarity = PrizeRarity.COMMON,
        shapeType = "bear",
        primaryColorHex = 0xFFD97706,
        secondaryColorHex = 0xFFFEF3C7
    ),
    RUBBER_DUCK(
        title = "Captain Quack",
        description = "Classic arcade rubber duck wearing a tiny sailor hat.",
        rarity = PrizeRarity.COMMON,
        shapeType = "duck",
        primaryColorHex = 0xFFFBBF24,
        secondaryColorHex = 0xFFF97316
    ),
    FORTUNE_CAT(
        title = "Lucky Maneki",
        description = "Waving golden fortune cat bringing good luck.",
        rarity = PrizeRarity.RARE,
        shapeType = "cat",
        primaryColorHex = 0xFFFDE047,
        secondaryColorHex = 0xFFDC2626,
        isMetallic = true
    ),
    ROBO_BUDDY(
        title = "Mecha Bot 3000",
        description = "Retro metallic robot with glowing antenna.",
        rarity = PrizeRarity.RARE,
        shapeType = "robot",
        primaryColorHex = 0xFF64748B,
        secondaryColorHex = 0xFF06B6D4,
        isMetallic = true
    ),
    KAWAII_BOBA(
        title = "Taro Boba Plush",
        description = "Sweet smiling bubble tea with squishy tapioca pearls.",
        rarity = PrizeRarity.RARE,
        shapeType = "boba",
        primaryColorHex = 0xFFA855F7,
        secondaryColorHex = 0xFFF3E8FF
    ),
    EMERALD_CRYSTAL(
        title = "Prism Emerald",
        description = "Polished geode crystal that sparkles under arcade lights.",
        rarity = PrizeRarity.EPIC,
        shapeType = "gem",
        primaryColorHex = 0xFF10B981,
        secondaryColorHex = 0xFF6EE7B7,
        isMetallic = true
    ),
    MYSTERY_EGG(
        title = "Cosmic Capsule",
        description = "Surprise capsule containing mystery arcade riches!",
        rarity = PrizeRarity.EPIC,
        shapeType = "egg",
        primaryColorHex = 0xFFEC4899,
        secondaryColorHex = 0xFF8B5CF6
    ),
    ROYAL_CROWN(
        title = "Arcade Monarch Crown",
        description = "Gilded royal crown encrusted with ruby gems.",
        rarity = PrizeRarity.LEGENDARY,
        shapeType = "crown",
        primaryColorHex = 0xFFF59E0B,
        secondaryColorHex = 0xFFE11D48,
        isMetallic = true
    ),
    RAINBOW_UNICORN(
        title = "Stardust Unicorn",
        description = "The ultimate prize! Glowing rainbow horn and dream coat.",
        rarity = PrizeRarity.JACKPOT,
        shapeType = "unicorn",
        primaryColorHex = 0xFFF472B6,
        secondaryColorHex = 0xFF38BDF8
    )
}

data class PrizeItem(
    val id: String,
    val type: PrizeType,
    var x: Float, // horizontal position 0.0f .. 1.0f inside pit
    var y: Float, // vertical elevation 0.0f (bottom) .. 1.0f (top of pit)
    var rotation: Float = 0f,
    val sizeDp: Float = 48f,
    var isGrabbed: Boolean = false
)

data class ClawModel(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val costCoins: Int,
    val costTickets: Int = 0,
    val baseGripPercent: Int, // e.g. 40 to 95
    val prongsCount: Int, // 2, 3, or 4
    val reachMultiplier: Float, // 1.0 to 1.5
    val ticketBonusMultiplier: Float, // 1.0 to 2.0
    val coinBonusMultiplier: Float, // 1.0 to 2.0
    val specialPerk: String,
    val primaryColorHex: Long,
    val accentColorHex: Long,
    val hasLaserPointer: Boolean = false,
    val hasMagneticPulse: Boolean = false,
    val isTitanCosmic: Boolean = false
)

enum class UpgradeType(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val maxLevel: Int,
    val baseCostCoins: Int,
    val costMultiplier: Float,
    val statLabel: String
) {
    GRIP_PRESSURE(
        id = "grip_pressure",
        title = "Hydraulic Grip Force",
        description = "Closes claws tighter around prizes to prevent slips during ascent.",
        iconName = "grip",
        maxLevel = 10,
        baseCostCoins = 80,
        costMultiplier = 1.45f,
        statLabel = "+5% Grip Tightness"
    ),
    GANTRY_MOTOR(
        id = "gantry_motor",
        title = "Gantry Motor Speed",
        description = "Speeds up horizontal positioning and crane return journey.",
        iconName = "motor",
        maxLevel = 8,
        baseCostCoins = 70,
        costMultiplier = 1.35f,
        statLabel = "+12% Carriage Speed"
    ),
    LASER_GUIDE(
        id = "laser_guide",
        title = "Targeting Laser Sight",
        description = "Projects a high-precision laser beam showing exact drop pinch zone.",
        iconName = "laser",
        maxLevel = 5,
        baseCostCoins = 150,
        costMultiplier = 1.6f,
        statLabel = "Laser Brightness & Reticle"
    ),
    CHUTE_FUNNEL(
        id = "chute_funnel",
        title = "Chute Funnel Expander",
        description = "Widens the goal drop chute so wobbly prizes still fall inside.",
        iconName = "funnel",
        maxLevel = 6,
        baseCostCoins = 120,
        costMultiplier = 1.5f,
        statLabel = "+8% Chute Drop Area"
    ),
    ANTI_SLIP_DAMPER(
        id = "anti_slip_damper",
        title = "Anti-Slip Tension Damper",
        description = "Absorbs carriage vibration when the claw stops and reverses.",
        iconName = "damper",
        maxLevel = 8,
        baseCostCoins = 100,
        costMultiplier = 1.4f,
        statLabel = "-10% Mid-Air Drop Slip"
    ),
    FORTUNE_LUCK(
        id = "fortune_luck",
        title = "Arcade Prize Luck",
        description = "Increases spawn probability for Rare, Epic, and Legendary plushies.",
        iconName = "luck",
        maxLevel = 8,
        baseCostCoins = 180,
        costMultiplier = 1.55f,
        statLabel = "+15% Rare Prize Spawn"
    ),
    COIN_MULTIPLIER(
        id = "coin_multiplier",
        title = "Token Payday Multiplier",
        description = "Earns extra coins and bonus tickets whenever you win a prize.",
        iconName = "coins",
        maxLevel = 6,
        baseCostCoins = 200,
        costMultiplier = 1.65f,
        statLabel = "+20% Cashout Value"
    )
}

data class MissionItem(
    val id: String,
    val title: String,
    val description: String,
    val rewardCoins: Int,
    val rewardTickets: Int,
    val targetCount: Int,
    var currentCount: Int,
    var isClaimed: Boolean = false
) {
    val isCompleted: Boolean get() = currentCount >= targetCount
}
