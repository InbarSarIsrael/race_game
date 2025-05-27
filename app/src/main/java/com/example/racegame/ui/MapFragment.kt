package com.example.racegame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.racegame.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val defaultLocation = LatLng(31.0461, 34.8516) // Israel
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6f))
    }

    fun zoom(lat: Double, lon: Double) {
        val position = LatLng(lat, lon)
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions().position(position).title("High Score"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12f))
    }
}



