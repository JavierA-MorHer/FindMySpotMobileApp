package com.example.findmyspot.entradas.model

data class Entrada(
    val id_Entrada:String,
    val id_Usuario:String,
    val id_Estacionamiento:String,
    val fechaHora:String,
)

data class NuevaEntrada(
    val id_Usuario:Int =0,
    val id_Estacionamiento:Int=0,
)

data class EntradaReciente(
     var usuarioE          : String,
     var estacionamiento   : String,
     var fecha             : String,
     var capacidad         : Int,
     var idEntrada         : Int,
     var idEstacionamiento : Int,
     var idUsuario         : Int,
     var direccion         : String
     )

