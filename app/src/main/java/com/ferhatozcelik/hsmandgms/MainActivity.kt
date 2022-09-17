package com.ferhatozcelik.hsmandgms

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ferhatozcelik.hsmandgms.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, com.google.android.gms.maps.OnMapReadyCallback {

    companion object {
        private const val MAP_BUNDLE_KEY = "MapBundleKey"
    }

    private lateinit var huaweiMap: HuaweiMap
    private lateinit var googleMap: GoogleMap

    private lateinit var binding: ActivityMainBinding
    private lateinit var marker: Marker
    private lateinit var cameraUpdate: CameraUpdate
    private lateinit var cameraPosition: CameraPosition

    private lateinit var mapFragment: com.google.android.gms.maps.SupportMapFragment
    private var hardService: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY)
        }

       if (isHmsAvailable(this)){
            binding.huaweiMapView.onCreate(mapViewBundle)
            binding.huaweiMapView.getMapAsync(this)
            binding.gmsLayout.visibility = View.GONE
            binding.hmsLayout.visibility = View.VISIBLE
        }else{
           mapFragment = (supportFragmentManager.findFragmentById(R.id.map_fragment) as com.google.android.gms.maps.SupportMapFragment?)!!
           mapFragment.getMapAsync(this)
           binding.hmsLayout.visibility = View.GONE
           binding.gmsLayout.visibility = View.VISIBLE
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val officePosition = com.google.android.gms.maps.model.LatLng(41.031261, 29.117277)
        val markerOptions: com.google.android.gms.maps.model.MarkerOptions = com.google.android.gms.maps.model.MarkerOptions()
            .position(officePosition)
            .title("Huawei Turkey")
            .snippet("Huawei Turkey")
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(officePosition))
    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        this.huaweiMap = huaweiMap

        marker = huaweiMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("Huawei Turkey")
                .position(LatLng(41.031261, 29.117277))

        )
        cameraPosition = CameraPosition.builder()
            .target(LatLng(41.031261, 29.117277))
            .zoom(10f)
            .bearing(2.0f)
            .tilt(2.5f).build()
        cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        huaweiMap.moveCamera(cameraUpdate)
    }

    fun isHmsAvailable(context: Context?): Boolean {
        return when (hardService) {
            "HMS" -> {
                true
            }
            "GMS" -> {
                false
            }
            else -> {
                var isAvailable = false
                if (null != context) {
                    val result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
                    isAvailable = ConnectionResult.SUCCESS == result
                }
                isAvailable
            }
        }
    }

}


