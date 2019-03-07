package com.xamoom.android.xamoomsdk.Helpers

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject
import android.support.v4.content.LocalBroadcastManager
import android.content.Intent
import android.util.Log
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.PushDevice.PushDeviceUtil


class PushService: FirebaseMessagingService() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data ?: return
        val contentId = data["content-id"]
        val title = data["title"]
        val body = data["body"]
        val wakeup = data["wakeup"]

        if (wakeup == null) {
            val intent = Intent("xamoom-push-notification-received")
            // You can also include some extra data.
            intent.putExtra("contentId", contentId)
            intent.putExtra("title", title)
            intent.putExtra("body", body)

            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        } else {

            sharedPreferences = applicationContext.getSharedPreferences(PushDeviceUtil.PREFES_NAME,
                    Context.MODE_PRIVATE)

            val enduserApi = EnduserApi.getSharedInstance(applicationContext)
            if (enduserApi != null) {
                enduserApi.pushDevice(PushDeviceUtil(sharedPreferences))
                XamoomBeaconService.getInstance(applicationContext).startBeaconService(enduserApi.majorId)
            } else {
                Log.w("xamoom PushService", "Xamoom EnduserApi is not initialized")
            }
        }
    }
}