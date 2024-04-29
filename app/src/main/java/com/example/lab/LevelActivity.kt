package com.example.lab

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LevelActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var angleView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        angleView = findViewById(R.id.angle)

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

    private var lastInclination = 0.0

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val g = event.values
//        val norm = sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2])
//        g[0] /= norm
//        g[1] /= norm
//        g[2] /= norm
        val inclination = Math.toDegrees(g[0].toDouble())
       if(Math.abs(inclination - lastInclination) < 1) return
        angleView.text = "$inclination°"
        lastInclination = inclination
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }
}