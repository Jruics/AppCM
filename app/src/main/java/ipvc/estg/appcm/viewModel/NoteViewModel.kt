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

    fun update(id: Int?, title: String, body: String, address: String) = viewModelScope.launch(Dispatchers.IO){
        repository.update(id, title, body, address)
    }

    fun delete(id: Int?) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(id)
    }

    fun deleteByTitle(title: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByTitle(title)
    }

    fun deleteByID(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByID(id)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun updateNoteFromId(title: String, body: String, address: String, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNoteFromId(title, body, address, id);
    }
}