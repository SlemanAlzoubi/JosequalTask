package sleman.alzoubi.josequaltask.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.kml.KmlLayer
import sleman.alzoubi.josequaltask.R
import sleman.alzoubi.josequaltask.model.MapResult
import sleman.alzoubi.josequaltask.ui.viewmodel.MapViewModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var progress: ProgressBar
    lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        progress = findViewById(R.id.progress_map)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        var lastMarkerOptions:MarkerOptions?=null

        viewModel.liveDataList.observe(this, Observer {
            progress.visibility= View.GONE
            for (item in it) {
                lastMarkerOptions = MarkerOptions()
                    .position(LatLng(item.lat?.toDouble()?:0.0, item.lng?.toDouble()?:0.0))
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_baseline_place_24))
                    .title(item.name)
                googleMap.addMarker(lastMarkerOptions!!)
            }

            lastMarkerOptions?.let {marker->
                zoomToPlace(googleMap,marker)
            }

            // show item information on place information when item is selected
            googleMap.setOnMarkerClickListener { marker ->
                val place = it.firstOrNull { it.name == marker.title }
                if (place != null) {
                    showItemInfoDialog(place)
                }
                true // consume the event
            }
        })

        val layer = KmlLayer(googleMap, R.raw.cluster, applicationContext)
        layer.addLayerToMap()

    }



   private fun showItemInfoDialog(item: MapResult) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_item_info, null)
        dialogView.findViewById<TextView>(R.id.tv_item_name).text = item.name

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(R.string.item_info_dialog_close_button) { dialog, _ -> dialog.dismiss() }

        builder.show()
    }


    private fun zoomToPlace(map:GoogleMap,marker:MarkerOptions){
        val zoomLevel = 16.0f // Change this value to adjust the zoom level
        val cameraPosition = CameraPosition.Builder()
            .target(marker.position)
            .zoom(zoomLevel)
            .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}