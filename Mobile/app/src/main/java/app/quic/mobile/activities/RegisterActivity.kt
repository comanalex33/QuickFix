package app.quic.mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import app.quic.mobile.R
import app.quic.mobile.dialogs.InfoDialog
import app.quic.mobile.models.*
import app.quic.mobile.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var emailField: EditText
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var passwordField2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.register_button_register)
        emailField = findViewById(R.id.email_register)
        usernameField = findViewById(R.id.username_register)
        passwordField = findViewById(R.id.password_register)
        passwordField2 = findViewById(R.id.password_register_2)

        registerButton.setOnClickListener {

            if(passwordField.text.toString() != passwordField2.text.toString()) {
                val dialog = InfoDialog("Incorrect password")
                dialog.show(supportFragmentManager, "Information dialog")
            } else {

                val registerModel = RegisterModel(
                    emailField.text.toString(),
                    usernameField.text.toString(),
                    passwordField.text.toString()
                )
                val registerCall: Call<UserModel> = ApiClient.getService().register(registerModel)

                registerCall.enqueue(object : Callback<UserModel> {
                    override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                        if (response.isSuccessful) {
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val errorMessage = response.errorBody()?.string()
                            if(errorMessage != null) {
                                val dialog = InfoDialog(errorMessage)
                                dialog.show(supportFragmentManager, "Information dialog")
                            } else {
                                val dialog = InfoDialog("Something went wrong")
                                dialog.show(supportFragmentManager, "Information dialog")
                            }
                        }
                    }

                    override fun onFailure(call: Call<UserModel>, t: Throwable) {
                        if(t.message != null){
                            val dialog = InfoDialog(t.message!!)
                            dialog.show(supportFragmentManager, "Information dialog")
                        } else {
                            val dialog = InfoDialog("Something went wrong")
                            dialog.show(supportFragmentManager, "Information dialog")
                        }
                    }

                })
            }
        }
    }

}