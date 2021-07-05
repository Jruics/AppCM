package ipvc.estg.appcm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteCreate : AppCompatActivity() {

    private lateinit var editTitleView: EditText
    private lateinit var editTypeView: EditText
    private lateinit var editDescriptionView: EditText
    private lateinit var shared_preferences: SharedPreferences
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        editTitleView = findViewById(R.id.edit_title)
        editTypeView = findViewById(R.id.edit_type)
        editDescriptionView = findViewById(R.id.edit_description)
        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0?.lastLocation!!
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
            }
        }

        createLocationRequest()

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    fun createNote(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val title = editTitleView.text.toString()
        val latitude = latitude
        val longitude = longitude
        val description = editDescriptionView.text.toString()
        val category = editTypeView.text.toString()
        val user_id = shared_preferences.getInt("id", 0)

        val call = request.create(
                title = title.toString(),
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                description = description,
                category = category,
                photo = "Photo",
                user_id = user_id)

        call.enqueue(object : Callback<OutputCreateNote> {
            override fun onResponse(call: Call<OutputCreateNote>, response: Response<OutputCreateNote>){
                if (response.isSuccessful){
                    val c: OutputCreateNote = response.body()!!
                    Toast.makeText(this@NoteCreate, c.statusMessage, Toast.LENGTH_LONG).show()
                    val intent = Intent(this@NoteCreate, NotesOnMapActivity::class.java)
                    startActivity(intent);
                    finish()

                }
            }
            override fun onFailure(call: Call<OutputCreateNote>, t: Throwable){
                Toast.makeText(this@NoteCreate,"${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }
}