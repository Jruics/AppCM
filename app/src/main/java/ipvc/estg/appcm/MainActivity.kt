package ipvc.estg.appcm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.appcm.adapters.NoteAdapter
import ipvc.estg.appcm.entities.Note
import ipvc.estg.appcm.viewModel.NoteViewModel

const val PARAM_ID: String = "id"
const val PARAM_TITLE: String = "title"
const val PARAM_BODY: String = "body"
const val PARAM_ADDRESS: String = "address"

//class MainActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener{
class MainActivity : AppCompatActivity(), OnItemClickListener{

    private lateinit var noteViewModel: NoteViewModel
    private val newNoteActivityRequestCode = 1
    private val changeNoteActivityRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this) { notes ->
            notes?.let {adapter.setNotes(it)}
        }

        val fab1 = findViewById<FloatingActionButton>(R.id.fab1)
        fab1.setOnClickListener{
            val intent = Intent(this@MainActivity, AddNote::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)
        }
    }

    override fun onItemClickListener(data: Note) {
        val intent = Intent(this, EditNote::class.java)
        intent.putExtra(PARAM_ID, data.id.toString())
        intent.putExtra(PARAM_TITLE, data.title.toString())
        intent.putExtra(PARAM_BODY, data.body.toString())
        intent.putExtra(PARAM_ADDRESS, data.address.toString())
        startActivityForResult(intent, changeNoteActivityRequestCode)
        //Log.e("***ID", data.id.toString())
        //Toast.makeText(this, "Note touched" + data.id.toString() + data.title.toString() + data.address.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //CRIAR NOTA
        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AddNote.EXTRA_REPLY_TITLE).toString()
            val body = data?.getStringExtra(AddNote.EXTRA_REPLY_BODY).toString()
            val address = data?.getStringExtra(AddNote.EXTRA_REPLY_ADDRESS).toString()
            val note = Note(title = title, body = body, address = address)
            noteViewModel.insert(note)


        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, R.string.errorCreatingNote, Toast.LENGTH_SHORT).show()
        }

        // EDITAR E APAGAR NOTA
        if (requestCode == changeNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var edit_title = data?.getStringExtra(EditNote.EXTRA_REPLY_TITLE).toString()
            var edit_body = data?.getStringExtra(EditNote.EXTRA_REPLY_BODY).toString()
            var edit_address = data?.getStringExtra(EditNote.EXTRA_REPLY_ADDRESS).toString()

            var id = data?.getStringExtra(EditNote.EXTRA_REPLY_ID)
            var id_delete = data?.getStringExtra(EditNote.EXTRA_DELETE_ID)
            if(data?.getStringExtra(EditNote.EXTRA_TYPE) == "EDIT"){
                noteViewModel.update(id?.toIntOrNull(), edit_title, edit_body, edit_address)
                Toast.makeText(this, R.string.noteEditedSuccessfully, Toast.LENGTH_SHORT).show()
            } else if(data?.getStringExtra(EditNote.EXTRA_TYPE) == "DELETE"){
                noteViewModel.delete(id_delete?.toIntOrNull())
                Toast.makeText(this, R.string.noteDeletedSuccessfully, Toast.LENGTH_SHORT).show()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if(data?.getStringExtra(EditNote.EXTRA_TYPE) == "EDIT"){
                Toast.makeText(this, R.string.errorEditingNote, Toast.LENGTH_SHORT).show()
            } else if(data?.getStringExtra(EditNote.EXTRA_TYPE) == "DELETE"){
                Toast.makeText(this, R.string.errorDeletingNote, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Funcionalidade implementada no Sprint03
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.apagarTudo -> {
                noteViewModel.deleteAll()
                true
            }


            R.id.todasNotas -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = NoteAdapter(this, this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
                noteViewModel.allNotes.observe(this, Observer { notes ->
                    // Update the cached copy of the words in the adapter.
                    notes?.let { adapter.setNotes(it) }
                })


                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}