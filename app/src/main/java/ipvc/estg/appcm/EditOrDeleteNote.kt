package ipvc.estg.appcm

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import ipvc.estg.appcm.NotesOnMapActivity.Companion.PARAM_DESCRIPTION
import ipvc.estg.appcm.NotesOnMapActivity.Companion.PARAM_ID
import ipvc.estg.appcm.NotesOnMapActivity.Companion.PARAM_TYPE

class EditOrDeleteNote : AppCompatActivity() {
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
        setContentView(R.layout.activity_edit_or_delete_note)

        editTypeView = findViewById(R.id.edit_type)
        editDescriptionView = findViewById(R.id.edit_description)

        var id = intent.getStringExtra(PARAM_ID)
        var type = intent.getStringExtra(PARAM_TYPE)
        var description = intent.getStringExtra(PARAM_DESCRIPTION)
        editTypeView.setText(type.toString())
        editDescriptionView.setText(description.toString())
        Log.d("****TIPO", type.toString())

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



        val button = findViewById<Button>(R.id.button_edit)
        button.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EDIT_ID, id)
            if (TextUtils.isEmpty(editTypeView.text)  || TextUtils.isEmpty(editDescriptionView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val edit_type = editTypeView.text.toString()
                replyIntent.putExtra(EDIT_TYPE, edit_type)
                val edit_description = editDescriptionView.text.toString()
                replyIntent.putExtra(EDIT_DESCRIPTION, edit_description)
                val latitude = latitude
                replyIntent.putExtra(EDIT_LATITUDE, latitude)
                val longitude = longitude
                replyIntent.putExtra(EDIT_LONGITUDE, longitude)
                replyIntent.putExtra(STATUS, "EDIT")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }

        val button_delete = findViewById<Button>(R.id.button_delete)
        button_delete.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTypeView.text)  || TextUtils.isEmpty(editDescriptionView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(DELETE_ID, id)
                replyIntent.putExtra(STATUS, "DELETE")
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

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

    companion object {
        const val STATUS = ""
        const val DELETE_ID = "DELETE_ID"
        const val EDIT_ID = "EDIT_ID"
        const val EDIT_TITLE = "EDIT_TITLE"
        const val EDIT_TYPE = "EDIT_TYPE"
        const val EDIT_DESCRIPTION = "EDIT_DESCRIPTION"
        const val EDIT_LATITUDE = "EDIT_LATITUDE"
        const val EDIT_LONGITUDE = "EDIT_LONGITUDE"
    }
}