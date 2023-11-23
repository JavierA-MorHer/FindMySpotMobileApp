package com.example.findmyspot.entradas.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

import retrofit2.http.POST
import retrofit2.http.Path

interface ApiEntradaService {
        @POST("api/Entradas")
        suspend fun registrarEntrada(@Body requestBody: NuevaEntrada): Response<Entrada>

        @GET("api/UsuariosEntradas/{id}")
        suspend fun ultimasEntradas(@Path("id") id: Int): Response<Array<EntradaReciente>>
}