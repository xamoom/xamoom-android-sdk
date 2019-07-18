package com.xamoom.android.xamoomsdk.Helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.LocationResult
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.PushDevice.PushDeviceUtil


class GeofenceBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            val name = context.packageName
            if (name.equals(action)) {
                val result = LocationResult.extractResult(intent)
                if (result != null) {
                    val locations = result.locations
                    val sharedPref = context!!.getSharedPreferences(PushDeviceUtil.PREFES_NAME,
                            Context.MODE_PRIVATE)
                    val util = PushDeviceUtil(sharedPref)
                    util.storeLocation(locations[0])
                    EnduserApi.getSharedInstance(context).pushDevice(util, false)
                }
            }
        }
    }
}