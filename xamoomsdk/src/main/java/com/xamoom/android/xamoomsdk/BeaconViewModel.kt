package com.xamoom.android.xamoomsdk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.altbeacon.beacon.Beacon
import java.util.*
import kotlin.collections.HashMap

class BeaconViewModel: ViewModel() {
 // TODO: Change to minor as key
 val beacons: MutableLiveData<HashMap<Long, Beacon>> by lazy {
  MutableLiveData(emptyMap<Long, Beacon>() as HashMap)
 }

 fun addBeacon(beacon: Beacon) {
  val newValue = beacons.value?: emptyMap<Long, Beacon>() as HashMap
  newValue[System.currentTimeMillis()] = beacon
  beacons.postValue(newValue)
 }

 fun clearBeacons() {
  val empty = beacons.value
  empty?.clear()
  beacons.postValue(empty)
 }
}