package ipvc.estg.appcm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.appcm.adapters.ListNotesAdapter
import ipvc.estg.appcm.adapters.NoteAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAllNotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_notes)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getNotes()
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerview_notes)

        call.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>){
                if (response.isSuccessful){
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@ListAllNotes)
                        adapter = ListNotesAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<Note>>, t: Throwable){
                Toast.makeText(this@ListAllNotes,"${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}