package app.quic.mobile.services

import app.quic.mobile.HelperClass.Companion.decodeBase64
import app.quic.mobile.HelperClass.Companion.deserializeTokenInfo
import app.quic.mobile.models.TokenDataModel

object LoggedInUser {
    var token: String? = null
    var tokenInfo: TokenDataModel? = null
    var username: String? = null

    fun setConnection(tok: String?, user: String?) {
        token = tok
        username = user
        if(token != null) {
            val data = token?.split(".")?.get(1)?.decodeBase64()
            tokenInfo = data!!.deserializeTokenInfo()
        }
    }

    fun getTokenForAuthentication(): String? {
        if(token == null)
            return null
        return "Bearer $token"
    }

    fun getUserRole(): String {
        return tokenInfo!!.roles
    }
}