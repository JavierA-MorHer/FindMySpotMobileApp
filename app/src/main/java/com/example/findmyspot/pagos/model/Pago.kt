package com.example.findmyspot.pagos.model

import java.text.SimpleDateFormat
import java.util.Date

val fechaActual = Date()
val formato = SimpleDateFormat("yy/MM/dd")
val fechaFormateada: String = formato.format(fechaActual)

data class Pago(
    val id_Usuario:Int,
    val id_Entrada:Int,
    val total:String,
    val fechaPago:String = fechaFormateada,

)
