package com.example.racegame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class StartActivity : AppCompatActivity() {

    private lateinit var start_BTN: ExtendedFloatingActionButton

    private lateinit var fragment_BTN: ExtendedFloatingActionButton

    private lateinit var speed_switch: SwitchCompat

    private lateinit var move_switch: SwitchCompat

    private var isFast: Boolean = false

    private var isTilt: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViews()
        initViews()

    }

    private fun findViews() {
        start_BTN = findViewById(R.id.start_BTN)
        fragment_BTN = findViewById(R.id.fragment_BTN)
        speed_switch = findViewById(R.id.speed_switch)
        move_switch = findViewById(R.id.move_switch)
    }

    private fun initViews() {
        speed_switch.setOnCheckedChangeListener { _, isChecked ->
            isFast = isChecked
        }
        move_switch.setOnCheckedChangeListener { _, isChecked ->
            isTilt = isChecked
        }
        start_BTN.setOnClickListener{view: View -> changeMainActivity()}
        fragment_BTN.setOnClickListener{view: View -> changeFragmentActivity()}
    }

    private fun changeMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("IS_FAST", isFast)
        intent.putExtra("IS_TILT", isTilt)
        startActivity(intent)
        finish()
    }

    private fun changeFragmentActivity() {
        startActivity(Intent(this, FragmentActivity::class.java))
        finish()
    }

}


