package com.example.findmyspot.metodospago.model

import androidx.annotation.IntegerRes
import com.example.findmyspot.auth.model.User
import com.example.findmyspot.pagos.model.Pago
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiMetodoPagoService {

    @GET("api/MetodosPago/PagosUsuario/{id}")
    suspend fun getMetodosPagoPorUsuario(@Path("id") id: Int): Response<Array<MetodoPago>>

    @POST("api/MetodosPago/")
    suspend fun nuevoMetodoPago(@Body requestBody: NuevoMetodoPago): Response<Int>

    @DELETE("api/MetodosPago")
    suspend fun borrarMetodoPago(@Query("ID") id: Int): Response<Int>

}