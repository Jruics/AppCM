package ipvc.estg.appcm.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.appcm.entities.Note

@Dao
interface NoteDao {
    @Query("SELECT * from note_table ORDER BY title ASC")
    fun getAlphabetizedNotes(): LiveData<List<Note>>

    @Query("SELECT * from note_table WHERE title == :title")
    fun getNotesFromTitle(title: String): LiveData<List<Note>>

    @Query("SELECT * from note_table WHERE address == :address")
    fun getNotesFromAddress(address: String): LiveData<List<Note>>

    @Query("SELECT * from note_table WHERE body == :body")
    fun getNotesFromBody(body: String): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun updateCity(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll();

    @Query("DELETE FROM note_table WHERE title == :title")
    suspend fun deleteByTitle(title :String)

    @Query("UPDATE note_table SET title = :title AND body = :body AND address = :address  WHERE id == :id")
    suspend fun updateNoteFromTitle(title: String, body: String, address: String, id: Int)
}