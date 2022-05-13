package app.quic.mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import app.quic.mobile.R
import app.quic.mobile.models.*
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var emailField: EditText
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.register_button_register)
        emailField = findViewById(R.id.email_register)
        usernameField = findViewById(R.id.username_register)
        passwordField = findViewById(R.id.password_register)

        registerButton.setOnClickListener {
            val registerModel = RegisterModel(emailField.text.toString(), usernameField.text.toString(), passwordField.text.toString())
            val registerCall: Call<UserModel> = ApiClient.getService().register(registerModel)

            registerCall.enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    if(response.isSuccessful) {
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                    } else {
                        val gson = Gson()
                        val error = gson.fromJson(response.errorBody()?.string(), ErrorModel::class.java)
                        Toast.makeText(
                            applicationContext,
                            error.title,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
                }

            })
        }
    }
}