import os
import math
import subprocess

os.makedirs("docs/media", exist_ok=True)
os.makedirs("/tmp/claw_frames", exist_ok=True)

FONT_BOLD = "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"
FONT_REG = "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"

def run_cmd(cmd):
    subprocess.run(cmd, check=True)

# -------------------------------------------------------------
# 1. HERO BANNER (1200 x 630)
# -------------------------------------------------------------
print("Generating Hero Banner...")
hero_cmd = [
    "convert", "-size", "1200x630", "xc:#070913",
    "-font", FONT_BOLD,
    # Gradient border frame
    "-stroke", "#06B6D4", "-strokewidth", "4", "-fill", "#0B0E1B",
    "-draw", "roundrectangle 16,16,1184,614,24,24",
    # Inner arcade glass panel
    "-stroke", "#1E2545", "-strokewidth", "2", "-fill", "#11162B",
    "-draw", "roundrectangle 32,32,1168,598,18,18",
    # Background neon grid lines
    "-stroke", "#182040", "-strokewidth", "1",
    "-draw", "line 32,200 1168,200",
    "-draw", "line 32,350 1168,350",
    "-draw", "line 32,500 1168,500",
    "-draw", "line 300,32 300,598",
    "-draw", "line 600,32 600,598",
    "-draw", "line 900,32 900,598",
    # Top Marquee Light Bulbs
    "-stroke", "none",
]
# Add glowing light bulbs
colors = ["#FBBF24", "#06B6D4", "#EC4899", "#10B981"]
for i in range(24):
    bx = 60 + i * 46
    hero_cmd.extend(["-fill", colors[i % 4], "-draw", f"circle {bx},54 {bx+4},54"])

# Title & Subtitle
hero_cmd.extend([
    "-fill", "#FBBF24", "-pointsize", "64",
    "-draw", "text 70,160 'CLAW MASTER'",
    "-fill", "#06B6D4", "-pointsize", "30",
    "-draw", "text 540,160 'ARCADE CRANE'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "22",
    "-draw", "text 70,205 'The Ultimate Physics-Driven Arcade Claw Machine Game for Android'",
    # Feature Badges
    "-fill", "#1E293B", "-stroke", "#FBBF24", "-strokewidth", "2",
    "-draw", "roundrectangle 70,250,380,310,12,12",
    "-fill", "#FBBF24", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 90,288 '🎯 REALISTIC PHYSICS'",

    "-fill", "#1E293B", "-stroke", "#06B6D4", "-strokewidth", "2",
    "-draw", "roundrectangle 410,250,720,310,12,12",
    "-fill", "#06B6D4", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 430,288 '⚡ 7 CUSTOM CLAWS'",

    "-fill", "#1E293B", "-stroke", "#EC4899", "-strokewidth", "2",
    "-draw", "roundrectangle 70,335,380,395,12,12",
    "-fill", "#EC4899", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 90,373 '🛠️ MACHINE UPGRADES'",

    "-fill", "#1E293B", "-stroke", "#10B981", "-strokewidth", "2",
    "-draw", "roundrectangle 410,335,720,395,12,12",
    "-fill", "#10B981", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 430,373 '🏆 9 RARE PRIZES'",

    # Tech Stack Pill
    "-fill", "#0F172A", "-stroke", "#334155", "-strokewidth", "1",
    "-draw", "roundrectangle 70,440,720,550,14,14",
    "-fill", "#F8FAFC", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "16",
    "-draw", "text 90,480 'BUILT WITH JETPACK COMPOSE & KOTLIN'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "14",
    "-draw", "text 90,510 '• Edge-to-Edge Material 3  • AudioTrack Sound Synthesizer  • Haptic Feedback'",
    "-draw", "text 90,535 '• Room & SharedPreferences  • Promo Code Redemption  • GitHub Actions CI/CD'",

    # Right Side Graphic: Mechanical Claw & Golden Star Prize
    "-fill", "#1E293B", "-stroke", "#06B6D4", "-strokewidth", "3",
    "-draw", "roundrectangle 770,90,1130,550,20,20",
    # Gantry Rail
    "-fill", "#334155", "-stroke", "#475569", "-strokewidth", "2",
    "-draw", "rectangle 800,120,1100,145",
    # Trolley
    "-fill", "#475569", "-stroke", "#64748B", "-strokewidth", "2",
    "-draw", "roundrectangle 920,115,980,150,6,6",
    # Cable
    "-stroke", "#E2E8F0", "-strokewidth", "4",
    "-draw", "line 950,150 950,270",
    # Laser guide line
    "-stroke", "#EC4899", "-strokewidth", "2",
    "-draw", "line 950,330 950,510",
    # Claw Hub
    "-fill", "#F43F5E", "-stroke", "#FB7185", "-strokewidth", "2",
    "-draw", "roundrectangle 925,270,975,305,8,8",
    "-fill", "#10B981", "-stroke", "none",
    "-draw", "circle 950,287 950,291",
    # Claw Prongs (clamping)
    "-stroke", "#E2E8F0", "-strokewidth", "8", "-fill", "none",
    "-draw", "path 'M 930,305 Q 890,345 915,405'",
    "-draw", "path 'M 970,305 Q 1010,345 985,405'",
    "-draw", "path 'M 950,305 L 950,400'",
    # Golden Star Prize in Claw
    "-stroke", "#D97706", "-strokewidth", "3", "-fill", "#FBBF24",
    "-draw", "polygon 950,340 958,362 982,364 964,380 970,404 950,390 930,404 936,380 918,364 942,362",
    "-fill", "#FFF", "-stroke", "none",
    "-draw", "circle 946,368 946,370",
    "-draw", "circle 954,368 954,370",
    # Sparkles
    "-fill", "#38BDF8",
    "-draw", "polygon 860,260 864,250 868,260 878,264 868,268 864,278 860,268 850,264",
    "-fill", "#F472B6",
    "-draw", "polygon 1050,320 1053,312 1056,320 1064,323 1056,326 1053,334 1050,326 1042,323",
    "-fill", "#10B981", "-font", FONT_BOLD, "-pointsize", "14",
    "-draw", "text 880,530 'READY FOR WINNING DROP'",

    "docs/media/hero_banner.png"
])
run_cmd(hero_cmd)
print("Hero Banner created at docs/media/hero_banner.png")

# -------------------------------------------------------------
# 2. SCREENSHOT: ARCADE MACHINE GAMEPLAY (720 x 1280)
# -------------------------------------------------------------
print("Generating Gameplay Screenshot...")
gameplay_cmd = [
    "convert", "-size", "720x1280", "xc:#0B0E1B",
    "-font", FONT_BOLD,
    # Phone screen border
    "-stroke", "#06B6D4", "-strokewidth", "3", "-fill", "#070913",
    "-draw", "roundrectangle 10,10,710,1270,30,30",

    # TOP MARQUEE BAR
    "-stroke", "#EC4899", "-strokewidth", "2", "-fill", "#14192F",
    "-draw", "roundrectangle 20,20,700,110,18,18",
    # Marquee Bulbs
]
for i in range(14):
    bx = 45 + i * 48
    gameplay_cmd.extend(["-fill", colors[i % 4], "-stroke", "none", "-draw", f"circle {bx},36 {bx+3},36"])

gameplay_cmd.extend([
    "-fill", "#FBBF24", "-pointsize", "22", "-font", FONT_BOLD,
    "-draw", "text 40,75 'CLAW'",
    "-fill", "#06B6D4",
    "-draw", "text 110,75 'MASTER'",
    "-fill", "#64748B", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 40,95 'Claw: Steel Talon (4-Prong)'",
    # Coins Pill
    "-fill", "#2D3766", "-stroke", "#FBBF24", "-strokewidth", "1",
    "-draw", "roundrectangle 420,55,540,95,10,10",
    "-fill", "#FBBF24", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "15",
    "-draw", "text 435,82 '💰 450'",
    # Tickets Pill
    "-fill", "#2D3766", "-stroke", "#EC4899", "-strokewidth", "1",
    "-draw", "roundrectangle 555,55,640,95,10,10",
    "-fill", "#EC4899", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "15",
    "-draw", "text 568,82 '🎟️ 25'",
    # Gift Button
    "-fill", "#2D3766", "-stroke", "#06B6D4", "-strokewidth", "1",
    "-draw", "circle 670,75 686,75",
    "-fill", "#06B6D4", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "14",
    "-draw", "text 662,80 '🎁'",

    # CABINET GLASS INTERIOR
    "-stroke", "#06B6D4", "-strokewidth", "2", "-fill", "#0F1224",
    "-draw", "roundrectangle 20,120,700,880,16,16",
    # Depth grid lines
    "-stroke", "#1E2545", "-strokewidth", "1",
])
for gy in range(650, 880, 30):
    gameplay_cmd.extend(["-draw", f"line 20,{gy} 700,{gy}"])

gameplay_cmd.extend([
    # DROP CHUTE (Bottom Left)
    "-fill", "#020408", "-stroke", "#06B6D4", "-strokewidth", "2",
    "-draw", "roundrectangle 30,680,180,870,12,12",
    # Hazard Stripes on Chute Lip
    "-fill", "#EAB308", "-stroke", "none",
    "-draw", "rectangle 30,680,180,698",
    "-stroke", "#1E293B", "-strokewidth", "5",
    "-draw", "line 40,680 55,698",
    "-draw", "line 70,680 85,698",
    "-draw", "line 100,680 115,698",
    "-draw", "line 130,680 145,698",
    "-draw", "line 160,680 175,698",
    # Arrow in chute
    "-stroke", "none", "-fill", "#10B981",
    "-draw", "polygon 105,740 105,770 120,770 95,800 70,770 85,770 85,740",
    "-fill", "#10B981", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 55,830 'PRIZE CHUTE'",

    # PRIZES IN PIT
    # 1. Teddy Bear (Brown)
    "-fill", "#D97706", "-stroke", "#B45309", "-strokewidth", "2",
    "-draw", "circle 240,820 240,845",
    "-draw", "circle 220,795 220,808",
    "-draw", "circle 260,795 260,808",
    "-fill", "#FEF3C7", "-stroke", "none",
    "-draw", "ellipse 240,828 14,10 0,360",

    # 2. Captain Quack (Yellow Duck)
    "-fill", "#FBBF24", "-stroke", "#D97706", "-strokewidth", "2",
    "-draw", "ellipse 330,830 25,18 0,360",
    "-draw", "circle 345,815 345,828",
    "-fill", "#F97316", "-stroke", "none",
    "-draw", "polygon 358,815 372,819 358,823",

    # 3. Lucky Neko Cat (White/Gold)
    "-fill", "#FDE047", "-stroke", "#EAB308", "-strokewidth", "2",
    "-draw", "circle 425,810 425,835",
    "-draw", "polygon 410,790 400,770 420,780",
    "-draw", "polygon 440,790 450,770 430,780",
    "-fill", "#DC2626", "-stroke", "none",
    "-draw", "roundrectangle 412,830,438,838,3,3",

    # 4. Prism Emerald Gem (Green)
    "-fill", "#10B981", "-stroke", "#6EE7B7", "-strokewidth", "2",
    "-draw", "polygon 510,790 535,800 525,835 495,835 485,800",

    # 5. Cosmic Capsule (Pink/Purple)
    "-fill", "#EC4899", "-stroke", "#8B5CF6", "-strokewidth", "2",
    "-draw", "ellipse 590,820 18,25 0,360",
    "-fill", "#8B5CF6", "-stroke", "none",
    "-draw", "rectangle 572,816,608,824",

    # 6. Robo-Buddy
    "-fill", "#64748B", "-stroke", "#06B6D4", "-strokewidth", "2",
    "-draw", "roundrectangle 630,800,670,840,5,5",
    "-fill", "#06B6D4", "-stroke", "none",
    "-draw", "roundrectangle 638,815,662,825,3,3",

    # GANTRY RAIL & CARRIAGE
    "-fill", "#1E293B", "-stroke", "#334155", "-strokewidth", "2",
    "-draw", "rectangle 20,135,700,165",
    # Trolley
    "-fill", "#475569", "-stroke", "#94A3B8", "-strokewidth", "2",
    "-draw", "roundrectangle 360,130,440,175,6,6",
    # Cable down
    "-stroke", "#CBD5E1", "-strokewidth", "4",
    "-draw", "line 400,175 400,420",
    # Laser Targeting Line down to floor
    "-stroke", "#EC4899", "-strokewidth", "2",
    "-draw", "line 400,470 400,820",
    "-stroke", "#EC4899", "-strokewidth", "2", "-fill", "none",
    "-draw", "circle 400,820 400,835",
    "-fill", "#EC4899", "-stroke", "none",
    "-draw", "circle 400,820 400,823",

    # CLAW ASSEMBLY
    # Hub
    "-fill", "#64748B", "-stroke", "#38BDF8", "-strokewidth", "2",
    "-draw", "roundrectangle 375,420,425,455,8,8",
    "-fill", "#10B981", "-stroke", "none",
    "-draw", "circle 400,437 400,441",
    # 4 Steel Prongs
    "-stroke", "#CBD5E1", "-strokewidth", "7", "-fill", "none",
    "-draw", "path 'M 380,455 Q 340,490 365,545'",
    "-draw", "path 'M 420,455 Q 460,490 435,545'",
    "-draw", "path 'M 393,455 L 390,535'",
    "-draw", "path 'M 407,455 L 410,535'",
    # Cyan tips
    "-fill", "#38BDF8", "-stroke", "none",
    "-draw", "circle 365,545 365,550",
    "-draw", "circle 435,545 435,550",
    "-draw", "circle 390,535 390,540",
    "-draw", "circle 410,535 410,540",

    # Glass reflection
    "-fill", "rgba(255,255,255,0.04)", "-stroke", "none",
    "-draw", "polygon 580,120 680,120 540,880 440,880",

    # CONTROLS PANEL (Bottom)
    "-fill", "#14192F", "-stroke", "#06B6D4", "-strokewidth", "2",
    "-draw", "roundrectangle 20,895,700,1240,22,22",

    # Grip Probability Gauge
    "-fill", "#94A3B8", "-font", FONT_BOLD, "-pointsize", "14",
    "-draw", "text 40,930 'GRIP CHANCE: 85%'",
    "-fill", "#1E293B", "-stroke", "#334155", "-strokewidth", "1",
    "-draw", "roundrectangle 40,945,500,962,8,8",
    "-fill", "#10B981", "-stroke", "none",
    "-draw", "roundrectangle 40,945,430,962,8,8",
    # Restock button
    "-fill", "#2D3766", "-stroke", "#06B6D4", "-strokewidth", "1",
    "-draw", "roundrectangle 530,925,680,965,10,10",
    "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "13",
    "-draw", "text 555,950 '🔄 Shake Pit'",

    # Direction Buttons & DROP Button
    # Left Arrow
    "-fill", "#0F172A", "-stroke", "#06B6D4", "-strokewidth", "3",
    "-draw", "circle 120,1050 155,1050",
    "-fill", "#06B6D4", "-stroke", "none",
    "-draw", "polygon 130,1030 100,1050 130,1070",

    # Right Arrow
    "-fill", "#0F172A", "-stroke", "#06B6D4", "-strokewidth", "3",
    "-draw", "circle 220,1050 255,1050",
    "-fill", "#06B6D4", "-stroke", "none",
    "-draw", "polygon 210,1030 240,1050 210,1070",

    # Big Glowing DROP Button
    "-fill", "#DC2626", "-stroke", "#FBBF24", "-strokewidth", "4",
    "-draw", "circle 560,1050 620,1050",
    "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "22",
    "-draw", "text 525,1048 'DROP'",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 522,1070 '10 COINS'",

    # Slider bar
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "11",
    "-draw", "text 40,1130 'Touch-Slide to Target Crane'",
    "-fill", "#0F172A", "-stroke", "#334155", "-strokewidth", "1",
    "-draw", "roundrectangle 40,1145,680,1185,14,14",
    "-fill", "#06B6D4", "-stroke", "#FFF", "-strokewidth", "2",
    "-draw", "roundrectangle 360,1147,430,1183,10,10",

    # Navigation Bar
    "-fill", "#0B0E1B", "-stroke", "#1E2545", "-strokewidth", "1",
    "-draw", "rectangle 20,1200,700,1260",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 60,1235 '🎮 Arcade'",
    "-fill", "#64748B",
    "-draw", "text 210,1235 '🖐️ Claws'",
    "-draw", "text 360,1235 '🛠️ Upgrades'",
    "-draw", "text 500,1235 '🏆 Prizes'",
    "-draw", "text 630,1235 '📋 Quests'",

    "docs/media/claw_machine_gameplay.png"
])
run_cmd(gameplay_cmd)
print("Gameplay screenshot created at docs/media/claw_machine_gameplay.png")

# -------------------------------------------------------------
# 3. SCREENSHOT: CLAW ARSENAL SHOP (720 x 1280)
# -------------------------------------------------------------
print("Generating Claw Arsenal Screenshot...")
claws_cmd = [
    "convert", "-size", "720x1280", "xc:#0B0E1B",
    "-font", FONT_BOLD,
    "-stroke", "#06B6D4", "-strokewidth", "3", "-fill", "#070913",
    "-draw", "roundrectangle 10,10,710,1270,30,30",
    # Header
    "-fill", "#14192F", "-stroke", "#06B6D4", "-strokewidth", "1",
    "-draw", "roundrectangle 30,30,690,110,16,16",
    "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "22", "-stroke", "none",
    "-draw", "text 50,75 '🖐️ CLAW ARSENAL'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "13",
    "-draw", "text 50,98 '7 Specialized Arcade Claws with High Grip & Unique Perks'",

    # CLAW 1: Steel Talon (Equipped)
    "-fill", "#14192F", "-stroke", "#FBBF24", "-strokewidth", "3",
    "-draw", "roundrectangle 30,130,690,300,18,18",
    "-fill", "#64748B", "-stroke", "#38BDF8", "-strokewidth", "2",
    "-draw", "circle 85,185 115,185",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 135,175 'Steel Talon'",
    "-fill", "#38BDF8", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 135,195 'Heavy Duty Quad-Claw • 4-PRONGS'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,230 'Reinforced carbon-steel prongs for superior hold on heavy toys.'",
    # Perk
    "-fill", "#0F172A", "-stroke", "#38BDF8", "-strokewidth", "1",
    "-draw", "roundrectangle 50,245,450,280,8,8",
    "-fill", "#38BDF8", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 65,268 '⭐ +20% Grip Strength & 4-Point Pincher'",
    # Equipped Badge
    "-fill", "#FBBF24", "-stroke", "none",
    "-draw", "roundrectangle 520,150,670,190,8,8",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 550,175 'EQUIPPED'",

    # CLAW 2: Electro-Magnetizer
    "-fill", "#14192F", "-stroke", "#0284C7", "-strokewidth", "1",
    "-draw", "roundrectangle 30,320,690,490,18,18",
    "-fill", "#0284C7", "-stroke", "#22D3EE", "-strokewidth", "2",
    "-draw", "circle 85,375 115,375",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 135,365 'Electro-Magnetizer'",
    "-fill", "#22D3EE", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 135,385 'Flux Core Attraction • 3-PRONGS'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,420 'Magnetic pulse pulls metallic and capsule prizes directly into claw!'",
    "-fill", "#0F172A", "-stroke", "#22D3EE", "-strokewidth", "1",
    "-draw", "roundrectangle 50,435,480,470,8,8",
    "-fill", "#22D3EE", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 65,458 '⚡ Magnetic Field Pulls Nearby Metallic Prizes'",
    "-fill", "#10B981", "-stroke", "none",
    "-draw", "roundrectangle 520,430,670,475,8,8",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 545,458 '380 COINS'",

    # CLAW 3: Cyberpunk Neo
    "-fill", "#14192F", "-stroke", "#D946EF", "-strokewidth", "1",
    "-draw", "roundrectangle 30,510,690,680,18,18",
    "-fill", "#D946EF", "-stroke", "#06B6D4", "-strokewidth", "2",
    "-draw", "circle 85,565 115,565",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 135,555 'Cyberpunk Neo'",
    "-fill", "#06B6D4", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 135,575 'Laser-Guided Hydraulic • 88% Grip'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,610 'Precision targeting laser beam and zero-wobble high-torque servos.'",
    "-fill", "#0F172A", "-stroke", "#D946EF", "-strokewidth", "1",
    "-draw", "roundrectangle 50,625,480,660,8,8",
    "-fill", "#D946EF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 65,648 '🔴 Built-in Laser Targeting Reticle & Guide'",
    "-fill", "#10B981", "-stroke", "none",
    "-draw", "roundrectangle 520,620,670,665,8,8",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 535,648 '1,800 COINS'",

    # CLAW 4: Quantum Titan
    "-fill", "#14192F", "-stroke", "#8B5CF6", "-strokewidth", "2",
    "-draw", "roundrectangle 30,700,690,870,18,18",
    "-fill", "#8B5CF6", "-stroke", "#EC4899", "-strokewidth", "2",
    "-draw", "circle 85,755 115,755",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
    "-draw", "text 135,745 'Quantum Titan'",
    "-fill", "#EC4899", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 135,765 'Celestial Void Gripper • 98% Base Grip'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,800 'Harnesses quantum gravitational anchors. Drops almost never slip!'",
    "-fill", "#0F172A", "-stroke", "#8B5CF6", "-strokewidth", "1",
    "-draw", "roundrectangle 50,815,480,850,8,8",
    "-fill", "#EC4899", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 65,838 '🌌 Near 100% Grip & Cosmic Singularity Aura'",
    "-fill", "#8B5CF6", "-stroke", "none",
    "-draw", "roundrectangle 520,810,670,855,8,8",
    "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 535,838 '7,500 COINS'",

    # Bottom Nav Bar
    "-fill", "#0B0E1B", "-stroke", "#1E2545", "-strokewidth", "1",
    "-draw", "rectangle 20,1200,700,1260",
    "-fill", "#64748B", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 60,1235 '🎮 Arcade'",
    "-fill", "#FBBF24",
    "-draw", "text 210,1235 '🖐️ Claws'",
    "-fill", "#64748B",
    "-draw", "text 360,1235 '🛠️ Upgrades'",
    "-draw", "text 500,1235 '🏆 Prizes'",
    "-draw", "text 630,1235 '📋 Quests'",

    "docs/media/claw_arsenal_showcase.png"
]
run_cmd(claws_cmd)
print("Claw Arsenal screenshot created at docs/media/claw_arsenal_showcase.png")

# -------------------------------------------------------------
# 4. SCREENSHOT: MACHINE UPGRADES WORKSHOP (720 x 1280)
# -------------------------------------------------------------
print("Generating Upgrades Screenshot...")
upgrades_cmd = [
    "convert", "-size", "720x1280", "xc:#0B0E1B",
    "-font", FONT_BOLD,
    "-stroke", "#06B6D4", "-strokewidth", "3", "-fill", "#070913",
    "-draw", "roundrectangle 10,10,710,1270,30,30",
    # Header
    "-fill", "#14192F", "-stroke", "#FBBF24", "-strokewidth", "1",
    "-draw", "roundrectangle 30,30,690,110,16,16",
    "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "22", "-stroke", "none",
    "-draw", "text 50,75 '🛠️ MACHINE WORKSHOP'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "13",
    "-draw", "text 50,98 'Upgrade machine components to maximize prize winning odds'",

    # Upgrade 1: Hydraulic Grip
    "-fill", "#14192F", "-stroke", "#10B981", "-strokewidth", "1",
    "-draw", "roundrectangle 30,130,690,265,16,16",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "17",
    "-draw", "text 50,165 'Hydraulic Grip Force'",
    "-fill", "#10B981", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 500,165 'LV 4 / 10 (+20% Grip)'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,190 'Closes claws tighter around prizes to prevent slips during ascent.'",
    # Progress
    "-fill", "#1E293B", "-stroke", "none",
    "-draw", "roundrectangle 50,205,480,217,6,6",
    "-fill", "#10B981",
    "-draw", "roundrectangle 50,205,222,217,6,6",
    # Upgrade Button
    "-fill", "#10B981",
    "-draw", "roundrectangle 510,195,670,240,10,10",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 530,223 'UPGRADE (140c)'",

    # Upgrade 2: Laser Guide
    "-fill", "#14192F", "-stroke", "#EC4899", "-strokewidth", "1",
    "-draw", "roundrectangle 30,285,690,420,16,16",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "17",
    "-draw", "text 50,320 'Targeting Laser Sight'",
    "-fill", "#EC4899", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 500,320 'LV 3 / 5 (Active)'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,345 'Projects high-contrast drop laser line and floor targeting reticle.'",
    "-fill", "#1E293B", "-stroke", "none",
    "-draw", "roundrectangle 50,360,480,372,6,6",
    "-fill", "#EC4899",
    "-draw", "roundrectangle 50,360,308,372,6,6",
    "-fill", "#EC4899",
    "-draw", "roundrectangle 510,350,670,395,10,10",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 530,378 'UPGRADE (240c)'",

    # Upgrade 3: Chute Funnel Expander
    "-fill", "#14192F", "-stroke", "#FBBF24", "-strokewidth", "1",
    "-draw", "roundrectangle 30,440,690,575,16,16",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "17",
    "-draw", "text 50,475 'Chute Funnel Expander'",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 500,475 'LV 2 / 6 (+16% Area)'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,500 'Widens the goal drop chute so wobbly prizes still tumble inside.'",
    "-fill", "#1E293B", "-stroke", "none",
    "-draw", "roundrectangle 50,515,480,527,6,6",
    "-fill", "#FBBF24",
    "-draw", "roundrectangle 50,515,193,527,6,6",
    "-fill", "#FBBF24",
    "-draw", "roundrectangle 510,505,670,550,10,10",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 530,533 'UPGRADE (180c)'",

    # Upgrade 4: Anti-Slip Damper
    "-fill", "#14192F", "-stroke", "#38BDF8", "-strokewidth", "1",
    "-draw", "roundrectangle 30,595,690,730,16,16",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "17",
    "-draw", "text 50,630 'Anti-Slip Tension Damper'",
    "-fill", "#38BDF8", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 500,630 'LV 3 / 8 (-30% Slip)'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,655 'Absorbs carriage vibration when claw stops and reverses at top rail.'",
    "-fill", "#1E293B", "-stroke", "none",
    "-draw", "roundrectangle 50,670,480,682,6,6",
    "-fill", "#38BDF8",
    "-draw", "roundrectangle 50,670,211,682,6,6",
    "-fill", "#38BDF8",
    "-draw", "roundrectangle 510,660,670,705,10,10",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 530,688 'UPGRADE (196c)'",

    # Upgrade 5: Prize Fortune Luck
    "-fill", "#14192F", "-stroke", "#A855F7", "-strokewidth", "1",
    "-draw", "roundrectangle 30,750,690,885,16,16",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "17",
    "-draw", "text 50,785 'Arcade Prize Luck'",
    "-fill", "#A855F7", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 500,785 'LV 2 / 8 (+30% Rare)'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "12",
    "-draw", "text 50,810 'Spawns more Rare, Epic, and Jackpot plushies inside the machine pit.'",
    "-fill", "#1E293B", "-stroke", "none",
    "-draw", "roundrectangle 50,825,480,837,6,6",
    "-fill", "#A855F7",
    "-draw", "roundrectangle 50,825,157,837,6,6",
    "-fill", "#A855F7",
    "-draw", "roundrectangle 510,815,670,860,10,10",
    "-fill", "#000", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 530,843 'UPGRADE (279c)'",

    # Bottom Nav Bar
    "-fill", "#0B0E1B", "-stroke", "#1E2545", "-strokewidth", "1",
    "-draw", "rectangle 20,1200,700,1260",
    "-fill", "#64748B", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 60,1235 '🎮 Arcade'",
    "-draw", "text 210,1235 '🖐️ Claws'",
    "-fill", "#FBBF24",
    "-draw", "text 360,1235 '🛠️ Upgrades'",
    "-fill", "#64748B",
    "-draw", "text 500,1235 '🏆 Prizes'",
    "-draw", "text 630,1235 '📋 Quests'",

    "docs/media/machine_upgrades_workshop.png"
]
run_cmd(upgrades_cmd)
print("Upgrades screenshot created at docs/media/machine_upgrades_workshop.png")

# -------------------------------------------------------------
# 5. SCREENSHOT: PRIZE SHOWCASE & TROPHY ROOM (720 x 1280)
# -------------------------------------------------------------
print("Generating Trophy Showcase Screenshot...")
trophies_cmd = [
    "convert", "-size", "720x1280", "xc:#0B0E1B",
    "-font", FONT_BOLD,
    "-stroke", "#06B6D4", "-strokewidth", "3", "-fill", "#070913",
    "-draw", "roundrectangle 10,10,710,1270,30,30",
    # Header Banner
    "-fill", "#14192F", "-stroke", "#FBBF24", "-strokewidth", "1",
    "-draw", "roundrectangle 30,30,690,110,16,16",
    "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "22", "-stroke", "none",
    "-draw", "text 50,75 '🏆 PRIZE SHOWCASE'",
    "-fill", "#10B981", "-font", FONT_BOLD, "-pointsize", "13",
    "-draw", "text 50,98 '7 / 9 Unique Prizes Unlocked • 18 Total Won'",

    # Prize Grid 2x2
    # Item 1: Barnaby Bear
    "-fill", "#14192F", "-stroke", "#10B981", "-strokewidth", "2",
    "-draw", "roundrectangle 30,130,345,340,16,16",
    "-fill", "#10B981", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 45,155 'COMMON • x5 CAUGHT'",
    "-fill", "#D97706", "-stroke", "#B45309", "-strokewidth", "2",
    "-draw", "circle 185,225 185,260",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "16",
    "-draw", "text 95,295 'Barnaby Bear'",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 120,320 '60 Coins Value'",

    # Item 2: Captain Quack
    "-fill", "#14192F", "-stroke", "#10B981", "-strokewidth", "2",
    "-draw", "roundrectangle 375,130,690,340,16,16",
    "-fill", "#10B981", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 390,155 'COMMON • x4 CAUGHT'",
    "-fill", "#FBBF24", "-stroke", "#D97706", "-strokewidth", "2",
    "-draw", "circle 530,225 530,260",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "16",
    "-draw", "text 445,295 'Captain Quack'",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 475,320 '40 Coins Value'",

    # Item 3: Lucky Maneki Cat
    "-fill", "#14192F", "-stroke", "#3B82F6", "-strokewidth", "2",
    "-draw", "roundrectangle 30,360,345,570,16,16",
    "-fill", "#3B82F6", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 45,385 'RARE • x3 CAUGHT'",
    "-fill", "#FDE047", "-stroke", "#EAB308", "-strokewidth", "2",
    "-draw", "circle 185,455 185,490",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "16",
    "-draw", "text 100,525 'Lucky Maneki'",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 100,550 '160 Coins + 8 Tickets'",

    # Item 4: Prism Emerald
    "-fill", "#14192F", "-stroke", "#8B5CF6", "-strokewidth", "2",
    "-draw", "roundrectangle 375,360,690,570,16,16",
    "-fill", "#8B5CF6", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "11",
    "-draw", "text 390,385 'EPIC • x2 CAUGHT'",
    "-fill", "#10B981", "-stroke", "#6EE7B7", "-strokewidth", "2",
    "-draw", "circle 530,455 530,490",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "16",
    "-draw", "text 445,525 'Prism Emerald'",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 440,550 '400 Coins + 25 Tickets'",

    # Item 5: Stardust Unicorn (Jackpot!)
    "-fill", "#14192F", "-stroke", "#EF4444", "-strokewidth", "2",
    "-draw", "roundrectangle 30,590,690,820,18,18",
    "-fill", "#EF4444", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 50,620 '🔥 JACKPOT LEGENDARY • x1 CAUGHT'",
    "-fill", "#F472B6", "-stroke", "#38BDF8", "-strokewidth", "2",
    "-draw", "circle 110,700 150,700",
    "-fill", "#FFF", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "20",
    "-draw", "text 180,685 'Stardust Unicorn'",
    "-fill", "#94A3B8", "-font", FONT_REG, "-pointsize", "13",
    "-draw", "text 180,715 'The holy grail of the arcade pit! Radiant dream horn.'",
    "-fill", "#FBBF24", "-font", FONT_BOLD, "-pointsize", "14",
    "-draw", "text 180,745 'Value: 2,500 Coins + 180 Tickets'",

    # Bottom Nav Bar
    "-fill", "#0B0E1B", "-stroke", "#1E2545", "-strokewidth", "1",
    "-draw", "rectangle 20,1200,700,1260",
    "-fill", "#64748B", "-font", FONT_BOLD, "-pointsize", "12",
    "-draw", "text 60,1235 '🎮 Arcade'",
    "-draw", "text 210,1235 '🖐️ Claws'",
    "-draw", "text 360,1235 '🛠️ Upgrades'",
    "-fill", "#FBBF24",
    "-draw", "text 500,1235 '🏆 Prizes'",
    "-fill", "#64748B",
    "-draw", "text 630,1235 '📋 Quests'",

    "docs/media/prize_collection_trophy.png"
]
run_cmd(trophies_cmd)
print("Trophy Showcase screenshot created at docs/media/prize_collection_trophy.png")

# -------------------------------------------------------------
# 6. ANIMATED GAMEPLAY VIDEO & GIF (600 x 800)
# -------------------------------------------------------------
print("Generating Animated Gameplay Frames (60 frames)...")
num_frames = 60

for f in range(num_frames):
    # Phase calculations:
    # 0-14: Aiming (crane moves right from 250 to 380)
    # 15-28: Drop descent (claw drops from y=160 to y=520)
    # 29-38: Grab close (clamping prize)
    # 39-48: Lift up (claw lifts back to y=160 carrying prize)
    # 49-55: Return to chute (crane moves left to x=100)
    # 56-60: Drop into chute + confetti!

    if f <= 14:
        t = f / 14.0
        cx = 250 + (380 - 250) * t
        cy = 160
        prong_open = 0.65
        prize_grabbed = False
        chute_win = False
    elif f <= 28:
        t = (f - 15) / 13.0
        cx = 380
        cy = 160 + (520 - 160) * t
        prong_open = 1.0
        prize_grabbed = False
        chute_win = False
    elif f <= 38:
        t = (f - 29) / 9.0
        cx = 380
        cy = 520
        prong_open = 1.0 - (0.6 * t)
        prize_grabbed = True
        chute_win = False
    elif f <= 48:
        t = (f - 39) / 9.0
        cx = 380
        cy = 520 - (520 - 160) * t
        prong_open = 0.4
        prize_grabbed = True
        chute_win = False
    elif f <= 54:
        t = (f - 49) / 5.0
        cx = 380 + (90 - 380) * t
        cy = 160
        prong_open = 0.4
        prize_grabbed = True
        chute_win = False
    else:
        cx = 90
        cy = 160
        prong_open = 0.9
        prize_grabbed = False
        chute_win = True

    frame_cmd = [
        "convert", "-size", "600x800", "xc:#0B0E1B",
        "-font", FONT_BOLD,
        # Border
        "-stroke", "#06B6D4", "-strokewidth", "2", "-fill", "#070913",
        "-draw", "roundrectangle 10,10,590,790,20,20",
        # Top Marquee
        "-fill", "#14192F", "-stroke", "#EC4899", "-strokewidth", "1",
        "-draw", "roundrectangle 20,20,580,75,12,12",
        "-fill", "#FBBF24", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "18",
        "-draw", "text 35,55 'CLAW MASTER ARCADE'",
        "-fill", "#06B6D4", "-font", FONT_BOLD, "-pointsize", "14",
        "-draw", "text 440,55 '🪙 500  🎟️ 35'",

        # Cabinet Interior
        "-fill", "#0F1224", "-stroke", "#06B6D4", "-strokewidth", "1",
        "-draw", "roundrectangle 20,85,580,630,12,12",

        # Drop Chute (Bottom Left)
        "-fill", "#020408", "-stroke", "#06B6D4", "-strokewidth", "2",
        "-draw", "roundrectangle 30,500,150,620,10,10",
        "-fill", "#EAB308", "-stroke", "none",
        "-draw", "rectangle 30,500,150,515",
        "-fill", "#10B981", "-font", FONT_BOLD, "-pointsize", "11",
        "-draw", "text 50,570 'WIN CHUTE'",

        # Prizes in pit (Floor is y=580)
        # Duck
        "-fill", "#FBBF24", "-stroke", "#D97706", "-strokewidth", "1",
        "-draw", "circle 240,570 240,590",
        # Cat
        "-fill", "#FDE047", "-stroke", "#EAB308", "-strokewidth", "1",
        "-draw", "circle 300,570 300,590",
        # Emerald Gem
        "-fill", "#10B981", "-stroke", "#6EE7B7", "-strokewidth", "1",
        "-draw", "circle 460,570 460,590",
        # Boba
        "-fill", "#A855F7", "-stroke", "#F3E8FF", "-strokewidth", "1",
        "-draw", "circle 520,570 520,590",
    ]

    # Target Prize (Barnaby Teddy Bear at x=380, y=570)
    if not prize_grabbed and not chute_win:
        frame_cmd.extend([
            "-fill", "#D97706", "-stroke", "#B45309", "-strokewidth", "2",
            "-draw", "circle 380,570 380,595",
            "-draw", "circle 365,550 365,560",
            "-draw", "circle 395,550 395,560",
            "-fill", "#FEF3C7", "-stroke", "none",
            "-draw", "ellipse 380,575 10,8 0,360"
        ])
    elif prize_grabbed:
        # Prize is attached to claw at (cx, cy + 50)
        py = cy + 45
        frame_cmd.extend([
            "-fill", "#D97706", "-stroke", "#B45309", "-strokewidth", "2",
            f"-draw", f"circle {cx},{py} {cx},{py+25}",
            f"-draw", f"circle {cx-15},{py-20} {cx-15},{py-10}",
            f"-draw", f"circle {cx+15},{py-20} {cx+15},{py-10}",
            "-fill", "#FEF3C7", "-stroke", "none",
            f"-draw", f"ellipse {cx},{py+5} 10,8 0,360"
        ])

    # Top Rail & Trolley
    frame_cmd.extend([
        "-fill", "#1E293B", "-stroke", "#334155", "-strokewidth", "1",
        "-draw", "rectangle 20,95,580,115",
        "-fill", "#475569", "-stroke", "#94A3B8", "-strokewidth", "1",
        f"-draw", f"roundrectangle {cx-30},90,{cx+30},120,4,4",
        # Steel Cable
        "-stroke", "#CBD5E1", "-strokewidth", "3",
        f"-draw", f"line {cx},120 {cx},{cy}",
    ])

    # Laser targeting line when aiming
    if f < 25:
        frame_cmd.extend([
            "-stroke", "#EC4899", "-strokewidth", "1.5",
            f"-draw", f"line {cx},{cy+25} {cx},580",
            "-fill", "none", "-stroke", "#EC4899",
            f"-draw", f"circle {cx},580 {cx},590"
        ])

    # Claw Hub & Prongs
    pr = int(35 * prong_open)
    frame_cmd.extend([
        "-fill", "#F43F5E", "-stroke", "#FB7185", "-strokewidth", "2",
        f"-draw", f"roundrectangle {cx-22},{cy},{cx+22},{cy+25},6,6",
        "-stroke", "#E2E8F0", "-strokewidth", "6", "-fill", "none",
        f"-draw", f"path 'M {cx-18},{cy+25} Q {cx-pr-10},{cy+40} {cx-pr},{cy+65}'",
        f"-draw", f"path 'M {cx+18},{cy+25} Q {cx+pr+10},{cy+40} {cx+pr},{cy+65}'",
        f"-draw", f"line {cx},{cy+25} {cx},{cy+60}"
    ])

    # Confetti & Celebration when winning in chute (f >= 55)
    if chute_win:
        cf_colors = ["#FBBF24", "#EC4899", "#38BDF8", "#10B981", "#EF4444"]
        frame_cmd.extend([
            "-stroke", "#FBBF24", "-strokewidth", "2", "-fill", "#14192F",
            "-draw", "roundrectangle 120,250,480,360,16,16",
            "-fill", "#FBBF24", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "22",
            "-draw", "text 180,300 '🎉 PRIZE WON! 🎉'",
            "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "14",
            "-draw", "text 210,335 '+60 COINS  •  +2 TICKETS'"
        ])
        for p in range(25):
            px = 50 + (p * 23) % 480
            py = 180 + (p * 37) % 400
            pc = cf_colors[p % len(cf_colors)]
            frame_cmd.extend(["-fill", pc, "-stroke", "none", f"-draw", f"circle {px},{py} {px+5},{py}"])

    # Bottom Control Bar
    frame_cmd.extend([
        "-fill", "#14192F", "-stroke", "#06B6D4", "-strokewidth", "1",
        "-draw", "roundrectangle 20,645,580,780,14,14",
        "-fill", "#94A3B8", "-stroke", "none", "-font", FONT_BOLD, "-pointsize", "12",
        "-draw", "text 40,675 'GRIP PROBABILITY: 85%'",
        # Small buttons
        "-fill", "#0F172A", "-stroke", "#06B6D4", "-strokewidth", "2",
        "-draw", "circle 80,725 105,725",
        "-fill", "#06B6D4", "-stroke", "none",
        "-draw", "polygon 88,715 68,725 88,735",
        "-fill", "#0F172A", "-stroke", "#06B6D4", "-strokewidth", "2",
        "-draw", "circle 150,725 175,725",
        "-fill", "#06B6D4", "-stroke", "none",
        "-draw", "polygon 142,715 162,725 142,735",
        # Big Drop button
        "-fill", "#DC2626" if f > 14 else "#EF4444", "-stroke", "#FBBF24", "-strokewidth", "3",
        "-draw", "circle 480,715 525,715",
        "-fill", "#FFF", "-font", FONT_BOLD, "-pointsize", "16",
        "-draw", "text 455,720 'DROP'",

        f"/tmp/claw_frames/frame_{f:03d}.png"
    ])
    run_cmd(frame_cmd)

print("Frames generated. Creating MP4 video and animated GIF...")

# Render MP4 video using ffmpeg
video_cmd = [
    "ffmpeg", "-y",
    "-framerate", "15",
    "-i", "/tmp/claw_frames/frame_%03d.png",
    "-c:v", "libx264",
    "-pix_fmt", "yuv420p",
    "-movflags", "+faststart",
    "docs/media/gameplay_demo.mp4"
]
run_cmd(video_cmd)
print("MP4 video created at docs/media/gameplay_demo.mp4")

# Render Animated GIF for GitHub README preview
gif_cmd = [
    "ffmpeg", "-y",
    "-framerate", "12",
    "-i", "/tmp/claw_frames/frame_%03d.png",
    "-vf", "fps=12,scale=480:-1:flags=lanczos,split[s0][s1];[s0]palettegen[p];[s1][p]paletteuse",
    "docs/media/gameplay_preview.gif"
]
run_cmd(gif_cmd)
print("Animated GIF created at docs/media/gameplay_preview.gif")

print("All photos and videos generated successfully!")
