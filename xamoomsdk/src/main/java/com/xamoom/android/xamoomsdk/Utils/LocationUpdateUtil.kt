package com.xamoom.android.xamoomsdk.Utils

import android.content.Context
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.*
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.PushDevice.PushDeviceUtil

fun AppCompatActivity.startUpdateLocation(updateInterval: Long, fastestInterval: Long, enduserApi: EnduserApi) {
    val mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = updateInterval
    mLocationRequest.fastestInterval = fastestInterval

    val builder = LocationSettingsRequest.Builder()
    builder.addLocationRequest(mLocationRequest)
    val locationSettingsRequest = builder.build()

    val settingsClient = LocationServices.getSettingsClient(this)
    settingsClient.checkLocationSettings(locationSettingsRequest)

    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
        return
    }
    LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            // do work here

            if (locationResult == null) {
                return
            }

            val location = locationResult.lastLocation
            val msg = "Updated Location: " +
                    java.lang.Double.toString(location.latitude) + "," +
                    java.lang.Double.toString(location.longitude)
            android.util.Log.e("Location change", msg)

            val sharedPref = applicationContext.getSharedPreferences(PushDeviceUtil.PREFES_NAME,
                    Context.MODE_PRIVATE)
            val util = PushDeviceUtil(sharedPref)
            util.storeLocation(location)
            // util.storeToken("stored-testing-token") !! Decomment if testing push-device call !!
            enduserApi.pushDevice(util)
        }
    }, android.os.Looper.myLooper())
}