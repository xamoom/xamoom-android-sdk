package com.xamoom.android.xamoomsdk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.altbeacon.beacon.Beacon
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

// TODO: Should this class just store beacons, instead of deleting them after region exit?
class BeaconViewModel : ViewModel() {
    val beacons: MutableLiveData<HashMap<Int, Beacon>> by lazy {
        MutableLiveData(hashMapOf<Int, Beacon>())
    }
    val isInsideRegion: MutableLiveData<Boolean> by lazy { MutableLiveData(false) }

    fun addBeacon(beacon: Beacon) {
        val newValue = beacons.value ?: emptyMap<Int, Beacon>() as HashMap
        newValue[beacon.id3.toInt()] = beacon
        beacons.postValue(newValue)
    }

    fun clearBeacons() {
        val empty = beacons.value
        empty?.clear()
        beacons.postValue(empty)
    }
}