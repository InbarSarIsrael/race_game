package com.example.racegame

import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.racegame.models.Score
import com.example.racegame.utilities.Constants
import com.example.racegame.utilities.HighScoreManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

private lateinit var end_LBL_message: MaterialTextView
private lateinit var end_BTN_save: ExtendedFloatingActionButton
private lateinit var end_ET_name: TextInputEditText
private var score: Int = 0


class EndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_end)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()

        end_BTN_save.setOnClickListener {
            val name = end_ET_name.text.toString().ifBlank { "Player" }

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val location = if (hasPermission)
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            else null
            val lat = location?.latitude ?: 0.0
            val lon = location?.longitude ?: 0.0
            val locationStr = "$lat,$lon"

            val highScore = Score(name, score, locationStr)
            HighScoreManager.saveHighScore(this, highScore)

            changeActivity()
        }
    }

    private fun findViews() {
        end_LBL_message = findViewById(R.id.end_LBL_message)
        end_BTN_save = findViewById(R.id.end_BTN_save)
        end_ET_name = findViewById(R.id.end_ET_name)
    }

    private fun initViews() {
        val message = intent.getStringExtra(Constants.BundleKeys.MESSAGE_KEY) ?: "🤷🏻‍♂️ Unknown Status"
        score = intent.getIntExtra(Constants.BundleKeys.SCORE_KEY, 0)

        end_LBL_message.text = message
    }

    private fun changeActivity() {
        startActivity(Intent(this, FragmentActivity::class.java))
        finish()
    }
}

