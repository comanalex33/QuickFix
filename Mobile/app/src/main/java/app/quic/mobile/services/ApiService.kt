package app.quic.mobile.services

import app.quic.mobile.models.LoginModel
import app.quic.mobile.models.RegisterModel
import app.quic.mobile.models.TokenModel
import app.quic.mobile.models.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/login")
    fun login(@Body model: LoginModel): Call<TokenModel>
    @GET("api/users/{username}")
    fun getUserData(@Path("username") username: String): Call<UserModel>
    @POST("api/auth/register")
    fun register(@Body model: RegisterModel): Call<UserModel>
    @GET("api/users/{username}")
    fun isUser(@Body username: String): Call<UserModel>
}