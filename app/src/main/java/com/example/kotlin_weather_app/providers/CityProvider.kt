package com.example.kotlin_weather_app.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.kotlin_weather_app.MainActivity
import com.example.kotlin_weather_app.R
import com.example.kotlin_weather_app.TimeAndWeather
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CityProvider : TimeAndWeather() {

    override fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        data: String
    ) {
        super.updateAppWidget(context, appWidgetManager, appWidgetId, data)
        // Construct the RemoteViews object
        val viewsCityAndWeather = RemoteViews(context.packageName, R.layout.city_and_weather)
        SimpleDateFormat("HH:mm | EEE, dd MMM", Locale.getDefault()).format(Date()).also {
            viewsCityAndWeather.setTextViewText(R.id.widget_date, it)
        }

        // Create an Intent to launch MainActivity
        val intent = Intent(context, MainActivity::class.java)

        // Create a PendingIntent for click the widget
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Get main data
        val mainData = JSONObject(data).optJSONObject("main")

        // Get temperature data
        val temp = mainData?.optDouble("temp") ?: 0.0
        viewsCityAndWeather.setTextViewText(R.id.widget_temp, "${temp.toInt()}°C")

        // Get city name
        val cityName = JSONObject(data).optString("name")
        viewsCityAndWeather.setTextViewText(R.id.widget_city, cityName)

        // set icon
        val weatherArray = JSONObject(data).optJSONArray("weather")
        val weatherData = weatherArray?.optJSONObject(0)
        val main = weatherData?.optString("main") ?: ""
        viewsCityAndWeather.setImageViewResource(R.id.widget_current_weather_description, getWeatherIcon(main))

        // Attach the click listener
        viewsCityAndWeather.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, viewsCityAndWeather)
    }
}