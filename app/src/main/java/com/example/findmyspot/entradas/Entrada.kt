package com.example.findmyspot.entradas

data class Entrada(
    val idEntrada:String,
    val idUsuario:String,
    val idEstacionamiento:String,
    val fechaHora:String,
)

data class NuevaEntrada(
    val idUsuario:Int =0,
    val idEstacionamiento:Int=0,
    val fechaHora:String="",
)
