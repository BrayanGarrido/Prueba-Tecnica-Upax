package com.bornidea.pruebatecnicaupax.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.FragmentLocationBinding
import com.bornidea.pruebatecnicaupax.model.services.TimerService
import com.bornidea.pruebatecnicaupax.ui.dialogs.InfoLocationDialogFragment
import com.bornidea.pruebatecnicaupax.ui.dialogs.LocationDialogFragment
import com.bornidea.pruebatecnicaupax.ui.dialogs.SettingsLocationDialogFragment
import com.bornidea.pruebatecnicaupax.util.Constants.COLLECTION_LOCATION
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject


class LocationFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val TAG_LOCATIONFRAGMENT = "DialogMain"
        const val TAG_LOCATIONFRAGMENT_SETTINGS = "DialogSettings"
        const val TAG_INFOLOCATION = "InfoLocation"
    }

    val db = Firebase.firestore
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentLocationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableLocation()
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                val arguments = Bundle()
                arguments.putString("info", marker.title)

                val dialogInfoLocation = InfoLocationDialogFragment()
                dialogInfoLocation.isCancelable = true
                dialogInfoLocation.arguments = arguments
                dialogInfoLocation.show(
                    requireActivity().supportFragmentManager,
                    TAG_INFOLOCATION
                )
            }
            true
        }
    }

    /**Comprobar permiso*/
    private fun isLocationPermission() = ContextCompat.checkSelfPermission(
        requireActivity().applicationContext,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::mMap.isInitialized) return
        if (isLocationPermission()) {
            mMap.isMyLocationEnabled = true
            /**Actualizar cada 5 minutos ubicacion*/
            sendActualLocation()
            getLocations()
        } else {
            requestLocationPermission()
        }
    }

    private fun getLocations() {
        db.collection(COLLECTION_LOCATION)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val map = document.data
                    val json = JsonObject()
                    json.addProperty("Fecha", "${map["Fecha"]}")
                    json.addProperty("Hora", "${map["Hora"]}")
                    json.addProperty("Latitud", "${map["Latitud"]}")
                    json.addProperty("Longitud", "${map["Longitud"]}")

                    val position = LatLng(
                        map["Latitud"] as Double,
                        map["Longitud"] as Double
                    )
                    mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(json.toString())
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0f))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents.", exception)
            }
    }

    private fun sendActualLocation() {
        if (!isMyServiceRunning(TimerService::class.java)) {
            Intent(activity?.applicationContext, TimerService::class.java).also {
                activity?.startService(it)
            }
        }
    }


    private fun requestLocationPermission() {
        /**Ya se pidieron los permisos pero el usuario los rechazó*/
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            /* FragmentDialog (Ve a ajustes y acepta los permisos)  */
            val dialogSetings = SettingsLocationDialogFragment()
            dialogSetings.isCancelable = false
            dialogSetings.show(
                requireActivity().supportFragmentManager,
                TAG_LOCATIONFRAGMENT_SETTINGS
            )
        } else {
            /**Primera vez que se piden los permisos*/
            val dialog = LocationDialogFragment()
            dialog.isCancelable = false
            dialog.show(requireActivity().supportFragmentManager, TAG_LOCATIONFRAGMENT)
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}