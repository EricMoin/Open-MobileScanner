package com.mobilescanner.main.main.data.utils

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.mobilescanner.main.feature_home.remote.model.Location

object LocationUtils {
    fun getLocation(context: Activity): Location {
        var locManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = Location("","")
        try {
            var loc = getLastKnownLocation(locManager)
            loc?.let {
                Log.d("gpslocation", loc.toString())
                location = Location(loc.longitude.toString(),loc.latitude.toString())
            }
        }catch (e:SecurityException){
            e.printStackTrace()
        }
        return location
    }
    private fun getLastKnownLocation(locationManager: LocationManager): android.location.Location? {
        try {
            val providers = locationManager.getProviders(true)
            var bestLocation: android.location.Location? = null
            for (provider in providers) {
                val l = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                }
            }
            return bestLocation
        }catch (e:SecurityException){
            e.printStackTrace()
        }
        return null
    }

}