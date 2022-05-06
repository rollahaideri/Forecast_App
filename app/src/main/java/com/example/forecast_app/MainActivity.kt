package com.example.forecast_app

import android.annotation.SuppressLint

import android.content.Intent


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private val itemsList = ArrayList<String>() // Initialising our i
    private lateinit var customAdapter: CustomAdapter
    // private lateinit var myQuery : String

    // Checking type safety
    private var weatherData: TextView? = null
    private var describe1: TextView? = null
    private var temp_min: TextView? = null
    private var temp_max: TextView? = null
    private var cityName: TextView? = null
    private var weatherInfo: LinearLayout? = null
    private var image: ImageView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        // Defined components
        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_searchbar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(applicationContext)
        customAdapter = CustomAdapter(itemsList)

        // Invoke our getCurrentData function on submitting a new search
        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(pO: String?): Boolean {
                viewModel.myQuery = pO.toString()
                getCurrentData()

                searchView.clearFocus()
                searchView.setQuery("",false)

                return false

            }

            override fun onQueryTextChange(pO: String?): Boolean {
                customAdapter.getFilter().filter(pO)
                //recyclerView.visibility = View.VISIBLE


                return false
            }
        })


        // NEW - Interface (on Item Click)
        customAdapter.setOnItemClickListener(object: CustomAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                viewModel.myQuery = customAdapter.itemFilterList[position] // Set the clicked item value to a variable

                recyclerView.visibility = View.GONE  //Make our Recycler view Gone
                searchView.clearFocus()
                searchView.setQuery("",false)
                getCurrentData()
            }
        })

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        prepareItems()


        // Defined components
        weatherData = findViewById(R.id.tv_temp)
        describe1 = findViewById(R.id.tv_describe)
        temp_min = findViewById(R.id.temp_min)
        temp_max = findViewById(R.id.temp_max)
        cityName = findViewById(R.id.city_name)

        weatherInfo = findViewById(R.id.RL_weatherInfo)
        image = findViewById(R.id.imageView)



        cityName!!.text = viewModel.cityName
        weatherData!!.text = viewModel.temp
        describe1!!.text = viewModel.describe
        temp_min!!.text = viewModel.temp_min
        temp_max!!.text = viewModel.temp_max



        // Intent onClick to WeatherActivity + passing values
        weatherInfo!!.setOnClickListener(){
            val name = cityName!!.text.toString()
            val temp = weatherData!!.text.toString()
            val tempLow = temp_min!!.text.toString()
            val tempHigh = temp_max!!.text.toString()
            val desc = describe1!!.text.toString()
            val intent = Intent(this@MainActivity, WeatherActivity::class.java)
            .apply{
                putExtra("cityName",name)
                putExtra("temp", temp)
                putExtra("temp_min",tempLow)
                putExtra("temp_max", tempHigh)
                putExtra("describe", desc)
            }

            startActivity(intent)
        }

    } // OnCreate scope ends here

    // Main functionality of a API call
    private fun getCurrentData() {

        // Build Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        // Connection
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(viewModel.myQuery,"metric", AppId)

        // Implementation
        call.enqueue(object : Callback<WeatherResponse> {

            // functionality of successful API
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!! // The response object

                    weatherInfo?.visibility = View.VISIBLE  // Make our WeatherInfo view visible

                    viewModel.temp = "${weatherResponse.main!!.temp.toInt()}" + "°"
                    viewModel.cityName = weatherResponse.name.toString()
                    viewModel.describe = "${weatherResponse.weather[0].description}"
                    viewModel.temp_min = " L: ${weatherResponse.main!!.temp_min.toInt()}" + "°C"
                    viewModel.temp_max = " H: ${weatherResponse.main!!.temp_max.toInt()}" + "°C"
                    val iconView = weatherResponse.weather[0].icon

                    Picasso.get()
                        .load(iconView)
                        .into(image!!)

                    // Set components text to our response values
                    weatherData!!.text = viewModel.temp
                    describe1!!.text = viewModel.describe
                    temp_min!!.text = viewModel.temp_min
                    temp_max!!.text = viewModel.temp_max
                    cityName!!.text = viewModel.cityName


                }
            }

            // Functionality on unsuccessful API
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                weatherData!!.text = t.message
            }
        })
    }

    companion object {
        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "c28b2af665d69602d625164a666659a7"
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun prepareItems() {
        itemsList.add("Stockholm")
        itemsList.add("Gothenburg")
        itemsList.add("Malmö")
        itemsList.add("Uppsala")
        itemsList.add("Västerås ")
        itemsList.add("Örebro")
        itemsList.add("Linköping")
        itemsList.add("Helsingborg")
        itemsList.add("Jönköping")
        itemsList.add("Norrköping")
        itemsList.add("Lund")
        itemsList.add("Umeå")
        itemsList.add("Gävle")
        itemsList.add("Borås")
        itemsList.add("Södertälje")
        itemsList.add("Eskilstuna")

        customAdapter.notifyDataSetChanged()
    }
}
