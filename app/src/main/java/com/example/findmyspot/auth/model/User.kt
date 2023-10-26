package com.example.findmyspot.auth.model


data class User(
    var nombre:String = "",
    var apellidoPaterno: String ="",
    var apellidoMaterno:String="",
    var email:String="",
    var telefono:String="",
    var password:String="",
    var id_Usuario:String = ""

)

data class NewUser(
    var nombre:String = "",
    var apellidoPaterno: String ="",
    var apellidoMaterno:String="",
    var email:String="",
    var telefono:String="",
    var password:String="",
)

fun NewUser.isValid(): Boolean {
    return nombre.isNotBlank() &&
            apellidoPaterno.isNotBlank() &&
            apellidoMaterno.isNotBlank() &&
            email.isNotBlank() &&
            telefono.isNotBlank() &&
            password.isNotBlank()
}
