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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.findmyspot.Data
import com.example.findmyspot.Message
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.findmyspot.components.MenuLateral
import com.example.findmyspot.estacionamiento.model.ApiEstacionamientoService
import com.example.findmyspot.helpers.getRetrofitEstacionamientos
import com.example.findmyspot.helpers.isInternetAvailable
import com.example.findmyspot.ui.theme.FindMySpotTheme
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

var primaryColor = Color(0xFF228B22)
val secondaryColor = Color(0xFFD9D9D9)
val bgColor = Color(0xFFD9D9D9)

class Home : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isInternetAvailable(this)) {
            val menu = MenuLateral()
            setContent{
                menu.MenuLateral { HomeComponent(messages = Data.conversationSample) }
            }
        }else{
            val intent = Intent(this, NoInternet::class.java)
            startActivity(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun HomeComponent(messages:List<Message>){
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val nombre = sharedPreferences.getString("nombre", null)
        val entradaActiva = sharedPreferences.getBoolean("EstanciaActiva", false)

        println("estancia activa $entradaActiva")


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

                Text(text = "Tus visitas mas recientes", fontSize = 20.sp, textAlign = TextAlign.Center,
                )

                LazyColumn {
                    items(messages) { _ ->
                        Card()
                    }
                }
            }

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
    fun Card(){
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
                Text(text = "Estacionamiento Altacia", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Text(text = "Fecha:")
                Text(text = "Duracion:")
                Text(text = "Total:")
                ClickableText(
                    text = AnnotatedString("Ver detalle"),
                    modifier = Modifier.align(End),
                    style = textStyle,
                    onClick = {
                        isDialogOpen = true
                    })
                if (isDialogOpen) {
                    ModalDetail(onDismiss = { isDialogOpen = false })
                }
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
                    Button(onClick = { reservarLugar(context) },
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
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = {  },
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

    //@Preview(showBackground = true)
    @Composable
    fun PreviewHome(){
        FindMySpotTheme {
            HomeComponent(Data.conversationSample)
        }
    }

}

