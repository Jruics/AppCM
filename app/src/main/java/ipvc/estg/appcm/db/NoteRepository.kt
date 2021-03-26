package ipvc.estg.appcm.db

import androidx.lifecycle.LiveData
import ipvc.estg.appcm.dao.NoteDao
import ipvc.estg.appcm.entities.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAlphabetizedNotes()

    fun getNotesByTitle(title: String): LiveData<List<Note>>{
        return noteDao.getNotesFromTitle(title)
    }

    fun getNotesByBody(body: String): LiveData<List<Note>>{
        return noteDao.getNotesFromBody(body)
    }

    fun getNotesByAddress(address: String): LiveData<List<Note>>{
        return noteDao.getNotesFromAddress(address)
    }

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun deleteAll(){
        noteDao.deleteAll()
    }

    suspend fun deleteByTitle(title: String){
        noteDao.deleteByTitle(title)
    }

    suspend fun updateNote(note: Note){
        noteDao.updateCity(note)
    }

    suspend fun udpateNoteFromId(title: String, body: String, address: String, id: Int){
        noteDao.updateNoteFromTitle(title, body, address, id)
    }
}