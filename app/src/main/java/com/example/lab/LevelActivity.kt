package com.example.lab

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class LevelActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var angleView: TextView
    private lateinit var moving_stick: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        angleView = findViewById(R.id.angle)
        moving_stick = findViewById(R.id.moving_stick)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { accelerometer ->
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private var lastInclination: Double? = null


    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0] // that is z for extended controls
        val y = event.values[1]
        val z = event.values[2]

        val percentage = x / 9.81f

        val steerRotation = percentage * 180

        if (lastInclination != null && Math.abs(lastInclination!! - steerRotation.toDouble()) < 0.2) return

        val formattedNumber = String.format("%.2f", steerRotation)
        angleView.text = "$formattedNumber°"
        moving_stick.rotation = steerRotation
        lastInclination = steerRotation.toDouble()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }
}