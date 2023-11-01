package com.example.findmyspot.estacionamiento.model

import retrofit2.Response
import retrofit2.http.GET


interface ApiEstacionamientoService {

    @GET("api/Estacionamientos")
    suspend fun getEstacionamientos(): Response< Array<Estacionamiento>>
}