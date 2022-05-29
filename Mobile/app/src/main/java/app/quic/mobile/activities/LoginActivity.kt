package app.quic.mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import app.quic.mobile.services.LoggedInUser
import app.quic.mobile.R
import app.quic.mobile.models.ErrorModel
import app.quic.mobile.models.LoginModel
import app.quic.mobile.models.TokenModel
import app.quic.mobile.models.UserModel
import app.quic.mobile.services.ApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)
        registerButton = findViewById(R.id.register_button)
        usernameField = findViewById(R.id.username_login)
        passwordField = findViewById(R.id.password_login)

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val loginModel = LoginModel(usernameField.text.toString(), passwordField.text.toString())
            val loginCall: Call<TokenModel> = ApiClient.getService().login(loginModel)

            loginCall.enqueue(object : Callback<TokenModel> {
                override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {
                    if(response.isSuccessful) {
                        LoggedInUser.setConnection(response.body()?.token, usernameField.text.toString())
                        if(LoggedInUser.getUserRole() == "admin")
                            Toast.makeText(
                                applicationContext,
                                "Admins must use dedicated site",
                                Toast.LENGTH_LONG
                            ).show()
                        else if(LoggedInUser.getUserRole() == null){
                            Toast.makeText(
                                applicationContext,
                                "User does not have a role",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            getUserInfo()
                        }
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

                override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    fun getUserInfo(){
        val isUserCall: Call<UserModel> = ApiClient.getService().getUserData(usernameField.text.toString())
        isUserCall.enqueue(object : Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if(response.isSuccessful){
                    var user = response.body()
                    if(!user!!.emailConfirmed){
                        Toast.makeText(
                            applicationContext,
                            "Please confirm your email!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else{
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
            }
        })
    }
}