package com.example.findmyspot.pagos.model

import androidx.lifecycle.viewmodel.CreationExtras
import com.example.findmyspot.salidas.model.NuevaSalida
import com.example.findmyspot.salidas.model.Salida
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiPagosService {
    @POST("pagos/create")
    suspend fun registrarPago(@Body requestBody: Pago):Response<Pago>

    @GET("pagos/status/{id}")
    suspend fun verificarEstancia(@Path("id") id: Int):Response<Boolean>
}