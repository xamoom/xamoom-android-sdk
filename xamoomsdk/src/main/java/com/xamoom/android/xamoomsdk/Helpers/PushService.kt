package com.xamoom.android.xamoomsdk.Helpers

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.Intent
import android.util.Log
import com.xamoom.android.xamoomsdk.EnduserApi
import com.xamoom.android.xamoomsdk.PushDevice.PushDeviceUtil
import timber.log.Timber


class PushService : FirebaseMessagingService() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.messageType
        val data = remoteMessage.data ?: return
        val contentId = data["content-id"]
        val title = data["title"]
        val body = data["body"]
        val wakeup = data["wake-up"]
        val enduserApi = EnduserApi.getSharedInstance(applicationContext)
        sharedPreferences = applicationContext.getSharedPreferences(
            PushDeviceUtil.PREFES_NAME,
            Context.MODE_PRIVATE
        )
        if (wakeup == null) {
            val intent = Intent("xamoom-push-notification-received")
            // You can also include some extra data.
            intent.putExtra("contentId", contentId)
            intent.putExtra("title", title)
            intent.putExtra("body", body)

            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            enduserApi?.pushDevice(PushDeviceUtil(sharedPreferences), true)
        } else {
            if (enduserApi != null) {
                XamoomBeaconService.getInstance(applicationContext, enduserApi).startBeaconService()
                enduserApi?.pushDevice(PushDeviceUtil(sharedPreferences), false)
            } else {
                Log.w("xamoom PushService", "Xamoom EnduserApi is not initialized")
            }
        }


    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        println("Firebase CM: new token generated $p0")
    }
}