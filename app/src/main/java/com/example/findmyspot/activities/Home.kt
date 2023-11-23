package com.example.findmyspot.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.findmyspot.components.MenuLateral
import com.example.findmyspot.entradas.model.EntradaReciente
import com.example.findmyspot.entradas.model.ApiEntradaService
import com.example.findmyspot.estacionamiento.model.ApiEstacionamientoService
import com.example.findmyspot.helpers.getRetrofitEntradas
import com.example.findmyspot.helpers.getRetrofitEstacionamientos
import com.example.findmyspot.helpers.isInternetAvailable
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.findmyspot.R
import com.example.findmyspot.helpers.getRetrofitPagos
import com.example.findmyspot.helpers.getRetrofitSalidas
import com.example.findmyspot.pagos.model.ApiPagosService
import com.example.findmyspot.pagos.model.Pago
import com.example.findmyspot.salidas.model.ApiSalidaService
import com.example.findmyspot.salidas.model.NuevaSalida

var primaryColor = Color(0xFF228B22)
val secondaryColor = Color(0xFFD9D9D9)
val white = Color(0xFFFFFFFF)
val bgColor = Color(0xFFD9D9D9)
lateinit var tiempoTotal:BigDecimal

class Home : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tiempoTotal = BigDecimal.ZERO

        if (isInternetAvailable(this)) {
            val menu = MenuLateral()
            setContent{
                menu.MenuLateral { HomeComponent() }
            }
        }else{
            val intent = Intent(this, NoInternet::class.java)
            startActivity(intent)
        }

    }

    private suspend fun getListaRecientes(): Array<EntradaReciente> {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("id", 0)

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val call = getRetrofitEntradas().create(ApiEntradaService::class.java).ultimasEntradas(idUsuario)
                if (call.isSuccessful) {
                    val estacionamientoResponse: Array<EntradaReciente>? = call.body()

                    if (estacionamientoResponse != null) {
                        return@async estacionamientoResponse // Devolver la respuesta del servicio
                    }
                } else {
                    println("ERROR")
                }
                return@async emptyArray<EntradaReciente>()
            } catch (e: Exception) {
                println("Error ${e.message}")
                return@async emptyArray<EntradaReciente>()
            }
        }.await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun HomeComponent(){
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val nombre = sharedPreferences.getString("nombre", null)



        val entradaActiva = sharedPreferences.getBoolean("EstanciaActiva", false)

        var visitasRecientes by remember { mutableStateOf(emptyArray<EntradaReciente>()) }

        LaunchedEffect(Unit) {
            visitasRecientes = getListaRecientes()


        }


        val context = LocalContext.current
        var isVisible by remember { mutableStateOf(false) }

        if(entradaActiva){
            isVisible = true
        }

        Column( modifier = Modifier.fillMaxSize()) {
            Column(
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                Text(text = "Bienvenido $nombre", fontSize = 20.sp, modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top),
                    textAlign = TextAlign.Center,
                )
                when(isVisible){
                     false -> BotonArea(context)
                    else -> {
                        EstanciaEnCurso(0.44.toBigDecimal())
                    }
                }


                if(visitasRecientes.isNotEmpty()){
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = "Tus visitas más recientes", fontSize = 20.sp, textAlign = TextAlign.Center)
                    LazyColumn {
                        items(visitasRecientes) { entrada ->
                            Card(entrada)
                        }
                    }
                }else{

                    LottieEmpty( )
                }

            }

        }

    }

    private suspend fun getStatusEstancia(): Boolean {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idEstacionamiento = sharedPreferences.getInt("idEstacionamiento", 0)

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val call = getRetrofitPagos().create(ApiPagosService::class.java).verificarEstancia(idEstacionamiento)
                if (call.code() == 200) {
                    val estacionamientoResponse = call.body()

                    if (estacionamientoResponse != null) {
                        println( estacionamientoResponse)
                        return@async true
                    }
                } else {
                    println("ERROR")
                }
                return@async false
            } catch (e: Exception) {
                return@async false
            }
        }.await()
    }

    @Composable
    fun LottieEmpty() {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No hay visitas recientes",color = Color.Gray , fontSize = 17.sp, textAlign = TextAlign.Center,)

            LottieAnimation(
                modifier = Modifier.height(350.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun EstanciaEnCurso(tarifaPorMinuto:BigDecimal) {
        var estacionamientoActual by remember{ mutableStateOf("Cargando...") }
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val horaEntrada = sharedPreferences.getString("HoraInicio", null)

        var tiempoAcumulado by remember { mutableStateOf(Duration.ZERO) }
        var totalAcumulado by remember { mutableStateOf(BigDecimal.ZERO) }
        val minutosTarifaMinima = 30

        var isDialogOpen by remember { mutableStateOf(false) }


        val horaInicio = runCatching {
            LocalTime.parse(horaEntrada)
        }.getOrNull()

        val scope = rememberCoroutineScope()

        if (horaInicio != null) {
            LaunchedEffect(true) {
                scope.launch(Dispatchers.IO) {
                    while (true) {
                        val horaActual = LocalTime.now()
                        tiempoAcumulado = Duration.between(horaInicio, horaActual)
                        val minutos = tiempoAcumulado.toMinutes()
                        if (tiempoAcumulado.toMinutes() <= minutosTarifaMinima) {
                            totalAcumulado = tarifaPorMinuto
                        } else {
                            // Después de 30 minutos, calculamos la tarifa adicional
                            val minutosAdicionales = tiempoAcumulado.toMinutes() - minutosTarifaMinima
                            val tarifaAdicional = tarifaPorMinuto * BigDecimal(minutosAdicionales)
                            totalAcumulado = tarifaPorMinuto + tarifaAdicional
                            tiempoTotal = totalAcumulado
                        }
                        delay(1000)
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            estacionamientoActual = getEstacionamiento() as String
        }

        Box(
            modifier = Modifier
                .padding(all = 16.dp)
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(10.dp)
                )
        ){
            Column(
                modifier = Modifier
                    .padding(30.dp)
            ) {
                Text(text = "Detalle de su estancia",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(40.dp))
                Row() {
                    Text(text = "Estacionamiento: ",fontSize = 15.sp)
                    Text(text = estacionamientoActual,fontSize = 15.sp)

                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(){
                    Text(text = "Hora de entrada: ",fontSize = 15.sp)
                    if (horaEntrada != null) {
                        Text(text = horaEntrada,fontSize = 15.sp)
                    }

                }
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    Text(text = "Tiempo acumulado: ",fontSize = 15.sp)
                    Text("${tiempoAcumulado.toHours()}:${tiempoAcumulado.toMinutesPart()}:${tiempoAcumulado.toSecondsPart()}")
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row() {
                    Text(text = "Total al momento: ",fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$$totalAcumulado ",fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    isDialogOpen = true
                },
                    colors = ButtonDefaults.buttonColors(primaryColor),
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Pagar salida", fontSize = 18.sp, modifier =
                    Modifier
                        .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Center,
                    )
                }

            }
        }
        if (isDialogOpen) {
            ModalQR(onDismiss = { isDialogOpen = false })
        }
    }

    private suspend fun getEstacionamiento(): Any {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idEstacionamiento = sharedPreferences.getInt("idEstacionamiento", 0)

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val call = getRetrofitEstacionamientos().create(ApiEstacionamientoService::class.java).getEstacionamientoById(idEstacionamiento)
                if (call.code() == 200) {
                    val estacionamientoResponse = call.body()

                    if (estacionamientoResponse != null) {
                        println( estacionamientoResponse)
                        return@async estacionamientoResponse.nombre
                    }
                } else {
                   println("ERROR")
                }
                return@async ""
            } catch (e: Exception) {
                return@async ""
            }
        }.await()
    }

    private fun entrarAlEstacionamiento(context: Context) {
        val intent = Intent(context, NewQR::class.java)
        context.startActivity(intent)
    }
    private fun reservarLugar(context: Context) {
        val intent = Intent(context, ReservarLugar::class.java)
        context.startActivity(intent)
    }
    @Composable
    fun Card(entrada: EntradaReciente){
        var isDialogOpen by remember { mutableStateOf(false) }

        val textStyle = TextStyle(
            color = primaryColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(10.dp)
                )

        ) {

            Column(modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(), ) {
                Text(text = entrada.estacionamiento,
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Bold
                    ), fontSize = 20.sp,textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Dirección: ${entrada.direccion}",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Fecha: ${entrada.fecha.take(10)}")
            }

        }

    }

    @Composable
    fun BotonArea(context:Context){

        Box{
            Column {
                Text(text = "¿Qué quieres hacer el día de hoy?", fontSize = 15.sp, modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(25.dp))
                Button(onClick = { entrarAlEstacionamiento(context) },
                    colors = ButtonDefaults.buttonColors(primaryColor),
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .height(55.dp),
                ) {
                    Text(text = "Entrar al estacionamiento", fontSize = 18.sp, modifier =
                    Modifier
                        .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Center,
                    )
                }
                Row(modifier = Modifier.align(CenterHorizontally)) {
                    /*Button(onClick = { reservarLugar(context) },
                        colors = ButtonDefaults.buttonColors(secondaryColor),
                        modifier = Modifier
                            .padding(10.dp)
                            .height(55.dp)

                    ) {
                        Text(text = "Reservar un lugar", fontSize = 15.sp, color = Color.Black, modifier =
                        Modifier
                            .wrapContentHeight(align = Alignment.Top),
                            textAlign = TextAlign.Center,
                        )
                    }*/

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = { openMetodosPago(context) },
                        colors = ButtonDefaults.buttonColors(secondaryColor),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(text = "Ver metodos de pago", fontSize = 15.sp,color = Color.Black, modifier =
                        Modifier
                            .wrapContentHeight(align = Alignment.Top),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

        }
    }

    private fun openMetodosPago(context: Context) {
        val intent = Intent(context, MetodosPago::class.java)
        context.startActivity(intent)
    }

    @Composable
    private fun ModalDetail(onDismiss: () -> Unit) {

        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnClickOutside = true),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text("Este es el contenido del modal.")
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(primaryColor),
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .align(End),
                        ) {
                            Text("Cerrar")
                        }
                    }
                }
            }

        }
    }

    @Composable
    private fun ModalQR(onDismiss: () -> Unit) {
        var selectedListItem by remember { mutableStateOf<ListItem?>(null) }
        var isButtonEnabled by remember { mutableStateOf(false) }
        var isDialogOpen by remember { mutableStateOf(false) }


        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnClickOutside = true),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = "Selecciona un método de pago para continuar")
                        
                        SelectableList(
                            items = listOf(
                                ListItem(id = 1, text = "Tarjeta Terminacion 6789"),
                                // Add more items as needed
                            )
                        ) { selectedItem ->
                            // Handle item selection here
                            selectedListItem = selectedItem
                            isButtonEnabled = true
                        }
                        Row( ) {
                            Button(
                                onClick = { onDismiss() },
                                colors = ButtonDefaults.buttonColors(primaryColor),
                                modifier = Modifier
                                    .padding(top = 16.dp)
                            ) {
                                Text("Cerrar")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = { isDialogOpen = true },
                                colors = ButtonDefaults.buttonColors(primaryColor),
                                enabled = isButtonEnabled,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                            ) {
                                Text("Pagar")
                            }
                        }
                    }
                }
            }

        }
        if (isDialogOpen) {
            ModalQRSalida(onDismiss = { isDialogOpen = false },selectedListItem?.id)
        }
    }

    @Composable
    private fun ModalQRSalida(onDismiss: () -> Unit, idMetodoPago: Int?) {

        var status by remember{ mutableStateOf("PENDING") }
        var textPayment by remember{ mutableStateOf("Procesando pago...")}

        LaunchedEffect(Unit) {
            status = realizarPago(idMetodoPago)
            textPayment = "Pago realizado con éxito, muestra este QR al salir."
        }

        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnClickOutside = true),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        when(status){
                            "PENDING"->{ Text(text = textPayment) }
                            "SUCCESS" ->{
                                Text(text = textPayment)
                                GenerarQR()

                                Row( ) {
                                    Button(
                                        onClick = {  recargarActividad() },
                                        colors = ButtonDefaults.buttonColors(primaryColor),
                                        modifier = Modifier
                                            .padding(top = 16.dp)
                                    ) {
                                        Text("Cerrar")
                                    }
                                }
                            }
                            "ERROR"->{
                                textPayment = "Hubo un error al realizar el pago, inténtanlo de nuevo."
                                Text(text = textPayment)

                                Row( ) {
                                    Button(
                                        onClick = { onDismiss() },
                                        colors = ButtonDefaults.buttonColors(primaryColor),
                                        modifier = Modifier
                                            .padding(top = 16.dp)
                                    ) {
                                        Text("Cerrar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    @Composable
    private fun GenerarQR() {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("id", 0)

        var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        val qrCodeText = idUsuario.toString()
        val qrCodeSize = 400

        val bitMatrix: BitMatrix = generarQR(qrCodeText, qrCodeSize, qrCodeSize)
        val bitmap = bitMatrix.toBitmap()
        qrCodeBitmap = bitmap.asImageBitmap()

        qrCodeBitmap?.let { qrCode ->
            Image(
                bitmap = qrCode,
                contentDescription = "Código QR",
                modifier = Modifier.size(qrCodeSize.dp)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun realizarPago(idMetodoPago: Int?):String {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idEntrada = sharedPreferences.getInt("idEntrada", 0)
        val idUsuario = sharedPreferences.getInt("id", 0)
        val nuevoPago = Pago(idUsuario,idEntrada,tiempoTotal.toString())

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val call = getRetrofitPagos().create(ApiPagosService::class.java).registrarPago(nuevoPago)
                if (call.isSuccessful) {

                    val editor = sharedPreferences.edit()

                    editor.putBoolean("EstanciaActiva", false)
                    editor.putString("HoraFin",obtenerHoraYMinutos())
                    editor.apply()

                    registrarSalida(idEntrada)

                    return@async "SUCCESS"
                } else {
                    // La solicitud no fue exitosa. Puedes manejar el error aquí.
                    val errorBody = call.errorBody()
                    if (errorBody != null) {
                        val errorMessage = errorBody.string()
                        println("Error en la solicitud: $errorMessage")
                    } else {
                        println("Error en la solicitud, pero no se pudo obtener el mensaje de error. PAGOS ${call.code()}")
                    }
                }
                return@async "ERROR"
            } catch (e: Exception) {
                return@async "ERROR"
            }
        }.await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registrarSalida(id: Int) {

        val nuevaSalida = NuevaSalida(id)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofitSalidas().create(ApiSalidaService::class.java).registrarSalida(nuevaSalida)

                if(call.isSuccessful) {
                    println(call.code())

                    val salida = call.body()

                    if (salida != null) {
                        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()

                        editor.putBoolean("EstanciaActiva", false)
                        editor.putString("HoraFin",obtenerHoraYMinutos())
                        editor.apply()
                    }
                }else{
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

    }

    private fun recargarActividad() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerHoraYMinutos(): String {
        val horaActual = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return horaActual.format(formatter)
    }

    @Composable
    private fun CardCredit() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "img")

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Terminada en 6789")
                Text(text = "Banco")
                Text(text = "Vencimiento: 11/24")
            }

            Text(text = "Eliminar")
        }

    }

    data class ListItem(val id: Int, val text: String)
    @Composable
    fun SelectableList(items: List<ListItem>, onItemSelected: (ListItem) -> Unit) {
        var selectedItem by remember { mutableStateOf<ListItem?>(null) }

        LazyColumn {
            items(items) { item ->
                ListItem(
                    item = item,
                    isSelected = selectedItem == item,
                    onItemSelected = {
                        selectedItem = item
                        onItemSelected(item)
                    }
                )
            }
        }
    }

    @Composable
    fun ListItem(item: ListItem, isSelected: Boolean, onItemSelected: () -> Unit) {
        val background = if (isSelected) Color.Gray else Color.Transparent

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemSelected() }
                .background(background)
                .padding(16.dp)
        ) {
            Text(text = item.text)
        }
    }

    private fun BitMatrix.toBitmap(): Bitmap {
        val width = this.width
        val height = this.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (this[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }

        return bitmap
    }


    private fun generarQR(text: String, width: Int, height: Int): BitMatrix {
        val multiFormatWriter = MultiFormatWriter()
        return multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
    }
}

