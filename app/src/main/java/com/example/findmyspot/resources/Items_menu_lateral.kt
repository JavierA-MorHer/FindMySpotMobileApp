package com.example.findmyspot.resources

import com.example.findmyspot.R
import com.example.findmyspot.activities.Home

sealed class Items_menu_lateral(
    val icon: Int,
    val title: String,
    val ruta: Class<Home>
){
    object  Item_menu_lateral:Items_menu_lateral(
        R.drawable.ic_launcher_foreground,
        "Consulta de saldos",
        Home::class.java
    )
}
