package com.example.findmyspot.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findmyspot.Data
import com.example.findmyspot.Message
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.findmyspot.ui.theme.FindMySpotTheme

var primaryColor = Color(0xFF228B22)
val secondaryColor = Color(0xFF68A767)
val bgColor = Color(0xFFD9D9D9)

class Home : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            HomeComponent(Data.conversationSample)
             }
    }

    @Composable
    fun HomeComponent(messages:List<Message>){
        val context = LocalContext.current
        var isVisible by remember { mutableStateOf(true) }
        isVisible = false

        Column( modifier = Modifier.fillMaxSize()) {
            Text(text = "FindMySpot", fontSize = 40.sp, modifier =
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .wrapContentHeight(align = Alignment.Top),
                textAlign = TextAlign.Center,
            )

            Column(modifier = Modifier
                .padding(16.dp)
            ) {

                Text(text = "Bienvenido Paco", fontSize = 20.sp, modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.Top),
                    textAlign = TextAlign.Left,
                )
                when(isVisible){
                     false -> BotonArea(context)
                    else -> {
                        EstanciaEnCurso()
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
    @Composable
    fun EstanciaEnCurso() {
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
                Text(text = "Estacionamiento: ",fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Hora de entrada: ",fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Tiempo acumulado: ",fontSize = 15.sp)
                Spacer(modifier = Modifier.height(25.dp))
                Text(text = "Total al momento: ",modifier = Modifier.fillMaxWidth(),textAlign = TextAlign.Center,fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {  },
                    colors = ButtonDefaults.buttonColors(primaryColor),
                    modifier = Modifier.align(CenterHorizontally)
                                        .fillMaxWidth(),
                ) {
                    Text(text = "Ver QR", fontSize = 18.sp, modifier =
                    Modifier
                        .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
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
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.height(25.dp))
                Button(onClick = { entrarAlEstacionamiento(context) },
                    colors = ButtonDefaults.buttonColors(primaryColor),
                    modifier = Modifier.align(CenterHorizontally).height(55.dp),
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
                        modifier = Modifier.padding(10.dp).height(55.dp)

                    ) {
                        Text(text = "Reservar un lugar", fontSize = 15.sp, modifier =
                        Modifier
                            .wrapContentHeight(align = Alignment.Top),
                            textAlign = TextAlign.Center,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(secondaryColor),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(text = "Ver metodos de pago", fontSize = 15.sp, modifier =
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

//    @Preview(showBackground = true)
    @Composable
    fun PreviewHome(){
        FindMySpotTheme {
            HomeComponent(Data.conversationSample)
        }
    }

}

