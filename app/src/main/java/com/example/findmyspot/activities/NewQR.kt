package com.example.findmyspot.activities


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findmyspot.components.MenuLateral
import com.example.findmyspot.entradas.model.NuevaEntrada
import com.example.findmyspot.entradas.model.ApiEntradaService
import com.example.findmyspot.entradas.model.Entrada
import com.example.findmyspot.helpers.getRetrofitEntradas
import com.example.findmyspot.helpers.isInternetAvailable
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date


class NewQR : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isInternetAvailable(this)) {
            val menu = MenuLateral()
            setContent{
                menu.MenuLateral { GenerateNewQR() }
            }
        }else{
            val intent = Intent(this, NoInternet::class.java)
            startActivity(intent)
        }


    }

    @Preview(showBackground = true)
    @Composable
    fun GenerateNewQR() {

        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("id", 0)
        val context = LocalContext.current

        var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
        val qrCodeText = idUsuario.toString()
        val qrCodeSize = 400

        val textStyle = TextStyle(
            color = white,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        // Generar el código QR
        LaunchedEffect(qrCodeText) {
            val bitMatrix: BitMatrix = generarQR(qrCodeText, qrCodeSize, qrCodeSize)
            val bitmap = bitMatrix.toBitmap()
            qrCodeBitmap = bitmap.asImageBitmap()
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(text = "Entrar al estacionamiento", fontSize = 20.sp, modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
                textAlign = TextAlign.Left,
            )
            Text(text = "Muestra este código QR al entrar al estacionamiento", fontSize = 15.sp, modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
                textAlign = TextAlign.Left,
            )

            Spacer(modifier = Modifier.width(50.dp))

            qrCodeBitmap?.let { qrCode ->
                Image(
                    bitmap = qrCode,
                    contentDescription = "Código QR",
                    modifier = Modifier.size(qrCodeSize.dp)
                )
            }

            ClickableText(
                text = AnnotatedString("ENTRAR"),
                style = textStyle,
                onClick = {
                    registrarEntrada(context)
                })
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun obtenerFechaEnFormatoDeseado(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS")
        val fecha = Date()

        return formato.format(fecha)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun registrarEntrada(context:Context) {

        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("id", 0)
        val date = obtenerFechaEnFormatoDeseado()
        val nuevaEntrada = NuevaEntrada(idUsuario,3)

        println(nuevaEntrada)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofitEntradas().create(ApiEntradaService::class.java).registrarEntrada(nuevaEntrada)
              println(call.body())
                if(call.isSuccessful) {
                    println(call.code())

                    val entrada = call.body()

                    if (entrada != null) {
                        setEntradaActiva(entrada.id_Estacionamiento.toInt(),entrada.id_Entrada.toInt())
                        val intent = Intent(context, Home::class.java)
                        context.startActivity(intent)
                    }else{

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEntradaActiva(idEstacionamiento:Int,idEntrada: Int) {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("EstanciaActiva", true)
        editor.putString("HoraInicio",obtenerHoraYMinutos())
        editor.putInt("idEstacionamiento", idEstacionamiento)
        editor.putInt("idEntrada", idEntrada)


        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerHoraYMinutos(): String {
        val horaActual = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return horaActual.format(formatter)
    }

    private fun BitMatrix.toBitmap(): Bitmap {
        val width = this.width
        val height = this.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (this[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }


    private fun generarQR(text: String, width: Int, height: Int): BitMatrix {
        val multiFormatWriter = MultiFormatWriter()
        return multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
    }


}

