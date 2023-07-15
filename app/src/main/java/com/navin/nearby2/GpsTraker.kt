package com.navin.nearby2

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder

class GpsTraker (var context: Context) : Service() , LocationListener {
    // flag for GPS status
    private var isGPSEnabled = false

    // flag for network status
    private var isNetworkEnabled = false

    // flag for GPS status
    private  var canGetLocation = false

    private var location: Location? =null
    private var latitude = 0.0
    private var longitude = 0.0

    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES: Long = 60000 // 1 minute
    }

    // Declaring a Location Manager
    lateinit var locationManager: LocationManager

    init {
        getLocation()
    }


    fun getLocation(): Location? {
        try {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager


            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)


            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isGPSEnabled && isNetworkEnabled) {

                canGetLocation = true

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this
                    )
                    if (locationManager != null) {
                        location =
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!

                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }

                }

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this
                    )
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }

                }


            }


        }catch (e : Exception){
            e.printStackTrace()
        }
        return location
    }

    fun stopUsingGPS(){
        if (locationManager != null){
            locationManager.removeUpdates(this)
        }
    }

    fun getLatitude() : Double{
        if (location != null) {
            latitude = location!!.latitude
        }
        return latitude
    }

    fun getLongitude() : Double {
        if (location != null) {
            longitude = location!!.longitude
        }
        return longitude
    }


    open fun canGetLocation() : Boolean{
        getLocation()
        return canGetLocation
    }



    override fun onLocationChanged(p0: Location) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    interface StateGps{

        fun disableGps()

        fun enableGps()

        fun onChangeLocation(location : Location)

    }

}