package app.quic.mobile.services

import app.quic.mobile.models.LoginModel
import app.quic.mobile.models.TokenModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/Auth/login")
    fun login(@Body model: LoginModel): Call<TokenModel>
}