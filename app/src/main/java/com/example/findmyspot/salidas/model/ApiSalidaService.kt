package com.example.findmyspot.salidas.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiSalidaService {
    @POST("api/Salidas")
    suspend fun registrarSalida(@Body requestBody: NuevaSalida): Response<Salida>
}