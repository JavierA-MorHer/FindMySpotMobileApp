package com.example.findmyspot.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findmyspot.components.MenuLateral

class AddCreditCard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menu = MenuLateral()
        setContent{
            menu.MenuLateral { AddCreditCardComponent() }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddCreditCardComponent(){
        var nombre by remember { mutableStateOf("") }
        var numeroTarjeta by remember { mutableStateOf("") }
        var fechaVencimiento by remember { mutableStateOf("") }
        var cvv by remember { mutableStateOf("") }

        // Formatear el número de tarjeta
        val formattedCreditCard = formatCreditCardNumber(numeroTarjeta)


        val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = primaryColor
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Agregar metodo de pago", fontSize = 25.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = {nombre =it},
                colors = textFieldColors,
                label = { Text("Nombre del propietario") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = formattedCreditCard,
                onValueChange = {numeroTarjeta =it.take(20)},
                colors = textFieldColors,
                label = { Text("Numero de tarjeta") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row() {
                OutlinedTextField(
                    value = fechaVencimiento,
                    onValueChange = {
                        fechaVencimiento = formatearFecha(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        cursorColor = Color.Black, // Cambia el color del cursor según tus preferencias
                        focusedBorderColor = Color.Black, // Cambia el color del borde cuando está enfocado
                        unfocusedBorderColor = Color.Gray // Cambia el color del borde cuando no está enfocado
                    ),
                    label = { Text("Fecha de vencimiento") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length <= 3) {
                            cvv = it
                        }},
                    colors = textFieldColors,
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                    ),
                    singleLine = true,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Agregar tarjeta")
            }
        }
    }

    @Composable
    fun formatCreditCardNumber(input: String): String {
        // Eliminar caracteres no numéricos
        val numbersOnly = input.replace(Regex("[^\\d]"), "")

        // Dividir el número en grupos de cuatro dígitos
        val groupedNumbers = numbersOnly.chunked(4)

        // Unir los grupos con espacios
        return groupedNumbers.joinToString("-") { it }
    }

    fun formatearFecha(inputText: String): String {
        // Eliminar caracteres no numéricos
        val numeros = inputText.replace(Regex("[^\\d]"), "")

        // Obtener los primeros dos caracteres (mes) y los siguientes dos caracteres (año)
        val mes = numeros.substring(0, minOf(numeros.length, 2))
        val año = numeros.substring(minOf(numeros.length, 2), minOf(numeros.length, 4))

        // Formatear la fecha como MM/YY
        return "$mes/$año"
    }


}