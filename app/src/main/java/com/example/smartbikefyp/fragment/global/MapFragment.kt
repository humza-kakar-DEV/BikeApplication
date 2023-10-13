package com.example.smartbikefyp.fragment.global

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.plantsservicefyp.MapRequest
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.FragmentMapBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.MapRequestData
import com.example.smartbikefyp.model.map.GoogleMapRequestModel
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.viewmodel.MapFragmentViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentMapBinding

    private val mapFragmentViewModel: MapFragmentViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var mapFragment: SupportMapFragment

    private var currentLatLng: LatLng? = null

    private val radius: Int = 1500

    private var googleMap: GoogleMap? = null

    private var nearbyShops: GoogleMapRequestModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!

        mapFragment.getMapAsync(this)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext()) as FusedLocationProviderClient

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    mapFragmentViewModel.observeGoogleMapsNearBy.collect {
                        when (it) {
                            is UiState.Exception -> {
                                requireContext().toast("exception: $it")
                            }

                            UiState.Loading -> {
                            }

                            is UiState.Success -> {
                                requireContext().log("api data: ${it.data}")
                                nearbyShops = it.data!!
                                googleMap?.apply {
                                    onMapReady(this)
                                }
                            }
                        }

                    }
                }
            }
        }

        if (!(ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    )
        ) {
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                mapFragment.getMapAsync(object : OnMapReadyCallback {
                    override fun onMapReady(googleMap: GoogleMap) {
                        this@MapFragment.googleMap = googleMap
                        location?.let {
                            currentLatLng = LatLng(location.latitude, location.longitude)
                            mapFragmentViewModel.googleMapsNearBy(
                                MapRequestData(
                                    latitude = currentLatLng?.latitude!!,
                                    longitude = currentLatLng?.longitude!!,
                                    radius = radius,
                                    type = "car_repair"
                                )
                            )
                            googleMap.addMarker(
                                MarkerOptions().position(currentLatLng!!)
                                    .title(resources.getString(R.string.map_output_1))
                                    .snippet(resources.getString(R.string.map_output_2))
                            )
                            googleMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng!!,
                                    14f
                                )
                            )
                            googleMap.addCircle(
                                CircleOptions()
                                    .center(
                                        LatLng(
                                            currentLatLng?.latitude!!,
                                            currentLatLng?.longitude!!
                                        )
                                    )
                                    .radius(radius.toDouble())
                                    .strokeWidth(0f)
                                    .fillColor("#7791CFFF".toColorInt())
                            )
                        }
                    }
                })
            }
        }

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        nearbyShops?.results?.forEach {
            map.addMarker(
                MarkerOptions().position(LatLng(it.geometry.location.lat, it.geometry.location.lng))
                    .title(it.name)
                    .snippet(
                        if (it.permanently_closed == null) {
                            "closed"
                        } else {
                            "opened"
                        }
                    )
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            resources.getDrawable(R.drawable.location_bike_icon_32)!!
                                .toBitmap(48, 40)
                        )
                    )
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return true
    }
}