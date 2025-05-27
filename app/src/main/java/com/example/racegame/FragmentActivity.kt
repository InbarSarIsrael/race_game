package com.example.racegame

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.racegame.interfaces.Callback_HighScoreItemClicked
import com.example.racegame.models.Score
import com.example.racegame.ui.HighScoreFragment
import com.example.racegame.ui.MapFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class FragmentActivity : AppCompatActivity(), HighScoreFragment.OnScoreSelectedListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private lateinit var main_FRAME_list: FrameLayout

    private lateinit var mapFragment: MapFragment

    private lateinit var highScoreFragment: HighScoreFragment

    private lateinit var start_BTN: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fragment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()

        if (!hasLocationPermission()) {
            requestLocationPermission()
        }
        val score = intent.getSerializableExtra("NEW_SCORE") as? Score

        mapFragment = supportFragmentManager.findFragmentById(R.id.main_FRAME_map) as MapFragment

        score?.let {
            window.decorView.postDelayed({
                val (lat, lon) = score.getLatLon()
                mapFragment.zoom(lat, lon)
            }, 500)
        }
    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        start_BTN = findViewById(R.id.start_BTN)

    }

    private fun initViews() {
        start_BTN.setOnClickListener{view: View -> changeActivity()}


        highScoreFragment = HighScoreFragment()
        highScoreFragment.highScoreItemClicked =
            object : Callback_HighScoreItemClicked {
                override fun highScoreItemClicked(lat: Double, lon: Double) {
                    mapFragment.zoom(lat, lon)
                }
            }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_FRAME_list, highScoreFragment)
            .commit()
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onScoreSelected(score: Score) {
        val (lat, lon) = score.getLatLon()
        mapFragment.zoom(lat, lon)
    }

    private fun changeActivity() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}


