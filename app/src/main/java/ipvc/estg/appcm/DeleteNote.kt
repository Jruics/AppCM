package ipvc.estg.appcm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.widget.ActionMenuView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class DeleteNote : AppCompatActivity() {


    //private lateinit var editWordViewOriginalTitle: EditText
    private lateinit var editWordViewID: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_note)

        /*
        editWordViewOriginalTitle = findViewById(R.id.title_to_delete)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editWordViewOriginalTitle.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                val replyIntent = Intent()

                val title = editWordViewOriginalTitle.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_ORIGINAL_TITLE, title)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
        */
        editWordViewID = findViewById(R.id.title_to_delete)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editWordViewID.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                val replyIntent = Intent()

                val id = editWordViewID.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_ORIGINAL_ID, id)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object{

        //const val EXTRA_REPLY_ORIGINAL_TITLE = "originalTitle"
        const val EXTRA_REPLY_ORIGINAL_ID = "originalID"

        //const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}