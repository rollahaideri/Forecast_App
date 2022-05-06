package com.example.forecast_app


import android.widget.LinearLayout
import androidx.lifecycle.ViewModel

class WeatherViewModel: ViewModel() {
    lateinit var myQuery: String
    var cityName = ""
    var temp = ""
    var temp_min = ""
    var temp_max = ""
    var describe = ""

    // TODO: Add the rest of values
}