package app.quic.mobile.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://18.196.144.212/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getService(): ApiService {
            return getRetrofit().create(ApiService::class.java)
        }
    }
}