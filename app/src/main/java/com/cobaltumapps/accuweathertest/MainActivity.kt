package com.cobaltumapps.accuweathertest

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "DebugTag"

    private val apiKey = "phJRvPU89rXpWUHRc9yllGMvRvkXdKBo"     // API ключ
    private val locationRegionCode = "322722"                   // Код міста "Дніпро"
    private val baseUrl = "http://dataservice.accuweather.com/" // Посилання на сайт для отримання відповіді

    private lateinit var minTempText: TextView // Текст. поле для мін. температури
    private lateinit var maxTempText: TextView // Текст. поле для макс. температури
    private lateinit var dateText: TextView    // Текст. поле для дати

    private val symbolTemp = "C°"

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dateText = findViewById(R.id.dateForecast)

        minTempText = findViewById(R.id.minTemp)
        maxTempText = findViewById(R.id.maxTemp)

        // Створення та налагождення класу Retrofit.Builder()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Створює інтерфейс WeatherService
        val service = retrofit.create(WeatherService::class.java)

        // Виклик методу getWeatherForecast
        val call = service.getWeatherForecast(locationRegionCode, apiKey, true)


        // Отримує прогноз і задає його в текст. поля
        getDailyForecast(call) { forecast ->
            debug(forecast) // LOG

            // Встановлення тексту
            dateText.text = parseDate(forecast.getDate())
            minTempText.text = "Мінімальна: ${forecast.getMinTemp()} $symbolTemp"
            maxTempText.text = "Максимальна: ${forecast.getMaxTemp()} $symbolTemp"

            // Виведення повідомлення
            Toast.makeText(this,"Daily forecast updated",Toast.LENGTH_SHORT).show()
        }

    }

    // LOG
    private fun debug(forecast: Forecast){
        Log.i(LOG_TAG,"Forecast: $forecast\nTemperature: " +
            "Date: ${forecast.getDate()}\n" +
                    "Min: ${forecast.getMinTemp()}\n" +
                    "Max: ${forecast.getMaxTemp()}\n")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val dateTime = ZonedDateTime.parse(dateString, formatter)
        return dateTime.toLocalDate().toString()
    }

    private fun getDailyForecast(call: Call<WeatherResponse>, callback: (Forecast) -> Unit) {
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()!!
                    val newDailyForecast = Forecast()

                    debug(newDailyForecast) // LOG

                    newDailyForecast.setDate(weatherResponse.dailyForecasts[0].date)
                    newDailyForecast.setMinTemp(weatherResponse.dailyForecasts[0].temperature.minimum.value)
                    newDailyForecast.setMaxTemp(weatherResponse.dailyForecasts[0].temperature.maximum.value)


                    callback(newDailyForecast) // Виклик callback
                } else {
                    Log.w(LOG_TAG,"Помилка при отримані відповіді\nmessage: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.w(LOG_TAG,"Помилка виконання запиту\nmessage: ${t.message}")
            }
        })
    }


}