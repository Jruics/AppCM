package ipvc.estg.appcm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoggedInPage : AppCompatActivity() {
    private lateinit var shared_preferences: SharedPreferences
    private lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in_menu)

        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        val username = shared_preferences.getString("username", "")
        welcomeText = findViewById(R.id.welcomeUser)
        var welcome = getString(R.string.welcomeSignedUser)
        welcomeText.text = "$welcome " + "$username"

    }

    fun notesMap(view: View){
        val intent = Intent(this@LoggedInPage, NotesOnMapActivity::class.java)
        startActivity(intent)
    }

    fun noteList(view: View){
        val intent = Intent(this@LoggedInPage, ListAllNotes::class.java)
        startActivity(intent)
    }

    fun noteCreate(view: View){
        val intent = Intent(this@LoggedInPage, NoteCreate::class.java)
        startActivity(intent)
    }

    fun logout(view: View){
        val shared_preferences_edit : SharedPreferences.Editor = shared_preferences.edit()
        shared_preferences_edit.clear()
        shared_preferences_edit.apply()

        val intent = Intent(this@LoggedInPage, LoginPage::class.java)
        startActivity(intent)
        finish()
    }
}