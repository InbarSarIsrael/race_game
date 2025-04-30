package com.example.racegame.logic

import com.example.racegame.utilities.SignalManager

class GameManager(private val lifeCount: Int = 3) { // default life

    companion object
    {
        const val RIGHT = 1
        const val LEFT = -1
        const val POS_LEFT = 0
        const val POS_MIDDLE = 1
        const val POS_RIGHT = 2
    }

    var crashCount: Int = 0 // the numbers of crash
        private set

    var carPosition = POS_MIDDLE
        private set

    var currentObstaclePosition = -1

    val isGameOver: Boolean
        get() = crashCount == lifeCount


    fun moveCar(move: Int)
    {
        // move to the side
        carPosition += move
        if (carPosition < POS_LEFT) carPosition = POS_LEFT
        if (carPosition > POS_RIGHT) carPosition = POS_RIGHT
    }
// -----------------------------------
    fun generateObstaclePosition(): Int {
        currentObstaclePosition = (0..2).random()
        return currentObstaclePosition
    }

    fun checkCrash()
    {
        // check if there is a crack between obstacle and car
        if(currentObstaclePosition == carPosition)
        {
            crashCount++

            SignalManager
                .getInstance()
                .toast("Crash!")
            SignalManager
                .getInstance()
                .vibrate()
        }
    }
}