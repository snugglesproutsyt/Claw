# 🎟️ Claw Master - Redeem Codes & Rewards Guide

Welcome to the official **Claw Master** promo codes guide! Redeem these codes in-game to claim free coins, tickets, and unlock exclusive high-tier claws.

---

## 🎁 Active Redeem Codes

| Promo Code | Rewards | Description |
| :--- | :--- | :--- |
| `CLAW2026` | **500 Coins** + **25 Tickets** | Official 2026 Arcade Welcome Gift |
| `LUCKYDROP` | **300 Coins** + **15 Tickets** | Fortune Drop Boost |
| `ARCADEVIP` | **1,000 Coins** + **50 Tickets** | High Roller VIP Pass |
| `CYBERBOOST` | **500 Coins** + **20 Tickets** + **Cyberpunk Neo Claw** | Unlocks the Laser-Guided Cyber Claw instantly! |
| `FREECOINS` | **250 Coins** | Quick token refill for extra drops |
| `TITANPOWER` | **2,500 Coins** + **100 Tickets** | Quantum Singularity surge to max your upgrades |

*Note: Promo codes are case-insensitive and can be redeemed once per device installation.*

---

## 📱 How to Redeem In-Game

1. Open **Claw Master** on your Android device.
2. Tap the **Gift Box icon** (`🎁`) on the top marquee bar, or go to the **Quests** tab and tap **REDEEM**.
3. Type or paste your code (e.g., `CLAW2026`).
4. Tap **REDEEM REWARD**.
5. Your coins, tickets, or unlocked claws will be instantly credited to your arcade account with sound and haptic confirmation!

---

## 📦 APK Installation Guide

The pre-built Android APK is included directly in this repository:

* **Location:** [`apk/claw-master-debug.apk`](./apk/claw-master-debug.apk)
* **Package Name:** `com.aistudio.clawmaster.vbxzq`
* **Minimum Android:** Android 7.0 (API 24+)
* **Target Android:** Android 15 (API 36)

### How to Install on Android:
1. Download `apk/claw-master-debug.apk` onto your Android phone or tablet.
2. Open the downloaded file from your device's notification or File Manager.
3. If prompted, allow **"Install unknown apps"** from your browser/file manager.
4. Tap **Install** and launch **Claw Master**!

---

## 🛠️ Adding New Codes (For Developers)

To add new promotional codes or seasonal events:
1. Open `app/src/main/java/com/example/data/repository/GameRepository.kt`.
2. Locate the `redeemPromoCode` method.
3. Add a new `when` branch with your desired code, coins, tickets, or claw unlocks:
   ```kotlin
   "SUMMERFUN" -> {
       addCoins(800)
       addTickets(40)
       RedeemResult.Success(800, 40, null, "Summer Arcade Party! +800 Coins")
   }
   ```
4. Rebuild the app with `gradle assembleDebug`.
