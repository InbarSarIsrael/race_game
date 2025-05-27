# 🚗 RaceGame - Android Racing Game

Welcome to **RaceGame**, a fun and interactive Android racing game where the player controls a car that dodges obstacles and collects coins. The game supports both button-based and tilt-based controls for a dynamic experience.

## 🎮 Features

- Smooth car control using:
    - Left and right buttons
    - Tilt sensor (accelerometer)
- Obstacle and coin generation with increasing challenge
- Health system with visual heart indicators
- Real-time scoring and sound effects
- End screen with score summary
- High score system integrated with Google Maps (WIP)

## 🧠 Gameplay Logic

- The game board is 5 columns wide.
- Obstacles and coins move from the top to the bottom of the screen.
- If the car collides with an obstacle: lose a heart.
- If the car collects a coin: gain a point.
- Game ends when all lives are lost.

## 🧱 Project Structure

- `MainActivity.kt`: Core game loop and logic
- `GameManager.kt`: Handles game state (score, lives, element generation)
- `TiltDetector.kt`: Detects tilt input (if enabled)
- `EndActivity.kt`: Shows game over screen
- `MapFragment.kt`: Shows location of high score players on Google Maps
- `HighScoreFragment.kt`: Displays high scores in a RecyclerView

🛠️ Tech Stack
* Language: Kotlin
* Platform: Android (minSdk 26)
* UI: XML layouts, Material Design components
* Architecture: Fragment-based split screen layout
* Tools: ViewBinding, RecyclerView, Google Maps SDK

🚀 Getting Started
1. Clone the repo
2. Open in Android Studio.
3. Add your Google Maps API key.
4. Run on emulator or physical device.
