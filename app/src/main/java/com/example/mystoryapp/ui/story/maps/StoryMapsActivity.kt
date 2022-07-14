package com.example.mystoryapp.ui.story.maps

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.mystoryapp.api.response.ListStoryItem
import com.example.mystoryapp.database.Injection
import com.example.mystoryapp.databinding.ActivityStoryMapsBinding
import com.example.mystoryapp.preferences.UserPreference
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.example.mystoryapp.R
import com.example.mystoryapp.database.Result

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding
    private lateinit var storyMapsModel: MapsViewModel
    private lateinit var stories: ArrayList<ListStoryItem>
    private lateinit var markerIcon: BitmapDescriptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.hide()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        markerIcon = vectorToBitmap(R.drawable.ic_map_marker_24, Color.parseColor("#C12E2E"))
        
        try {
            if (!mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_night))) {
                Log.e("TAG", "MAP STYLE ERROR.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("TAG", "MAP STYLE ERROR: ", exception)
        }
        
        storyMapsModel = MapsViewModel(Injection.provideRepository(this, UserPreference(this).getUser().token.toString()))
        storyMapsModel.getStory().observe(this){
            when (it) {
                is Result.Loading -> {
                    Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    if (it.data.error == false){
                        stories = it.data.listStory as ArrayList<ListStoryItem>
                        
                        for (i in stories.indices){
                            val lat = stories[i].lat
                            val lng = stories[i].lon
                            if(lat != null && lng != null){
                                val marker = MarkerOptions()
                                marker.position(LatLng(lat, lng))
                                marker.title(stories[i].name)
                                marker.snippet(stories[i].description)
                                marker.icon(markerIcon)
                                mMap.addMarker(marker)
                            }
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(-5.666075,114.149139)))
                    }
                }
            }
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
}