package app.quic.mobile.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://3.66.157.143/")
                //.baseUrl("https://0.0.0.0:44365/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getService(): ApiService {
            return getRetrofit().create(ApiService::class.java)
        }
    }
}