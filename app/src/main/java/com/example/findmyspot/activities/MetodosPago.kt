package com.example.findmyspot.activities

import android.content.Context
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findmyspot.Data
import com.example.findmyspot.components.MenuLateral
import com.example.findmyspot.estacionamiento.model.ApiEstacionamientoService
import com.example.findmyspot.helpers.getRetrofitEstacionamientos
import com.example.findmyspot.helpers.getRetrofitUsuarios
import com.example.findmyspot.helpers.isInternetAvailable
import com.example.findmyspot.metodospago.model.ApiMetodoPagoService
import com.example.findmyspot.metodospago.model.MetodoPago
import com.example.findmyspot.ui.icons.CustomIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

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
        var metodosDePagoList by remember { mutableStateOf(emptyArray<MetodoPago>()) }
        var isCharging by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            metodosDePagoList = getMetodosDePagoByUser()
            isCharging = false
        }


        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(80.dp))

            if(isCharging){
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Cargando...", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }

            if(metodosDePagoList.isEmpty() && !isCharging){
                Text(text = "No tienes metodo de pago registrados", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
            LazyColumn {
                items(metodosDePagoList) { metodoPago ->
                    MetodosPagoCard(metodoPago)
                }

                item{
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = { openAddCreditCard() }) {
                        Text(text = "Agregar tarjeta")
                    }
                }
            }

        }

    }

    private suspend fun getMetodosDePagoByUser(): Array<MetodoPago> {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("id", 0)

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val call = getRetrofitUsuarios().create(ApiMetodoPagoService::class.java).getMetodosPagoPorUsuario(idUsuario)
                if (call.isSuccessful) {
                    val metodosPagoList: Array<MetodoPago>? = call.body()

                    if (metodosPagoList != null) {
                        return@async metodosPagoList
                    }
                } else {
                    // Manejar el error de la solicitud de manera apropiada
                }
                return@async emptyArray<MetodoPago>() // Devolver una lista vacía en caso de error
            } catch (e: Exception) {
                // Manejar la excepción de manera apropiada
                return@async emptyArray<MetodoPago>()
            }
        }.await()
    }

    private fun openAddCreditCard() {
        val intent = Intent(this, AddCreditCard::class.java)
        startActivity(intent)
    }

    @Composable
    private fun MetodosPagoCard(metodoPago: MetodoPago) {
        val context = LocalContext.current

        var icons= CustomIcons()
        val textStyle = TextStyle(
            color = Color.Red,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
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
                Icon(imageVector = icons.CreditCardIcon(), contentDescription = "Metodos de pago")


                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Terminada en ${metodoPago.numero_Tarjeta.takeLast(4)}")
                    Text(text = "Vencimiento: ${metodoPago.fecha_Vencimiento}")
                }
                Box(){
                    Column() {
                        ClickableText(
                            text = AnnotatedString("Eliminar"),
                            modifier = Modifier.align(Alignment.End),
                            style = textStyle,
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val call : Response<Int> = getRetrofitUsuarios().create(ApiMetodoPagoService::class.java).borrarMetodoPago(id=metodoPago.id_MetodoPago)
                                        if(call.isSuccessful){
                                            val intent = Intent(context, MetodosPago::class.java)
                                            context.startActivity(intent)
                                            finish()

                                        } else{
                                            // La solicitud no fue exitosa. Puedes manejar el error aquí.
                                            val errorBody = call.errorBody()
                                            if (errorBody != null) {
                                                val errorMessage = errorBody.string()
                                                println("Error en la solicitud: $errorMessage")
                                            } else {
                                                println("Error en la solicitud, pero no se pudo obtener el mensaje de error.")
                                            }
                                        }
                                    }catch (e:Exception){
                                        println("Error: ${e.message}")
                                    }
                                }
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