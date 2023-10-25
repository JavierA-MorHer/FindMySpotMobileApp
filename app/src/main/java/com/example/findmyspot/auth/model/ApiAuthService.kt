package com.example.findmyspot.auth.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuthService {

     @POST("api/UsuariosLogin")
    suspend fun login(@Body requestBody: User):Response<User>

     @POST("api/Usuarios")
     suspend fun createUser(@Body requestBody: User):Response<User>
}