package ipvc.estg.appcm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class LoginPage : AppCompatActivity() {
    private lateinit var loginUsernameBox: EditText
    private lateinit var loginPasswordBox: EditText
    private lateinit var loginCheckboxRemember: CheckBox
    private lateinit var shared_preferences: SharedPreferences
    private var remember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        actionBar?.hide()

        loginUsernameBox = findViewById(R.id.login_username)
        loginPasswordBox = findViewById(R.id.login_password)
        loginCheckboxRemember = findViewById(R.id.login_checkbox)

        shared_preferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        remember = shared_preferences.getBoolean("remember", false)

        if(remember){
            val intent = Intent(this@LoginPage, LoggedInPage::class.java)
            startActivity(intent);
            finish()
        }

    }

    fun loginCheck(view: View) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val name = loginUsernameBox.text.toString()
        val password = loginPasswordBox.text.toString()
        val checked_remember: Boolean = loginCheckboxRemember.isChecked
        val call = request.loginPage(name = name, password = password)

        call.enqueue(object : Callback<OutputLoginPage> {
            override fun onResponse(call: Call<OutputLoginPage>, response: Response<OutputLoginPage>){
                if (response.isSuccessful){
                    val c: OutputLoginPage = response.body()!!
                    if(TextUtils.isEmpty(loginUsernameBox.text) || TextUtils.isEmpty(loginPasswordBox.text)) {
                        Toast.makeText(this@LoginPage, "Login error", Toast.LENGTH_LONG).show()
                    }else{
                        if(c.status =="false"){
                            Toast.makeText(this@LoginPage, c.statusMessage, Toast.LENGTH_LONG).show()
                        }else{
                            val shared_preferences_edit : SharedPreferences.Editor = shared_preferences.edit()
                            shared_preferences_edit.putString("name", name)
                            shared_preferences_edit.putString("password", password)
                            shared_preferences_edit.putInt("id", c.id)
                            shared_preferences_edit.putBoolean("remember", checked_remember)
                            shared_preferences_edit.apply()

                            val intent = Intent(this@LoginPage, LoggedInPage::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<OutputLoginPage>, t: Throwable){
                Toast.makeText(this@LoginPage,"${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}