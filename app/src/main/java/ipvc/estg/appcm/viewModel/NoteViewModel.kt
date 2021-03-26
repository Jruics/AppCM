package ipvc.estg.appcm.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.appcm.db.NoteDB
import ipvc.estg.appcm.db.NoteRepository
import ipvc.estg.appcm.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    val allNotes: LiveData<List<Note>>

    init{
        val notesDao = NoteDB.getDatabase(application, viewModelScope).noteDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.allNotes
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteByTitle(title: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByTitle(title)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }
}