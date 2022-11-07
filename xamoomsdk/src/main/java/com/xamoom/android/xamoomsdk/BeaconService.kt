package com.xamoom.android.xamoomsdk

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.altbeacon.beacon.*
import timber.log.Timber

/**
 * Handle stop action from foreground notification.
 */
class BeaconServiceBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BeaconService.NOTIFICATION_ACTION_STOP_SERVICE) {
            BeaconService.sharedInstance?.disableForegroundService()
        }
    }
}

/**
 * BeaconService for background / foreground beacon scanning.
 *
 * Use (start/stop)(Monitoring/Ranging) for normal foreground/background scanning with actual
 * Android limitations. https://altbeacon.github.io/android-beacon-library/detection_times.html
 *
 * Use (enable/disable)Foreground Service to start an Android Foreground Service with sticky
 * notification to have better scanning.
 *
 * // TODO: Permissions needed?
 */
class BeaconService(
    context: Context, majorId: Int,
    @StringRes private val notificationTitle: Int = R.string.beacon_service_notification_title,
    @DrawableRes private val notificationIcon: Int = R.drawable.ic_near_me
) {

    val beaconViewModel: BeaconViewModel = BeaconViewModel()
    val isShowingForegroundService: MutableLiveData<Boolean> = MutableLiveData(false);

    private val backgroundObserver = Observer<Int> { state ->
        beaconViewModel.isInsideRegion.value = state == MonitorNotifier.INSIDE

        if (state == MonitorNotifier.INSIDE) {
            Timber.d("Background: Beacons detected")
            if (automaticRanging) startRanging()
        } else {
            Timber.d("Background: No beacons detected")
            if (automaticRanging) stopRanging()
            beaconViewModel.clearBeacons()
        }
    }
    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Timber.d("Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            beaconViewModel.addBeacon(beacon)
            Timber.d("$beacon about ${beacon.distance} meters away")
        }
    }
    private var automaticRanging = true
    private var beaconManager: BeaconManager = BeaconManager.getInstanceForApplication(context)
    private var foregroundNotification: Notification? = null
    private var region: Region =
        Region(
            "${context.packageName}.beacons",
            XAMOOM_BEACON_IDENTIFIER,
            Identifier.fromInt(majorId),
            null
        )

    // kotlin does not recognize my default parameters
    constructor(context: Application, beaconMajor: Int) : this(
        context,
        beaconMajor,
        R.string.beacon_service_notification_title,
        R.drawable.ic_near_me
    ) {
    }

    init {
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_LAYOUT))

        debugInformation()

        beaconManager.getRegionViewModel(region).regionState.observeForever(backgroundObserver)
        beaconManager.getRegionViewModel(region).rangedBeacons.observeForever(rangingObserver)

        sharedInstance = this
    }

    /**
     * Changes the method for beacon scanning from scheduled scan jobs to running an foreground service.
     * Will stop running monitoring & ranging.
     * If you start monitoring after enabling this, it will show the user an notification and use
     * it for scanning beacons. Normal background beacon scanning intervals are used.
     */
    fun enableForegroundService(context: Context) {
        if (beaconManager.isAnyConsumerBound) {
            Timber.w("There are already consumers bound. Stopping monitoring & ranging. If you use another beacon service please consider stopping if you run into problems.")
        }

        // disable monitoring & ranging
        stopMonitoring()
        stopRanging()

        val builder = Notification.Builder(context)
        builder.setSmallIcon(notificationIcon)
        builder.setContentTitle(context.getString(notificationTitle))

        // opening the app when clicking on the notification
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)

        // Action to stop the service
        val disableService = Intent(NOTIFICATION_ACTION_STOP_SERVICE)
        disableService.setClass(context, BeaconServiceBroadcastReceiver::class.java)
        val disableServicePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, disableService, 0)
        builder.addAction(
            R.drawable.ic_stop_24,
            context.getString(R.string.beacon_service_stop_action),
            disableServicePendingIntent
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.beacon_service_notification_name),
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description =
                context.getString(R.string.beacon_service_notification_description)

            context.startActivity(intent)

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channel.id)
        }

        foregroundNotification = builder.build()
        foregroundNotification?.let {
            beaconManager.enableForegroundServiceScanning(
                it,
                NOTIFICATION_ID
            )
        }
        beaconManager.setEnableScheduledScanJobs(false)
        isShowingForegroundService.postValue(true);
        startMonitoring()
    }

    /**
     * Removes foreground service with their notification.
     * Also stops monitoring & ranging of beacons.
     */
    fun disableForegroundService() {
        stopMonitoring()
        stopRanging()
        beaconManager.disableForegroundServiceScanning()
        beaconManager.setEnableScheduledScanJobs(true)
        isShowingForegroundService.postValue(false);
    }

    fun startMonitoring() {
        Timber.d("Start background monitoring")
        beaconManager.startMonitoring(region)
    }

    fun stopMonitoring() = beaconManager.stopMonitoring(region)

    private fun startRanging() {
        Timber.d("Start Beacon ranging")
        beaconManager.startRangingBeacons(region)
    }

    private fun stopRanging() = beaconManager.stopRangingBeacons(region)

    private fun debugInformation() {
        Timber.d("Beacon Region: $region")
        Timber.d("isAutoBindActive: ${beaconManager.isAutoBindActive}")
        Timber.d("scheduledScanJobsEnabled: ${beaconManager.scheduledScanJobsEnabled}")

        Timber.d("backgroundScanPeriod: ${beaconManager.backgroundScanPeriod}")
        Timber.d("backgroundBetweenScanPeriod: ${beaconManager.backgroundBetweenScanPeriod}")
    }

    companion object {
        const val BEACON_LAYOUT: String = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
        const val NOTIFICATION_CHANNEL_ID: String = "foreground-beacon-service-channel-id"
        const val NOTIFICATION_ID: Int = 1001
        const val NOTIFICATION_ACTION_STOP_SERVICE: String =
            "com.xamoom.android.xamoomsdk.NOTIFICATION_ACTION_STOP_SERVICE"
        val XAMOOM_BEACON_IDENTIFIER: Identifier =
            Identifier.parse("de2b94ae-ed98-11e4-3432-78616d6f6f6d")

        var sharedInstance: BeaconService? = null
    }
}