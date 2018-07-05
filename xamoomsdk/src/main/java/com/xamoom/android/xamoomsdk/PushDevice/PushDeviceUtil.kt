package com.xamoom.android.xamoomsdk.PushDevice

import android.content.Context
import android.location.Location
import android.preference.PreferenceManager

class PushDeviceUtil(val context: Context) {

    fun storeLocation(location: Location) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putFloat("xamoom_push_device_lat", location.latitude.toFloat()).apply()
        preferences.edit().putFloat("xamoom_push_device_lon", location.longitude.toFloat()).apply()
    }

    fun storeToken(token: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putString("xamoom_push_device_token", token).apply()
    }

    fun getSavedLocation(): Map<String, Float>? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val lat = preferences.getFloat("xamoom_push_device_lat", 0.0f)
        val lon = preferences.getFloat("xamoom_push_device_lon", 0.0f)

        if (lat == 0.0f || lon == 0.0f) {
            return null
        }

        return mapOf("lat" to lat, "lon" to lon)
    }

    fun getSavedToken(): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = preferences.getString("xamoom_push_device_token", "")

        if (token.isEmpty()) {
            return null
        }

        return token
    }
}