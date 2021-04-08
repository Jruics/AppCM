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

class EditNote : AppCompatActivity() {

    /*
    private lateinit var editWordViewTitle: EditText
    private lateinit var editWordViewBody: EditText
    private lateinit var editWordViewAddress: EditText
    private lateinit var editWordViewOriginalTitle: EditText
    */

    private lateinit var editWordViewId: EditText
    private lateinit var editWordViewTitle: EditText
    private lateinit var editWordViewBody: EditText
    private lateinit var editWordViewAddress: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        /*editWordViewOriginalTitle = findViewById(R.id.original_title)
        editWordViewTitle = findViewById(R.id.edit_title)
        editWordViewBody = findViewById(R.id.edit_body)
        editWordViewAddress = findViewById(R.id.edit_address)*/

        editWordViewId = findViewById(R.id.original_id)
        editWordViewTitle = findViewById(R.id.edit_title)
        editWordViewBody = findViewById(R.id.edit_body)
        editWordViewAddress = findViewById(R.id.edit_address)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editWordViewId.text) || TextUtils.isEmpty(editWordViewTitle.text) || TextUtils.isEmpty(editWordViewBody.text) || TextUtils.isEmpty(editWordViewAddress.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                val replyIntent = Intent()

                val id = editWordViewId.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_ID, id)

                val title = editWordViewTitle.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_TITLE, title)

                val body = editWordViewBody.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_BODY, body)

                val address = editWordViewAddress.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_ADDRESS, address)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        /*val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editWordViewTitle.text) || TextUtils.isEmpty(editWordViewBody.text) || TextUtils.isEmpty(editWordViewAddress.text) || TextUtils.isEmpty(editWordViewOriginalTitle.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                val replyIntent = Intent()

                val title = editWordViewTitle.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_TITLE, title)

                val body = editWordViewBody.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_BODY, body)

                val address = editWordViewAddress.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_ADDRESS, address)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }*/
    }

    companion object{

        /*
        const val EXTRA_REPLY_ADDRESS = "address"
        const val EXTRA_REPLY_BODY = "body"
        const val EXTRA_REPLY_TITLE = "title"
        const val EXTRA_REPLY_ORIGINAL_TITLE = "originalTitle"
        */

        const val EXTRA_REPLY_ID = "id"
        const val EXTRA_REPLY_ADDRESS = "address"
        const val EXTRA_REPLY_BODY = "body"
        const val EXTRA_REPLY_TITLE = "title"

        //const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}