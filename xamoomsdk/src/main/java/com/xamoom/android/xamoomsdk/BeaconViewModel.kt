package com.xamoom.android.xamoomsdk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.altbeacon.beacon.Beacon
import java.time.LocalDateTime

class BeaconViewModel : ViewModel() {
    val beacons: MutableLiveData<HashMap<Int, BeaconData>> by lazy {
        MutableLiveData(hashMapOf<Int, BeaconData>())
    }
    val isInsideRegion: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    fun addBeacon(beacon: Beacon) {
        val newValue = beacons.value ?: emptyMap<Int, BeaconData>() as HashMap
        newValue[beacon.id3.toInt()] = BeaconData(LocalDateTime.now(), beacon)
        beacons.postValue(newValue)
    }

    fun clearBeacons() {
        val empty = beacons.value
        empty?.clear()
        beacons.postValue(empty)
    }

    data class BeaconData(val lastSeen: LocalDateTime, val beacon: Beacon)
}