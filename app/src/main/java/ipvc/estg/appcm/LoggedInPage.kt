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
    private lateinit var textWelcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in_menu)

    }

}