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

fun getRetrofitEntradas(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://findmyspotservicesentradas.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getRetrofitSalidas(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://findmyspotsalidas-production.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun getRetrofitPagos(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://findmyspotspringboot-production.up.railway.app/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}