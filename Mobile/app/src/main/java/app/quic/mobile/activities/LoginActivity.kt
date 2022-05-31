package app.quic.mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import app.quic.mobile.services.LoggedInUser
import app.quic.mobile.R
import app.quic.mobile.dialogs.InfoDialog
import app.quic.mobile.models.ErrorModel
import app.quic.mobile.models.LoginModel
import app.quic.mobile.models.TokenModel
import app.quic.mobile.models.UserModel
import app.quic.mobile.services.ApiClient
import com.google.firebase.messaging.FirebaseMessaging
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

        FirebaseMessaging.getInstance().subscribeToTopic("all")

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
                        when {
                            LoggedInUser.getUserRole() == "admin" -> {
                                val dialog = InfoDialog("Admins must use dedicated web page")
                                dialog.show(supportFragmentManager, "Information dialog")
                            }
                            LoggedInUser.getUserRole() == null -> {
                                val dialog = InfoDialog("User does not have a role")
                                dialog.show(supportFragmentManager, "Information dialog")
                            }
                            else -> {
                                getUserInfo()
                            }
                        }
                    } else {
                        val gson = Gson()
                        val error = gson.fromJson(response.errorBody()?.string(), ErrorModel::class.java)
                        val dialog = InfoDialog(error.title)
                        dialog.show(supportFragmentManager, "Information dialog")
                    }
                }

                override fun onFailure(call: Call<TokenModel>, t: Throwable) {
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

    fun getUserInfo(){
        val isUserCall: Call<UserModel> = ApiClient.getService().getUserData(usernameField.text.toString())
        isUserCall.enqueue(object : Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if(response.isSuccessful){
                    var user = response.body()
                    if(!user!!.emailConfirmed){
                        val dialog = InfoDialog("Please confirm your email!")
                        dialog.show(supportFragmentManager, "Information dialog")
                    }
                    else{
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
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