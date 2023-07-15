package com.navin.nearby2

import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.Transliterator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.navin.nearby2.api.ApiService
import com.navin.nearby2.api.Iservice
import com.navin.nearby2.model.Adress
import com.navin.nearby2.model.Item
import com.navin.nearby2.model.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import kotlin.math.log

class MainActivity : AppCompatActivity() ,OnMapReadyCallback{
    lateinit var gpsTraker:GpsTraker
    var longitude:Double=0.0
    var latitude:Double=0.0
    var GPS_PERMISSION_CODE=1414
    lateinit var iservice:Iservice



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iservice=ApiService.retrofit.create(Iservice::class.java)
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted


            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                GPS_PERMISSION_CODE
            )


        } else {
            getUserlocatin()

        }
        checkCameraPermission()
        var map=supportFragmentManager.findFragmentById(R.id.fragment) as SupportMapFragment
        map.getMapAsync(this)



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getUserlocatin()
    }
    private fun getUserlocatin() {
        gpsTraker = GpsTraker(applicationContext)
        if (gpsTraker.canGetLocation()) {
            Log.e("", "")
            latitude = gpsTraker.getLatitude()
            longitude = gpsTraker.getLongitude()
        }
    }
    fun checkCameraPermission(){


        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted


            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.CAMERA),
                1
            )

        } else {

            //  CaptureFrontPhoto()
        }


    }

    override fun onMapReady(map: GoogleMap) {
        //Marker option
        var position = LatLng(35.72564,51.37231)
        var marker = MarkerOptions().position(position)
            .title("android class")
            .snippet("android learn academy")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        map.addMarker(marker)
        var position2 = LatLng(35.694928889912674, 51.39177818577823)
        var marker2 = MarkerOptions().position(position2)
            .title("software class")
            .snippet("nira system")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
        map.addMarker(marker2)
        map.mapType=GoogleMap.MAP_TYPE_HYBRID
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled=true
        map.uiSettings.isZoomControlsEnabled=true
        map.addPolyline(PolylineOptions().add(position,position2).color(R.color.teal_700).width(5f))
        var camera=CameraPosition.builder().target(position).zoom(15f).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camera))
        map.addCircle(CircleOptions().center(position).radius(1000.0).fillColor(R.color.teal_700))

        var key="service.f8d1d6db65534968b4eb2f4d74ece00f"
        iservice.getAddress(key,35.694928889912674,51.39177818577823).enqueue(object :Callback<Adress>{
            override fun onResponse(call: Call<Adress>, response: Response<Adress>) {
                Log.e("","")

            }

            override fun onFailure(call: Call<Adress>, t: Throwable) {
                Log.e("","")
            }

        })
        iservice.searchLocation(key,"Fast food",35.694928889912674,51.39177818577823).enqueue(object :Callback<PlaceData>{
            override fun onResponse(call: Call<PlaceData>, response: Response<PlaceData>) {
                Log.e("", "")
                for (item in response.body()!!.items) {


                    var positionItem = LatLng(
                        item.location.latitude.toDouble(),
                        item.location.longitude.toDouble()
                    )
                    var markerPosition = MarkerOptions().position(positionItem)
                        .title(item.title)
                        .snippet(item.address)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))

                    map.addMarker(markerPosition)

                }
            }
            override fun onFailure(call: Call<PlaceData>, t: Throwable) {
                Log.e("","")

            }

        })

    }
}