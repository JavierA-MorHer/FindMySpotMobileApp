package com.example.findmyspot.auth.model

import java.util.UUID

data class User(
    var firstName:String,
    var lastName: String,
    var email:String,
    var password:String,
    var id:UUID = UUID.randomUUID()
)
