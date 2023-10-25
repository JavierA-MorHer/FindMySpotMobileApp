package com.example.findmyspot.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAPI {

   fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://findmyspotservices.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}