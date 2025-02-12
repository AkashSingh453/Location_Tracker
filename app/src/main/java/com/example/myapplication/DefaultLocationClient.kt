package com.example.myapplication

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClient(
    private val context : Context,
    private val client: FusedLocationProviderClient
) : LocationClient {
    override fun getLocationUpdate(interval: Long): Flow<Location> {
         return callbackFlow {
             if (!context.hasLocationPermission()){
                 throw LocationClient.LocationException("Missing Location permission")
             }

             val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
             val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
             val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
             if (!isGpsEnabled && !isNetworkEnabled){
                 throw LocationClient.LocationException("GPS id disabled")
             }

             val request = com.google.android.gms.location.LocationRequest.create()
                 .setInterval(interval)
                 .setFastestInterval(interval)


             val locationCallback = object : LocationCallback(){
                 override fun onLocationResult(result: LocationResult) {
                     super.onLocationResult(result)
                     result.locations.lastOrNull()?.let { location ->
                         launch { send(location) }
                     }
                 }
             }

             client.requestLocationUpdates(
                 request,
                 locationCallback,
                 Looper.getMainLooper()
             )

             awaitClose{
                 client.removeLocationUpdates(locationCallback)
             }

         }
    }
}