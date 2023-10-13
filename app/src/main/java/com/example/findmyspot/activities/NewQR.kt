package com.example.findmyspot.activities


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix


class NewQR : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{Title()}

    }

    @Preview(showBackground = true)
    @Composable
    fun Title() {

        var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

        // Text para generar el código QR
        val qrCodeText = "https://www.youtube.com"

        // Tamaño del código QR
        val qrCodeSize = 400

        // Generar el código QR
        LaunchedEffect(qrCodeText) {
            val bitMatrix: BitMatrix = generarQR(qrCodeText, qrCodeSize, qrCodeSize)
            val bitmap = bitMatrix.toBitmap()
            qrCodeBitmap = bitmap.asImageBitmap()
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "FindMySpot", fontSize = 40.sp,
                modifier =
                Modifier.fillMaxWidth()
                    .padding(20.dp)
                    .wrapContentHeight(align = Alignment.Top),
                textAlign = TextAlign.Center,
            )

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

            // Mostrar el código QR
            qrCodeBitmap?.let { qrCode ->
                Image(
                    bitmap = qrCode,
                    contentDescription = "Código QR",
                    modifier = Modifier.size(qrCodeSize.dp)
                )
            }

        }

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

