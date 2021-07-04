package ipvc.estg.appcm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditNote : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)


        var editTitleView: EditText = findViewById(R.id.edit_type)
        var editBodyView: EditText = findViewById(R.id.edit_description)
        var editAddressView: EditText = findViewById(R.id.edit_address)

        var id = intent.getStringExtra(PARAM_ID)
        var title = intent.getStringExtra(PARAM_TITLE)
        var body = intent.getStringExtra(PARAM_BODY)
        var address = intent.getStringExtra(PARAM_ADDRESS)

        editTitleView.setText(title.toString())
        editBodyView.setText(body.toString())
        editAddressView.setText(address.toString())

        val button = findViewById<Button>(R.id.button_edit)
        button.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(EXTRA_REPLY_ID, id)
            if (TextUtils.isEmpty(editTitleView.text)  || TextUtils.isEmpty(editBodyView.text) || TextUtils.isEmpty(editAddressView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val edit_title = editTitleView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_TITLE, edit_title)

                val edit_body = editBodyView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_BODY, edit_body)

                val edit_address = editAddressView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_ADDRESS, edit_address)

                replyIntent.putExtra(EXTRA_TYPE, "EDIT")

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        val button_delete = findViewById<Button>(R.id.button_delete)
        button_delete.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text)  || TextUtils.isEmpty(editBodyView.text) || TextUtils.isEmpty(editAddressView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_DELETE_ID, id)
                replyIntent.putExtra(EXTRA_DELETE_ID, id)
                replyIntent.putExtra(EXTRA_DELETE_ID, id)
                replyIntent.putExtra(EXTRA_TYPE, "DELETE")
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object{

        const val EXTRA_REPLY_ID = "id"
        const val EXTRA_REPLY_ADDRESS = "address"
        const val EXTRA_REPLY_BODY = "body"
        const val EXTRA_REPLY_TITLE = "title"
        const val EXTRA_DELETE_ID = "delete_id"
        const val EXTRA_TYPE = ""
        const val STATUS = ""

        //const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}