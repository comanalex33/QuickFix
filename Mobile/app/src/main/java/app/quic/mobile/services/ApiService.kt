package app.quic.mobile.services

import app.quic.mobile.models.BuildingModel
import app.quic.mobile.models.LoginModel
import app.quic.mobile.models.TokenModel
import app.quic.mobile.models.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //Auth
    @POST("api/auth/login")
    fun login(@Body model: LoginModel): Call<TokenModel>

    //User
    @GET("api/users/{username}")
    fun getUserData(@Path("username") username: String): Call<UserModel>
    @POST("api/users/{username}/buildings/{id}")
    fun addBuildingToUser(@Header("Authorization") authHeader : String,
                          @Path("username") username: String,
                          @Path("id") id: Long): Call<UserModel>

    //Building
    @GET("/api/buildings")
    fun getAllBuildings(): Call<List<BuildingModel>>
    @GET("api/buildings/{id}")
    fun getBuildingById(@Path("id") id: Long): Call<BuildingModel>
}