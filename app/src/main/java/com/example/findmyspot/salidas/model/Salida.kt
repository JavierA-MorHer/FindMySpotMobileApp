package com.example.findmyspot.salidas.model

data class Salida(
    val idSalida:Int,
    val idEntrada:Int,
    val fechaHora:String,
)

data class NuevaSalida(
    val idEntrada:Int
)