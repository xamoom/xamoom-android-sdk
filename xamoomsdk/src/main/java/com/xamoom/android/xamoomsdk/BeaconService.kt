package com.xamoom.android.xamoomsdk

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import org.altbeacon.beacon.*
import kotlin.properties.Delegates

class BeaconService(context: Context, majorId: Int) {

    val beaconViewModel: BeaconViewModel = BeaconViewModel()

    // TODO: Big problem, insert better beacon unique identifier
    private var region: Region =
        Region("wtf.beacons", XAMOOM_BEACON_IDENTIFIER, Identifier.fromInt(majorId), null);
    private var automaticRanging = true
    private var beaconManager: BeaconManager = BeaconManager.getInstanceForApplication(context)
    private var foregroundNotification: Notification? = null

    private val backgroundObserver = Observer<Int> { state ->
        beaconViewModel.isInsideRegion.value = state == MonitorNotifier.INSIDE

        if (state == MonitorNotifier.INSIDE) {
            Log.d(TAG, "Background: Beacons detected")
            if (automaticRanging) startRanging()
        }
        else {
            Log.d(TAG, "Background: No beacons detected")
            if (automaticRanging) stopRanging()
            beaconViewModel.clearBeacons()
        }
    }

    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            beaconViewModel.addBeacon(beacon)
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }

    init {
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_LAYOUT))

        debugInformation()

        beaconManager.getRegionViewModel(region).regionState.observeForever(backgroundObserver)
        beaconManager.getRegionViewModel(region).rangedBeacons.observeForever(rangingObserver)
    }

    /**
     * Changes the method for beacon scanning from scheduled scan jobs to running an foregroundservice.
     * Will stop running monitoring & ranging.
     * If you start monitoring after enabling this, it will show the user an notification and use
     * it for scanning beacons. Normal background beacon scanning intervals are used.
     */
    fun enableForegroundService(context: Context, clazz: Class<out Activity>) {
        if (beaconManager.isAnyConsumerBound) {
            Log.w(TAG, "There are already consumers bound. Stopping monitoring & ranging. If you use another beacon service please consider stopping if you run into problems.")
        }

        // disable monitoring & ranging
        stopMonitoring()
        stopRanging()

        val builder = Notification.Builder(context)
        builder.setSmallIcon(R.drawable.ic_android)
        builder.setContentTitle("Scanning for Beacons")
        val intent = Intent(context, clazz)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "beacon-ref-notification-id",
                "My Notification Name", NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "My Notification Channel Description"
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            builder.setChannelId(channel.id)
        }

        foregroundNotification = builder.build()
        foregroundNotification?.let { beaconManager.enableForegroundServiceScanning(it, 456) }
        beaconManager.setEnableScheduledScanJobs(false)
    }

    fun disableForegroundService() {
        beaconManager.disableForegroundServiceScanning()
        beaconManager.setEnableScheduledScanJobs(true)
    }

    fun startMonitoring() {
        Log.d(TAG, "Start background monitoring")
        beaconManager.startMonitoring(region)
    }

    fun stopMonitoring() = beaconManager.stopMonitoring(region)

    private fun startRanging() {
        Log.d(TAG, "Start Beacon ranging")
        beaconManager.startRangingBeacons(region)
    }

    private fun stopRanging() = beaconManager.stopRangingBeacons(region)

    private fun debugInformation() {
        Log.d(TAG, "Beacon Region: $region")
        Log.d(TAG, "isAutoBindActive: ${beaconManager.isAutoBindActive}")
        Log.d(TAG, "scheduledScanJobsEnabled: ${beaconManager.scheduledScanJobsEnabled}")

        Log.d(TAG, "backgroundScanPeriod: ${beaconManager.backgroundScanPeriod}")
        Log.d(TAG, "backgroundBetweenScanPeriod: ${beaconManager.backgroundBetweenScanPeriod}")
    }

    companion object {
        const val TAG: String = "WTF BeaconService"
        const val BEACON_LAYOUT: String = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
        val XAMOOM_BEACON_IDENTIFIER: Identifier = Identifier.parse("de2b94ae-ed98-11e4-3432-78616d6f6f6d")
    }
}