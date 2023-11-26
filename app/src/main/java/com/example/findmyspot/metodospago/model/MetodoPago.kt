package com.example.findmyspot.metodospago.model

data class MetodoPago(
    val id_MetodoPago: Int,
    val id_Usuario: Int,
    val numero_Tarjeta: String,
    val fecha_Vencimiento: String,
    val cvv: String,
    val propietario: String,
    val saldo: Long
)

data class NuevoMetodoPago(
    val id_Usuario: Int,
    val numero_Tarjeta: String,
    val fecha_Vencimiento: String,
    val cvv: String,
    val propietario: String,
    val saldo: Long
)