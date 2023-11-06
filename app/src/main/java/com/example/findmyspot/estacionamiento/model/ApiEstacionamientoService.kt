package com.example.findmyspot.estacionamiento.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiEstacionamientoService {

    @GET("api/Estacionamientos")
    suspend fun getEstacionamientos(): Response< Array<Estacionamiento>>

    @GET("api/Estacionamientos/{id}")
    suspend fun getEstacionamientoById(@Path("id") id: Int): Response<Estacionamiento>
}