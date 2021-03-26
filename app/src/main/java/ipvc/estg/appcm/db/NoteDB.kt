package ipvc.estg.appcm.db

import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.appcm.dao.NoteDao
import ipvc.estg.appcm.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
public abstract class NoteDB : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){

        override fun onOpen(db: SupportSQLiteDatabase){
            super.onOpen(db)
            INSTANCE ?.let{database ->
                scope.launch{
                    var noteDao = database.noteDao()

                    /*
                    //Codigo para popular BD
                    noteDao.deleteAll()

                    var nota = Note(1, "Nota 1", "Body 1", "Morada 1")
                    noteDao.insert(nota)
                    nota = Note(2, "Nota 2", "Body 2", "Morada 2")
                    noteDao.insert(nota)
                    */
                }
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: NoteDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDB::class.java,
                    "notes_database"
                )
                    //.fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}