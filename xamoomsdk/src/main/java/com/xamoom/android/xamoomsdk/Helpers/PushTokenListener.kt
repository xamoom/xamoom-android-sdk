package com.xamoom.android.xamoomsdk.Helpers

import android.content.Context
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.PushDevice.PushDeviceUtil

class PushTokenListener: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val sharedPref = applicationContext.getSharedPreferences(PushDeviceUtil.PREFES_NAME,
                Context.MODE_PRIVATE)
        val util = PushDeviceUtil(sharedPref)
        util.storeToken(token)
        EnduserApi.getSharedInstance().pushDevice(util, true)
    }
}