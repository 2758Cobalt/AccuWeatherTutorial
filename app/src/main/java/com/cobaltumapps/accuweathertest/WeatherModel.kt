package com.cobaltumapps.accuweathertest

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("DailyForecasts") val dailyForecasts: List<DailyForecast>
)

data class DailyForecast(
    @SerializedName("Temperature") val temperature: Temperature,
    @SerializedName("Date") var date: String,
)

data class Temperature(
    @SerializedName("Minimum") val minimum: Minimum,
    @SerializedName("Maximum") val maximum: Maximum,
)

data class Minimum(
    @SerializedName("Value") val value: Double
)
data class Maximum(
    @SerializedName("Value") val value: Double
)

data class Forecast(
    private var date: String = "2000-01-01",
    private var maxTemp: Double = 0.0,
    private var minTemp: Double = 0.0)
{
    fun setDate(newDate: String){
        this.date = newDate
    }
    fun setMaxTemp(newTemp: Double){
        this.maxTemp = newTemp
    }
    fun setMinTemp(newTemp: Double){
        this.minTemp = newTemp
    }
    fun getMinTemp(): Double {
        return this.minTemp
    }
    fun getMaxTemp(): Double {
        return this.maxTemp
    }
    fun getDate(): String {
        return this.date
    }

}
