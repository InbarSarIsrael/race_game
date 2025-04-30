package com.example.racegame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.racegame.utilities.Constants
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import androidx.core.view.isVisible
import com.example.racegame.logic.GameManager
import android.os.Handler
import android.os.Looper
import com.example.racegame.utilities.SignalManager

private lateinit var main_BTN_left: ExtendedFloatingActionButton

private lateinit var main_BTN_right: ExtendedFloatingActionButton

private lateinit var main_IMG_car: Array<AppCompatImageView>

private lateinit var main_IMG_hearts: Array<AppCompatImageView>

private lateinit var obstacles: Array<Array<AppCompatImageView>>

private lateinit var gameManager: GameManager

private val obstacleHandler = Handler(Looper.getMainLooper())

private lateinit var obstacleRunnable: Runnable

private const val OBSTACLE_INTERVAL: Long = 800L

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        SignalManager.init(this)
        findViews()
        gameManager = GameManager(main_IMG_hearts.size)
        initViews()
        startObstacleLoop()
    }

    private fun initViews() {
        main_BTN_right.setOnClickListener { view: View -> moveClicked(GameManager.RIGHT) } // 1 for right
        main_BTN_left.setOnClickListener { view: View -> moveClicked(GameManager.LEFT) } // -1 for left
    }

    private fun findViews() {
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_BTN_left = findViewById(R.id.main_BTN_left)

        main_IMG_car = arrayOf(
            findViewById(R.id.car_left),
            findViewById(R.id.car_middle),
            findViewById(R.id.car_right)
        )

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        obstacles = arrayOf(
            arrayOf(
                findViewById(R.id.obstacle1),
                findViewById(R.id.obstacle2),
                findViewById(R.id.obstacle3)
            ),
            arrayOf(
                findViewById(R.id.obstacle4),
                findViewById(R.id.obstacle5),
                findViewById(R.id.obstacle6)
            ),
            arrayOf(
                findViewById(R.id.obstacle7),
                findViewById(R.id.obstacle8),
                findViewById(R.id.obstacle9)
            ),
            arrayOf(
                findViewById(R.id.obstacle10),
                findViewById(R.id.obstacle11),
                findViewById(R.id.obstacle12)
            ))
    }

    private fun startObstacleLoop() {
        obstacleRunnable = object : Runnable {
            override fun run() {
                refreshObstacles()
                if (!gameManager.isGameOver) {
                    obstacleHandler.postDelayed(this, OBSTACLE_INTERVAL)
                }
            }
        }
        obstacleHandler.postDelayed(obstacleRunnable, OBSTACLE_INTERVAL)
    }

    private fun updateCarPosition() {
        for (i in main_IMG_car.indices) {
            main_IMG_car[i].visibility = if (i == gameManager.carPosition) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun refreshObstacles() {
        moveObstacles()
        handleGameEvents { newObstacle() }
    }

    private fun newObstacle() {
        val obstaclePosition = gameManager.generateObstaclePosition()
        for (i in obstacles[0].indices)
            obstacles[0][i].visibility = if (i == obstaclePosition) View.VISIBLE else View.INVISIBLE
    }

    private fun moveObstacles() {
        for (i in obstacles[3].indices)
            obstacles[3][i].visibility = View.INVISIBLE

        for (i in (obstacles.size) - 2 downTo 0){
            for (j in obstacles[i].indices) {
                if (obstacles[i][j].isVisible) {
                    obstacles[i+1][j].visibility = View.VISIBLE
                    obstacles[i][j].visibility = View.INVISIBLE
                    if(i==2) {
                        gameManager.currentObstaclePosition = j
                        gameManager.checkCrash()
                    }
                }
            }
        }
    }

    private fun moveClicked(direction: Int) {
        gameManager.moveCar(direction)
        updateCarPosition()
        refreshUI()
    }

    private fun refreshUI() {
        handleGameEvents { updateCarPosition() }
    }

    private fun handleGameEvents(update: () -> Unit) {
        if (gameManager.isGameOver) {
            changeActivity("Game Over! 😭")
        } else {
            update()
            if (gameManager.crashCount != 0) {
                main_IMG_hearts[main_IMG_hearts.size - gameManager.crashCount].visibility = View.INVISIBLE
            }
        }
    }

    private fun changeActivity(message: String) {
        val intent = Intent(this, EndActivity::class.java)
        var bundle = Bundle()
        bundle.putString(Constants.BundleKeys.MESSAGE_KEY , message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }
}