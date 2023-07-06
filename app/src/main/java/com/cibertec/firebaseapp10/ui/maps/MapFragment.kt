package com.cibertec.firebaseapp10.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.cibertec.firebaseapp10.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {

    private var map : GoogleMap?=null

    @SuppressLint("MissingPermission")
    private val coarsepermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranded ->
        when{
            isGranded -> map?.isMyLocationEnabled = true
            else -> println("No tiene permiso")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{maps ->
            map = maps
            val place = LatLng(-12.2261498,-76.9432551)
            maps?.animateCamera(CameraUpdateFactory.newLatLngZoom(place,16f),2000,null)

            val marker = MarkerOptions().position(place).title("Casita")

            maps?.addMarker(marker)

            maps?.uiSettings?.isZoomControlsEnabled= true
            maps?.setPadding(80,80,80,80)

            if(isLocationPermissionGranted()){
                map?.isMyLocationEnabled = true
            }else{
                coarsepermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

        }
    }

    private fun isLocationPermissionGranted()  =
        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}