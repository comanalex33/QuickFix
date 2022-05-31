package app.quic.mobile

import android.content.Context
import android.util.Base64
import android.widget.Toast
import app.quic.mobile.models.MessageModel
import app.quic.mobile.models.TokenDataModel
import app.quic.mobile.services.ApiClient
import app.quic.mobile.services.LoggedInUser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelperClass {
    companion object {
        fun String.deserializeTokenInfo(): TokenDataModel {
            val gson = Gson()
            return gson.fromJson(this, TokenDataModel::class.java)
        }
        fun String.decodeBase64(): String {
            return Base64.decode(this, Base64.DEFAULT).decodeToString()
        }

        fun notify(topic: String, message: String, title: String, context: Context){
            val notifyCall: Call<MessageModel> = ApiClient.getService().sendNotification(LoggedInUser.getTokenForAuthentication()!!, title, message, topic)

            notifyCall.enqueue(object: Callback<MessageModel>{
                override fun onResponse(
                    call: Call<MessageModel>,
                    response: Response<MessageModel>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}