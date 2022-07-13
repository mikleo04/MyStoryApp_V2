package com.example.mystoryapp.ui.story.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Transformations.map
import com.example.mystoryapp.api.response.ListStoryItem
import com.example.mystoryapp.constant.Constant
import com.example.mystoryapp.constant.Constant.MAP_BOUNDARY_BOTTOM_LATITUDE
import com.example.mystoryapp.constant.Constant.MAP_BOUNDARY_BOTTOM_LONGITUDE
import com.example.mystoryapp.constant.Constant.MAP_BOUNDARY_TOP_LATITUDE
import com.example.mystoryapp.constant.Constant.MAP_BOUNDARY_TOP_LONGITUDE
import com.example.mystoryapp.constant.Constant.INTERNET_CONNECTION_MAX_RETRY_COUNT
import com.example.mystoryapp.constant.Constant.MAP_ICON_MARKER_COLOR
import com.example.mystoryapp.data.User
import com.example.mystoryapp.database.Injection
import com.example.mystoryapp.databinding.ActivityStoryMapsBinding
import com.example.mystoryapp.preferences.UserPreference
import com.example.mystoryapp.ui.story.detail.DetailStoryActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.example.mystoryapp.R
import com.example.mystoryapp.database.Result

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private lateinit var storyMapsModel: MapsViewModel

    private lateinit var user: User
    private lateinit var userPreference: UserPreference
    private lateinit var stories: ArrayList<ListStoryItem>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var markers = arrayListOf<MarkerOptions>()
    private lateinit var markerIcon: BitmapDescriptor
    private lateinit var deviceLocation: LatLng
    private var reconnectingNum = 0

    companion object{
        private const val TAG = "StoryMapsActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.hide()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        markerIcon = vectorToBitmap(R.drawable.ic_map_marker_24, Color.parseColor(MAP_ICON_MARKER_COLOR))
        setMapStyle()
        getMyLocation()
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        userPreference = UserPreference(this)
        user = userPreference.getUser()

        val storyRepository = Injection.provideRepository(this, user.token.toString())
        storyMapsModel = MapsViewModel(storyRepository)

        getStoryData()

    }

    @Suppress("UNCHECKED_CAST")
    private fun getStoryData(){
        if (reconnectingNum++ < INTERNET_CONNECTION_MAX_RETRY_COUNT){
            if (reconnectingNum > 1)
                Toast.makeText(this, "Connecting", Toast.LENGTH_SHORT).show()

            storyMapsModel.getStory().observe(this){
                when (it) {
                    is Result.Loading -> { }
                    is Result.Error -> { getStoryData() }
                    is Result.Success -> {
                        if (it.data.error == false){
                            reconnectingNum = 0
                            val allStoryResponse = it.data
                            stories = allStoryResponse.listStory as ArrayList<ListStoryItem>
                            generateMarker()
                        }else{
                            getStoryData()
                        }
                    }
                }

            }
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun generateMarker(){
        val bounds = LatLngBounds.builder()
        for (i in stories.indices){
            if(stories[i].lat != null && stories[i].lon != null){
                val lat = stories[i].lat!!
                val lng = stories[i].lon!!

                val marker = MarkerOptions()
                val pos = LatLng(lat, lng)

                if ( !(lng < MAP_BOUNDARY_TOP_LATITUDE || lng > MAP_BOUNDARY_BOTTOM_LATITUDE ||
                            lat < MAP_BOUNDARY_BOTTOM_LONGITUDE || lat > MAP_BOUNDARY_TOP_LONGITUDE)){
                    bounds.include(pos)
                }

                marker.position(pos)
                marker.title(stories[i].name)
                marker.snippet(stories[i].description)
                marker.icon(markerIcon)

                markers.add(marker)
                mMap.addMarker(marker)?.tag = i
            }
        }
        val boundsBuilt = bounds.build()
        val cam = CameraUpdateFactory.newLatLngBounds(boundsBuilt, Constant.MAP_FIT_TO_MARKER_PADDING)
        setCameraPos(cam)

        mMap.setOnInfoWindowClickListener {
            val tag = it.tag.toString().toInt()
            tag.let {
                val data = stories[tag]
                val intent = Intent(this, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.USER_DETAIL_EXTRA, data)
                startActivity(intent)
            }
        }
    }

    private fun setCameraPos(cam: CameraUpdate){
        mMap.animateCamera(cam)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
                getCurrentLocation()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    deviceLocation = LatLng(location.latitude, location.longitude)
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "Permission is Required", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission( this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_night))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
}