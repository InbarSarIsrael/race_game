package com.example.racegame.utilities


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.racegame.interfaces.TiltCallback
import com.example.racegame.logic.GameManager
import kotlin.math.abs

class TiltDetector(context: Context, private val tiltCallback: TiltCallback?) {

    private val sensorManager = context
        .getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor

    private lateinit var sensorEventListener: SensorEventListener

    var tiltCounterX: Int = 0
        private set

    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                calculateTilt(x)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //pass
            }
        }
    }

    private fun calculateTilt(x: Float) {
        if (System.currentTimeMillis() - timestamp >= 300) {
            timestamp = System.currentTimeMillis()
            if (x >= 2.0) {
                tiltCallback?.onTilt(GameManager.LEFT)
            } else if(x < -2.0) {
                tiltCallback?.onTilt(GameManager.RIGHT)
            }
        }
    }

    fun start() {
        sensorManager
            .registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
    }

    fun stop() {
        sensorManager
            .unregisterListener(
                sensorEventListener,
                sensor
            )
    }
}