package ipvc.estg.appcm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotesOnMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private lateinit var myMap: GoogleMap
    private lateinit var  notes: List<Note>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val newEditNoteActivityRequestCode = 1
    private lateinit var shared_preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotes()
        var position: LatLng
        val user_id = shared_preferences.getInt("id", 0)

        call.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>){
                if (response.isSuccessful){
                    notes = response.body()!!
                    for (note in notes){
                        position = LatLng(note.latitude.toDouble(), note.longitude.toDouble())
                        if(note.user_id == user_id){
                            myMap.addMarker(MarkerOptions().position(position).title(note.id.toString()).snippet(note.category + "-" + note.description))
                                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        }else{
                            myMap.addMarker(MarkerOptions().position(position).title(note.id.toString()).snippet(note.category + "-" + note.description))
                        }

                    }
                }
            }
            override fun onFailure(call: Call<List<Note>>, t: Throwable){
                Toast.makeText(this@NotesOnMapActivity,"${t.message}", Toast.LENGTH_LONG).show()
            }
        })


    }

    override fun onInfoWindowClick(p0: Marker?) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call : Call<List<Note>> = request.getNotesById(p0!!.title)
        val user_id = shared_preferences.getInt("id", 0)

        call.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if(response.isSuccessful){
                    notes = response.body()!!
                    for(note in notes){
                        if(note.user_id == user_id){
                            Toast.makeText(this@NotesOnMapActivity, note.description, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@NotesOnMapActivity, EditOrDeleteNote::class.java)
                            intent.putExtra(PARAM_ID, note.id.toString())
                            intent.putExtra(PARAM_TYPE, note.category)
                            intent.putExtra(PARAM_DESCRIPTION, note.description)
                            intent.putExtra(PARAM_LATITUDE, note.latitude)
                            intent.putExtra(PARAM_LONGITUDE, note.longitude)
                            intent.putExtra(PARAM_USER_ID, note.user_id.toString())
                            startActivityForResult(intent, newEditNoteActivityRequestCode)
                        }else{
                            Toast.makeText(this@NotesOnMapActivity,R.string.notOwnedNote, Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }
            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@NotesOnMapActivity,"${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        myMap.setOnInfoWindowClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        } else{
            //1
            myMap.isMyLocationEnabled = true

            //2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) {location ->
                //3
                if(location != null){
                    lastLocation = location
                    //Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // EDITAR E APAGAR NOTA
        if (requestCode == newEditNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var id = data?.getStringExtra(EditOrDeleteNote.EDIT_ID)
            var id_delete = data?.getStringExtra(EditOrDeleteNote.DELETE_ID)
            var edit_type = data?.getStringExtra(EditOrDeleteNote.EDIT_TYPE).toString()
            var edit_title = data?.getStringExtra(EditOrDeleteNote.EDIT_TITLE).toString()
            var edit_description = data?.getStringExtra(EditOrDeleteNote.EDIT_DESCRIPTION).toString()
            var edit_latitude = data?.getDoubleExtra(EditOrDeleteNote.EDIT_LATITUDE, 0.0).toString()
            var edit_longitude = data?.getDoubleExtra(EditOrDeleteNote.EDIT_LONGITUDE, 0.0).toString()
            val user_id = shared_preferences.getInt("id", 0)

            if(data?.getStringExtra(EditOrDeleteNote.STATUS) == "EDIT"){
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.edit(
                        id = id,
                        title = edit_title,
                        latitude = edit_latitude,
                        longitude = edit_longitude,
                        category = edit_type,
                        description = edit_description,
                        photo = "Imagem",
                        user_id = user_id)

                call.enqueue(object : Callback<OutputEditNote> {
                    override fun onResponse(call: Call<OutputEditNote>, response: Response<OutputEditNote>){
                        if (response.isSuccessful){
                            val c: OutputEditNote = response.body()!!
                            Toast.makeText(this@NotesOnMapActivity, c.statusMessage, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@NotesOnMapActivity, NotesOnMapActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<OutputEditNote>, t: Throwable){
                        Toast.makeText(this@NotesOnMapActivity,"${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else if(data?.getStringExtra(EditOrDeleteNote.STATUS) == "DELETE"){

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.delete(
                        id = id_delete)
                Log.d("****DELETE", id_delete.toString())

                call.enqueue(object : Callback<OutputDeleteNote> {
                    override fun onResponse(call: Call<OutputDeleteNote>, response: Response<OutputDeleteNote>){
                        if (response.isSuccessful){
                            val c: OutputDeleteNote = response.body()!!
                            Toast.makeText(this@NotesOnMapActivity, c.statusMessage, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@NotesOnMapActivity, NotesOnMapActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<OutputDeleteNote>, t: Throwable){
                        Toast.makeText(this@NotesOnMapActivity,"${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            if(data?.getStringExtra(EditNote.STATUS) == "EDIT"){
                Toast.makeText(this, R.string.editNoChangedFields, Toast.LENGTH_SHORT).show()
            } else if(data?.getStringExtra(EditNote.STATUS) == "DELETE"){
                Toast.makeText(this, R.string.deleteNoChangedFields, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val STATUS = ""
        const val DELETE_ID = "DELETE_ID"
        const val PARAM_ID = "PARAM_ID"
        const val PARAM_TYPE = "PARAM_TYPE"
        const val PARAM_DESCRIPTION = "PARAM_DESCRIPTION"
        const val PARAM_LATITUDE = "PARAM_LATITUDE"
        const val PARAM_LONGITUDE = "PARAM_LONGITUDE"
        const val PARAM_USER_ID = "PARAM_USER_ID"
    }

}