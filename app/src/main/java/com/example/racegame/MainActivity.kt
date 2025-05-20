package com.example.racegame

import android.content.Intent
import android.content.res.Resources
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
import com.example.racegame.utilities.BackgroundMusicPlayer
import com.example.racegame.utilities.SignalManager
import com.example.racegame.utilities.SingleSoundPlayer
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

private lateinit var main_BTN_left: ExtendedFloatingActionButton

private lateinit var main_BTN_right: ExtendedFloatingActionButton

private lateinit var main_IMG_car: Array<AppCompatImageView>

private lateinit var main_IMG_hearts: Array<AppCompatImageView>

private lateinit var main_LBL_score: MaterialTextView

private lateinit var obstacles: Array<Array<AppCompatImageView>>

private lateinit var coins: Array<Array<AppCompatImageView>>

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
        startObstacleCoinsLoop()
    }

    private fun initViews() {
        main_LBL_score.text = gameManager.score.toString()
        main_BTN_right.setOnClickListener { view: View -> moveClicked(GameManager.RIGHT) } // 1 for right
        main_BTN_left.setOnClickListener { view: View -> moveClicked(GameManager.LEFT) } // -1 for left
    }

    private fun findViews() {
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_LBL_score = findViewById(R.id.main_LBL_score)

        main_IMG_car = arrayOf(
            findViewById(R.id.car1),
            findViewById(R.id.car2),
            findViewById(R.id.car3),
            findViewById(R.id.car4),
            findViewById(R.id.car5)
        )

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        obstacles = Array(5) { row ->
            Array(5) { col ->
                val index = row * 5 + col + 1
                val resId = resources.getIdentifier("obstacle$index", "id", packageName)
                if (resId == 0)
                    throw Resources.NotFoundException("Missing obstacle ID: obstacle$index")
                findViewById<AppCompatImageView>(resId)
            }
        }

        coins = Array(5) { row ->
            Array(5) { col ->
                val index = row * 5 + col + 1
                val resId = resources.getIdentifier("coin$index", "id", packageName)
                if (resId == 0)
                    throw Resources.NotFoundException("Missing coin ID: coin$index")
                findViewById<AppCompatImageView>(resId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //tiltDetector.start()
       BackgroundMusicPlayer.getInstance().playMusic()
    }

    override fun onPause() {
        super.onPause()
        //tiltDetector.stop()
        BackgroundMusicPlayer.getInstance().pauseMusic()
    }

    private fun startObstacleCoinsLoop() {
        obstacleRunnable = object : Runnable {
            override fun run() {
                refreshElements()
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

    private fun refreshElements() {
        moveElements(obstacles, true)
        moveElements(coins, false)
        checkCollisionOnMove()
        if(gameManager.generateElement())
            handleGameEvents { newElement(obstacles, 1) }
        else
            handleGameEvents { newElement(coins, 0)}

    }

    private fun newElement(elements : Array<Array<AppCompatImageView>>, isObstacle: Int) {
        var elementPosition = gameManager.generateElementPosition(isObstacle)

        for (i in elements[0].indices) {
            elements[0][i].visibility = if (i == elementPosition) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun moveElements(elements : Array<Array<AppCompatImageView>>, isObstacle : Boolean ) {
        for (i in elements[4].indices)
            elements[4][i].visibility = View.INVISIBLE

        for (i in (elements.size) - 2 downTo 0){
            for (j in elements[i].indices) {
                if (elements[i][j].isVisible) {
                    elements[i+1][j].visibility = View.VISIBLE
                    elements[i][j].visibility = View.INVISIBLE
                    if((i+1) == 4)
                    {
                        if (isObstacle)
                            gameManager.currentObstaclePosition = j
                        else
                            gameManager.currentCoinPosition = j
                    }
                }
            }
        }
    }

    private fun moveClicked(direction: Int) {
        gameManager.moveCar(direction)
        updateCarPosition()
        checkCollisionOnMove()
        refreshUI()
    }

    private fun checkCollisionOnMove() {
        val carPos = gameManager.carPosition
        val lastRow = obstacles.size - 1

        if (obstacles[lastRow][carPos].isVisible) {
            if (gameManager.checkCrack()) {
                SingleSoundPlayer(this).playSound(R.raw.boom)
            }
        }

        if (coins[lastRow][carPos].isVisible) {
            if (gameManager.checkScore()) {
                SingleSoundPlayer(this).playSound(R.raw.coin_music)
            }
        }
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
            main_LBL_score.text = gameManager.score.toString()
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