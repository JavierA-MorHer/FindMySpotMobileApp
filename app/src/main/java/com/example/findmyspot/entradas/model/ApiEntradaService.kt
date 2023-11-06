package com.example.findmyspot.entradas.model

import com.example.findmyspot.entradas.Entrada
import com.example.findmyspot.entradas.NuevaEntrada
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.POST

interface ApiEntradaService {
        @POST("api/Entradas")
        suspend fun registrarEntrada(@Body requestBody: NuevaEntrada): Response<Entrada>
}