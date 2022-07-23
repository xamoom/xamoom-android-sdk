package com.xamoom.android.xamoomsdk

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import org.altbeacon.beacon.*
import kotlin.properties.Delegates

class BeaconService(context: Context, private val majorId: String) {

    val beaconViewModel: BeaconViewModel = BeaconViewModel()
    var shouldBackgroundScan by Delegates.observable(true) { _, _, new ->
        if (new) startMonitoring() else stopMonitoring()
    }

    // TODO: Big problem, insert better beacon unique identifier
    private var region: Region =
        Region("wtf.beacons", XAMOOM_BEACON_IDENTIFIER, Identifier.parse(majorId), null);
    private var automaticRanging = true
    private var beaconManager: BeaconManager = BeaconManager.getInstanceForApplication(context)

    private val backgroundObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.INSIDE) {
            Log.d(TAG, "Background: Beacons detected")
            if (automaticRanging) startRanging()
        }
        else {
            Log.d(TAG, "Background: No beacons detected")
            if (automaticRanging) stopRanging()
        }
    }

    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }

    init {
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_LAYOUT))

        debugInformation()

        beaconManager.getRegionViewModel(region).regionState.observeForever(backgroundObserver)
        beaconManager.getRegionViewModel(region).rangedBeacons.observeForever(rangingObserver)

        startMonitoring()
    }

    private fun startMonitoring() {
        Log.d(TAG, "Start background monitoring")
        beaconManager.startMonitoring(region)
    }

    private fun stopMonitoring() = beaconManager.stopMonitoring(region)

    private fun startRanging() {
        Log.d(TAG, "Start Beacon ranging")
        beaconManager.startRangingBeacons(region)
    }

    private fun stopRanging() = beaconManager.stopRangingBeacons(region)

    private fun debugInformation() {
        Log.d(TAG, "Beacon Region: $region")
        Log.d(TAG, "isAutoBindActive: ${beaconManager.isAutoBindActive}")
        Log.d(TAG, "scheduledScanJobsEnabled: ${beaconManager.scheduledScanJobsEnabled}")
    }

    companion object {
        const val TAG: String = "WTF BeaconService"
        const val BEACON_LAYOUT: String = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
        val XAMOOM_BEACON_IDENTIFIER: Identifier = Identifier.parse("de2b94ae-ed98-11e4-3432-78616d6f6f6d")
    }
}