package app.quic.mobile

import android.util.Base64
import app.quic.mobile.models.TokenDataModel
import com.google.gson.Gson

class HelperClass {
    companion object {
        fun String.deserializeTokenInfo(): TokenDataModel {
            val gson = Gson()
            return gson.fromJson(this, TokenDataModel::class.java)
        }
        fun String.decodeBase64(): String {
            return Base64.decode(this, Base64.DEFAULT).decodeToString()
        }
    }
}