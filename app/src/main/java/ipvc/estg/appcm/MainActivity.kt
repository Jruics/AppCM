package ipvc.estg.appcm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private val newWordActivityRequestCode = 1
    private val deleteNoteRequestCode = 2
    private val editNoteRequestCode = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this) { notes ->
            notes?.let {adapter.setNotes(it)}
        }

        val fab1 = findViewById<FloatingActionButton>(R.id.fab1)
        fab1.setOnClickListener{
            val intent = Intent(this@MainActivity, AddNote::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

        val fab3 = findViewById<FloatingActionButton>(R.id.fab2)
        fab3.setOnClickListener{
            val intent = Intent(this@MainActivity, DeleteNote::class.java)
            startActivityForResult(intent, deleteNoteRequestCode)
        }

        val fab2 = findViewById<FloatingActionButton>(R.id.fab3)
        fab2.setOnClickListener{
            val intent = Intent(this@MainActivity, EditNote::class.java)
            startActivityForResult(intent, editNoteRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            /*
            data?.getStringExtra(AddNote.EXTRA_REPLY)?.let {
                val tempTitle = data.getStringExtra("title").toString()
                val tempBody = data.getStringExtra("body").toString()
                val tempAddress = data.getStringExtra("address").toString()

                val note = Note(title = it, body = "body", address = "address")
                noteViewModel.insert(note)
            }
            */

            val newTitle = data?.getStringExtra(AddNote.EXTRA_REPLY_TITLE)
            val newBody = data?.getStringExtra(AddNote.EXTRA_REPLY_BODY)
            val newAddress = data?.getStringExtra(AddNote.EXTRA_REPLY_ADDRESS)

            if (newTitle != null && newBody != null && newAddress != null) {
                val note = Note(title = newTitle, body = newBody, address = newAddress)
                noteViewModel.insert(note)
            }

            /*
            var title = data?.getStringExtra(AddNote.EXTRA_REPLY_TITLE).toString()
            var body = data?.getStringExtra(AddNote.EXTRA_REPLY_BODY).toString()
            var address = data?.getStringExtra(AddNote.EXTRA_REPLY_ADDRESS).toString()

            val note = Note(title = title, body = body, address = address)
            noteViewModel.insert(note)
            */
        }else if(requestCode == editNoteRequestCode && resultCode == Activity.RESULT_OK){
            /*
            val originalTitle = data?.getStringExtra(EditNote.EXTRA_REPLY_ORIGINAL_TITLE)
            val newTitle = data?.getStringExtra(EditNote.EXTRA_REPLY_TITLE)
            val newBody = data?.getStringExtra(EditNote.EXTRA_REPLY_BODY)
            val newAddress = data?.getStringExtra(EditNote.EXTRA_REPLY_ADDRESS)


            if (newTitle != null && newBody != null && newAddress != null && originalTitle == newTitle) {
                val note = Note(title = newTitle, body = newBody, address = newAddress)
                noteViewModel.updateNote(note)
            }
            */

            val originalId = data?.getStringExtra(EditNote.EXTRA_REPLY_ID)
            val newTitle = data?.getStringExtra(EditNote.EXTRA_REPLY_TITLE)
            val newBody = data?.getStringExtra(EditNote.EXTRA_REPLY_BODY)
            val newAddress = data?.getStringExtra(EditNote.EXTRA_REPLY_ADDRESS)


            if (originalId != null && newTitle != null && newBody != null && newAddress != null) {
                //val note = Note(title = newTitle, body = newBody, address = newAddress)
                //noteViewModel.updateNote(note)
                noteViewModel.updateNoteFromId(newTitle, newBody, newAddress, originalId.toInt())
                println(originalId.toInt())
                println(newTitle)
                println(newBody)
                println(newAddress)
            }

        }else if(requestCode == deleteNoteRequestCode && resultCode == Activity.RESULT_OK){

            /*
            val originalTitle = data?.getStringExtra(DeleteNote.EXTRA_REPLY_ORIGINAL_TITLE)

            if (originalTitle != null) {
                noteViewModel.deleteByTitle(originalTitle)
            }
             */
            val originalId = data?.getStringExtra(DeleteNote.EXTRA_REPLY_ORIGINAL_ID)

            if (originalId != null) {
                noteViewModel.deleteByID(originalId.toInt())
                //println(originalId.toInt())
            }
        }
        else{
            Toast.makeText(applicationContext, "Nota vazia; não inserida", Toast.LENGTH_LONG).show()
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
                val adapter = NoteAdapter(this)
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

            /*
            R.id.apagarID -> {
                //noteViewModel.deleteByTitle("Aveiro")
                true
            }

            R.id.alterarID -> {
                //val note = Note(id = 1, title = "xxx", body = "xxx", address = "xxx")
                //noteViewModel.updateNote(note)
                true
            }
            */


            else -> super.onOptionsItemSelected(item)
        }
    }


    
}