package com.example.firebaseapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_gps.*
import kotlinx.android.synthetic.main.activity_main.*

class Gps : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        bnt_localizar.setOnClickListener{
            checkPermissions()
        }
    }

    private fun checkPermissions() {

        if( ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION), 1)
        } else{
            getLocations()
        }

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocations() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if( it == null ){
                Toast.makeText(this, "No se pudo realizar el rastreo", LENGTH_SHORT).show()
            } else it.apply {
                val latitude = it.latitude
                val longitude = it.longitude
                tv_gps.text = "Latitude: $longitude, Latitude: $latitude "
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions , grantResults)
        if( requestCode == 1 ){
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED){
                if ( ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION ) == PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", LENGTH_SHORT).show()
                    getLocations()
                } else {
                    Toast.makeText(this,"Permission Denied", LENGTH_SHORT).show()
                }
            }
        }
    }
}