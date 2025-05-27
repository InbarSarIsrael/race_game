package com.example.racegame.logic

import androidx.collection.emptyLongList
import com.example.racegame.utilities.SignalManager
import com.example.racegame.utilities.SingleSoundPlayer

class GameManager(private val lifeCount: Int = 3) { // default life

    companion object
    {
        const val RIGHT = 1
        const val LEFT = -1
        const val NUM_POSITIONS = 5
        const val OBSTACLE = 1
        const val COIN = 0

    }

    var crashCount: Int = 0 // the numbers of crash
        private set

    var score: Int = 0
        private set

    var carPosition = 2 // middle
        private set

    var currentObstaclePosition = -1

    var currentCoinPosition = -1

    val isGameOver: Boolean
        get() = crashCount == lifeCount


    fun moveCar(move: Int)
    {
        // move to the side
        carPosition += move
        if (carPosition < 0) carPosition = 0
        if (carPosition > (NUM_POSITIONS-1)) carPosition = NUM_POSITIONS-1
    }

    fun generateElementPosition(element: Int): Int {
        var elementPosition = (0..4).random()
        if(element == OBSTACLE)
            currentObstaclePosition = elementPosition
        else
            currentCoinPosition = elementPosition
        return elementPosition
    }

    fun generateElement(): Boolean {
        return (0..2).random() == OBSTACLE
    }

    fun checkCrack() : Boolean
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

            return true
        }
        return false
    }
    fun checkScore() : Boolean
    {
        if (currentCoinPosition == carPosition)
        {
            score++
            return true
        }
        return false
    }
}