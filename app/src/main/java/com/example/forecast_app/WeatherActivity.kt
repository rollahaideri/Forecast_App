package com.example.forecast_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        findViewById<Button>(R.id.search_btn).setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java )
            startActivity(intent)
        }
        val temp = findViewById<TextView>(R.id.temp)
        val cityName = findViewById<TextView>(R.id.id_CityName)
        val tempLow = findViewById<TextView>(R.id.id_tempLow)
        val tempHigh = findViewById<TextView>(R.id.id_tempLHigh)
        val desc = findViewById<TextView>(R.id.id_desc)

        val intentName = intent.getStringExtra("cityName")
        val intentTemp = intent.getStringExtra("temp")
        val intentTempLow = intent.getStringExtra("temp_min")
        val intentTempHigh = intent.getStringExtra("temp_max")
        val intentDesc = intent.getStringExtra("describe")

        temp.text = intentTemp
        cityName.text = intentName
        tempLow.text = intentTempLow
        tempHigh.text = intentTempHigh
        desc.text = intentDesc


    }
}