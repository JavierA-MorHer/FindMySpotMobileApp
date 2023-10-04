package com.example.findmyspot.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.findmyspot.ui.theme.FindMySpotTheme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginComponent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComponent() {
    val primaryColor = Color(0xFF228B22) // Puedes definir el color aquí o usar uno de los colores personalizados definidos anteriormente
    var emailInputValue by remember { mutableStateOf("") }
    var passwordInputValue by remember { mutableStateOf("") }
    val context = LocalContext.current

    val textStyle = TextStyle(
        color = primaryColor,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
    )

    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = primaryColor,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = primaryColor
    )

    Text(text = "FindMySpot", fontSize = 40.sp, modifier =
    Modifier.fillMaxWidth()
        .padding(20.dp)
        .wrapContentHeight(align = Alignment.Top),
        textAlign = TextAlign.Center,
    )


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Text(text = "Iniciar sesión", fontSize = 25.sp, color = primaryColor)
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = emailInputValue,
            onValueChange = {emailInputValue =it},
            colors = textFieldColors,
            label = { Text("Correo electrónico") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = passwordInputValue,
            onValueChange = {passwordInputValue = it},
            label = { Text("Contraseña") },
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { logIn(emailInputValue, passwordInputValue) },
            modifier = Modifier.align(CenterHorizontally),
            colors = ButtonDefaults.buttonColors(primaryColor)
            ) {
            Text(text = "Iniciar sesión")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            Text(text = "¿Aún no estás registrado?")
            Spacer(modifier = Modifier.width(5.dp))

            ClickableText(
                text = AnnotatedString("Crea una cuenta"),
                style = textStyle,
                onClick = {
                    openAccountRegister(context)
                })
        }
        }
    }

private fun logIn(email:String, password:String) {
    println("Estoy iniciando sesion con el email: $email y el password: $password")
}

private fun openAccountRegister(context:Context) {
    val intent = Intent(context, Register::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FindMySpotTheme {
        LoginComponent()
    }
}