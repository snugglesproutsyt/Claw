# 🕹️ Claw Master - Arcade Claw Machine Game

**Claw Master** is a vibrant, retro-futuristic arcade claw machine game built with **Kotlin** and **Jetpack Compose**. Players control the motorized carriage, aim for prized plushies and gems, upgrade machine mechanics to increase win probabilities, and purchase powerful custom claws.

![Android 15](https://img.shields.io/badge/Android-15%20(API%2036)-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?logo=jetpackcompose&logoColor=white)

---

## 📥 Download APK

You can download and install the latest pre-compiled debug APK directly from this repository:

👉 **[Download claw-master-debug.apk](./apk/claw-master-debug.apk)** *(~22 MB)*

* **Package:** `com.aistudio.clawmaster.vbxzq`
* **Version:** 1.0
* **Target SDK:** Android 15 (API 36)
* **Min SDK:** Android 7.0 (API 24)

---

## 🎁 Gift & Promo Codes

Check out **[REDEEM.md](./REDEEM.md)** or **[redeem_codes.json](./redeem_codes.json)** for active promo codes!

| Promo Code | Rewards |
| :--- | :--- |
| `CLAW2026` | **+500 Coins** + **25 Tickets** |
| `LUCKYDROP` | **+300 Coins** + **15 Tickets** |
| `ARCADEVIP` | **+1,000 Coins** + **50 Tickets** |
| `CYBERBOOST` | **+500 Coins** + **20 Tickets** + **Cyberpunk Neo Claw Unlocked!** |
| `FREECOINS` | **+250 Coins** |
| `TITANPOWER` | **+2,500 Coins** + **100 Tickets** |

### How to Redeem:
1. Tap the **Gift Box (`🎁`)** on the top marquee bar or the **Redeem** button in the **Quests** tab.
2. Enter any active code.
3. Enjoy instant coins, tickets, and unlocked claws!

---

## 🌟 Key Features

* **Physics-Driven Crane Simulation**:
  * Real-time Canvas rendering of the gantry rails, motorized trolley, steel cable, and articulated claws.
  * Tactile Left/Right directional buttons and precision touch-slider.
  * Glowing arcade "DROP" button with haptic feedback.
  * Drop chute with hazard stripes and win sensors triggering confetti celebrations.

* **Claw Arsenal (7 Collectible Claws)**:
  * **Arcade Standard**: Classic 3-prong starter.
  * **Steel Talon**: 4-prong heavy-duty carbon steel claws (+20% grip).
  * **Electro-Magnetizer**: Electromagnetic pulse pulling metallic prizes into the claw center.
  * **Gilded Sovereign**: 24K gold-plated claw providing +75% tickets and +50% coins.
  * **Cyberpunk Neo**: Laser targeting reticle and zero-wobble precision.
  * **Dragon's Maw**: Massive scoop span to grab giant jackpot plushies.
  * **Quantum Titan**: Celestial void gripper with near 100% grip strength.

* **Machine Workshop Upgrades**:
  * **Hydraulic Grip Force**: Tightens claws to prevent slips (+5% per level).
  * **Gantry Motor Speed**: Faster crane transit and return cycles.
  * **Targeting Laser Sight**: Projects an accurate drop laser line and floor reticle.
  * **Chute Funnel Expander**: Widens the drop chute goal area.
  * **Anti-Slip Tension Damper**: Eliminates top-rail jolts and mid-air slips.
  * **Arcade Prize Luck**: Boosts spawn chances for Rare, Epic, and Legendary prizes.
  * **Token Payday Multiplier**: Boosts payout values from won prizes.

* **Trophy Room & Collection**:
  * 9 collectible prizes: Barnaby Bear, Captain Quack, Lucky Cat, Mecha Bot, Taro Boba, Prism Emerald, Cosmic Egg, Monarch Crown, and Stardust Unicorn.
  * Sell duplicate prizes for extra coin payouts!

* **Missions & Career Records**:
  * Daily quests, career win rate tracking, total drops, and lifetime earnings.

---

## 🚀 Building from Source

To build the APK locally using Gradle:

```bash
# Assemble Debug APK
gradle assembleDebug

# Output location:
# app/build/outputs/apk/debug/app-debug.apk
```

Or trigger the included GitHub Actions workflow at `.github/workflows/build-apk.yml`.
