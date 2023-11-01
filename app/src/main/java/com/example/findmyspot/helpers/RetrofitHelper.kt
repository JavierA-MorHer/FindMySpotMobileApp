package com.example.findmyspot.helpers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

 fun getRetrofitUsuarios(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://findmyspotservices.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getRetrofitEstacionamientos(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://findmyspotserviceslugares-production.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}