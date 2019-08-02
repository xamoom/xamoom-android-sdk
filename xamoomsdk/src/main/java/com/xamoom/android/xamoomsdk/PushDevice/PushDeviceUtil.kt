package com.xamoom.android.xamoomsdk.PushDevice

import android.content.SharedPreferences
import android.location.Location

class PushDeviceUtil(val preferences: SharedPreferences) {

    fun storeLocation(location: Location) {
        preferences.edit().putFloat("xamoom_push_device_lat", location.latitude.toFloat()).apply()
        preferences.edit().putFloat("xamoom_push_device_lon", location.longitude.toFloat()).apply()
    }

    fun storeToken(token: String) {
        preferences.edit().putString("xamoom_push_device_token", token).apply()
    }

    fun getSavedLocation(): Map<String, Float>? {
        val lat = preferences.getFloat("xamoom_push_device_lat", 0.0f)
        val lon = preferences.getFloat("xamoom_push_device_lon", 0.0f)

        if (lat == 0.0f || lon == 0.0f) {
            return null
        }

        return mapOf("lat" to lat, "lon" to lon)
    }

    fun getSavedToken(): String? {
        val token = preferences.getString("xamoom_push_device_token", "")

        if (token.isEmpty()) {
            return null
        }

        return token
    }

    fun setSound(sound: Boolean) {
        preferences.edit().putBoolean("xamoom_push_device_sound", sound).apply()
    }

    fun getSound(): Boolean {
        return preferences.getBoolean("xamoom_push_device_sound", true)
    }

    fun setNoNotification(noNotification: Boolean) {
        preferences.edit().putBoolean("xamoom_push_device_no_notification", noNotification).apply()
    }

    fun getNoNotification(): Boolean {
        return preferences.getBoolean("xamoom_push_device_no_notification", false)
    }

    companion object {
        const val PREFES_NAME = "push_device_util_preferences"
    }
}