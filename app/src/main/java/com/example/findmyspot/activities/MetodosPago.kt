package com.example.findmyspot.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findmyspot.Data
import com.example.findmyspot.components.MenuLateral
import com.example.findmyspot.helpers.isInternetAvailable

class MetodosPago: ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menu = MenuLateral()
        setContent{
            menu.MenuLateral { MetodosDePago() }
        }
    }

    @Composable
    fun MetodosDePago(){
        /*LazyColumn {
            items(items) { item ->
               MetodosPagoCard()
            }
        }*/
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MetodosPagoCard()
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { openAddCreditCard() }) {
                Text(text = "Agregar tarjeta")
            }
        }

    }

    private fun openAddCreditCard() {
        val intent = Intent(this, AddCreditCard::class.java)
        startActivity(intent)
    }

    @Composable
    private fun MetodosPagoCard() {
        val textStyle = TextStyle(
            color = Color.Red,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(50.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(25.dp)
            ) {
                Text(text = "img")

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Terminada en 6789")
                    Text(text = "Banco")
                    Text(text = "Vencimiento: 11/24")
                }
                Box(){
                    Column() {
                        ClickableText(
                            text = AnnotatedString("Eliminar"),
                            modifier = Modifier.align(Alignment.End),
                            style = textStyle,
                            onClick = {
                               eliminarTarjeta()
                            })
                    }

                }

            }
        }

    }

    private fun eliminarTarjeta() {
        println("Eliminando tarjeta")
    }
}