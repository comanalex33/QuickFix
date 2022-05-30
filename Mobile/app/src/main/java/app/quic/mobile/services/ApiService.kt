package app.quic.mobile.services

import app.quic.mobile.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //Auth
    @POST("api/auth/login")
    fun login(@Body model: LoginModel): Call<TokenModel>
    @POST("api/auth/register")
    fun register(@Body model: RegisterModel): Call<UserModel>

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

    //Category
    @GET("/api/category")
    fun getCategories(): Call<List<CategoryModel>>
    @GET("/api/category/{id}")
    fun getCategoryById(@Path("id") id: Long): Call<CategoryModel>

    //Request
    @POST("/api/requests")
    fun sendRequest(@Header("Authorization") authHeader:String, @Body model: RequestModel): Call<RequestModel>
    @GET("/api/requests")
    fun getAllRequests(): Call<List<RequestModel>>
}