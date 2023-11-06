package com.example.findmyspot.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.findmyspot.R
import com.example.findmyspot.auth.model.ApiAuthService
import com.example.findmyspot.components.MenuLateral
import com.example.findmyspot.estacionamiento.model.ApiEstacionamientoService
import com.example.findmyspot.estacionamiento.model.Estacionamiento
import com.example.findmyspot.helpers.getRetrofitEstacionamientos
import com.example.findmyspot.helpers.isInternetAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReservarLugar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isInternetAvailable(this)) {
            val menu = MenuLateral()
            setContent {
                menu.MenuLateral {
                    ResevarLugar()
                }
            }
        }else{
            val intent = Intent(this, NoInternet::class.java)
            startActivity(intent)
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ResevarLugar() {
        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }
        var estacionamientos by remember { mutableStateOf(emptyList<String>()) }
        var selectedText by remember { mutableStateOf("Selecciona un estacionamiento") }
        var showLoadingDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Selecciona un estacionamiento") },
                text = {  },
                confirmButton = {},
                dismissButton = {
                    Button(
                        onClick = { showErrorDialog = false }
                    ) {
                        Text("Cerrar")
                    }
                }
            )
        }


        if (showLoadingDialog) {
            AlertDialog(
                onDismissRequest = { /* No hacer nada al hacer clic fuera del diálogo */ },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                confirmButton = {},
                title = { Text("Cargando...") },
                text = {
                    CircularProgressIndicator() // Spinner de carga
                }
            )
        }

       LaunchedEffect(Unit) {
           showLoadingDialog = true
           estacionamientos = fillDropDown()
           showLoadingDialog = false
       }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "Reservar un lugar en un estacionamiento.", fontSize = 20.sp, modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
                textAlign = TextAlign.Left,
            )
            Spacer(modifier = Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    estacionamientos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = {
                if (selectedText.contains("Selecciona")){
                    showErrorDialog = true
                }else{
                    resevarLugar(selectedText)
                }
            }) {
                Text(text = "Resevar lugar")
            }
        }
    }

    private fun resevarLugar(selectedText: String) {
        println("Reservando lugar en estacionamiento: $selectedText")
    }


    private suspend fun fillDropDown(): List<String> {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val call = getRetrofitEstacionamientos().create(ApiEstacionamientoService::class.java).getEstacionamientos()
                if (call.code() == 200) {
                    val estacionamientosResponse = call.body()

                    if (estacionamientosResponse != null) {
                        println( estacionamientosResponse.map { it.nombre })
                        return@async estacionamientosResponse.map { it.nombre }
                    }
                } else {
                    // Manejar el error de la solicitud de manera apropiada
                }
                return@async emptyList() // Devolver una lista vacía en caso de error
            } catch (e: Exception) {
                // Manejar la excepción de manera apropiada
                return@async emptyList()
            }
        }.await()
    }
}