package sleman.alzoubi.josequaltask.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiMap {
    companion object {
       const val BASE_URL = "https://josequal.net"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}