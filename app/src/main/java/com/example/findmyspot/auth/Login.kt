package com.example.findmyspot.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import androidx.compose.ui.window.DialogProperties
import com.example.findmyspot.activities.Home
import com.example.findmyspot.activities.NoInternet
import com.example.findmyspot.auth.model.ApiAuthService
import com.example.findmyspot.auth.model.User
import com.example.findmyspot.helpers.getRetrofitUsuarios
import com.example.findmyspot.helpers.isInternetAvailable
import com.example.findmyspot.ui.theme.FindMySpotTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isInternetAvailable(this)) {
            if (isLoggedIn) {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            } else {
                setContent {
                    LoginComponent()
                }
            }
        } else {
            val intent = Intent(this, NoInternet::class.java)
            startActivity(intent)
        }

        


    }

    private fun setSharedPreferences(usuario: User) {
        val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("nombre", usuario.nombre)
        editor.putString("apellidoPaterno", usuario.apellidoPaterno)
        editor.putString("apellidoMaterno", usuario.apellidoMaterno)
        editor.putString("telefono", usuario.telefono)
        editor.putString("email", usuario.email)
        editor.putString("password", usuario.password)
        editor.putInt("id",usuario.id_Usuario.toInt())
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }

    private fun openAccountRegister(context:Context) {
        val intent = Intent(context, Register::class.java)
        context.startActivity(intent)
    }

    @Composable
    fun BasicDialog(
        title: String,
        message: String,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {},
            dismissButton = {
                onDismiss()
            }

        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginComponent() {
        val primaryColor = Color(0xFF228B22) // Puedes definir el color aquí o usar uno de los colores personalizados definidos anteriormente
        var emailInputValue by remember { mutableStateOf("") }
        var passwordInputValue by remember { mutableStateOf("") }
        val context = LocalContext.current
        var showLoadingDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var msgError by remember { mutableStateOf("") }
        var isDialogVisible by remember { mutableStateOf(true) }


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

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(msgError) },
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
        Modifier
            .fillMaxWidth()
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
            Button(onClick = {
                if (emailInputValue.isNotEmpty() && passwordInputValue.isNotEmpty()){

                    val sharedPreferences = getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
                    val nombre = sharedPreferences.getString("nombre", null)

                    var entradaActiva = sharedPreferences.getBoolean("EstanciaActiva", false)
                    val idUsuarioEntrada = sharedPreferences.getInt("idUsuarioEntrada", 0)
                    val email = sharedPreferences.getString("email", null)
                    val apellidoPaterno = sharedPreferences.getString("apellidoPaterno", null)


                    showLoadingDialog = true

                    val requestBody = User(email = emailInputValue, password = passwordInputValue)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val call = getRetrofitUsuarios().create(ApiAuthService::class.java).login(requestBody)
                            if(call.code()== 200){
                                val usuario = call.body()

                                if (usuario != null) {
                                    if (!entradaActiva){
                                        setSharedPreferences(usuario)
                                        val intent = Intent(context, Home::class.java)
                                        context.startActivity(intent)
                                    }else{
                                        if(idUsuarioEntrada != usuario.id_Usuario.toInt()){
                                            msgError = "El usuario $nombre $apellidoPaterno ($email) tiene una estancia en curso, finalicela para continuar."
                                            showLoadingDialog = false
                                            showErrorDialog = true
                                        }else{
                                            setSharedPreferences(usuario)
                                            val intent = Intent(context, Home::class.java)
                                            context.startActivity(intent)
                                        }

                                    }
                                }
                            } else{
                                msgError = "El usuario y/o contraseña son incorrectos"
                                showLoadingDialog = false
                                showErrorDialog = true
                                // La solicitud no fue exitosa. Puedes manejar el error aquí.
                                val errorBody = call.errorBody()
                                if (errorBody != null) {
                                    println("CODIGO ERROR: ${call.code()}")
                                    val errorMessage = errorBody.string()
                                    println("Error en la solicitud: $errorMessage")
                                } else {
                                    println("Error en la solicitud, pero no se pudo obtener el mensaje de error.")
                                }
                            }
                        }catch (e:Exception){
                            println("Error: ${e.message}")
                            msgError = "Se perdio la comunicacion con el servidor"
                            showLoadingDialog = false
                            showErrorDialog = true
                        }
                    }
                }else{
                    msgError = "Por favor, ingrese su email y contraseña"
                    showErrorDialog = true
                }},
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
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FindMySpotTheme {
            LoginComponent()
        }
    }
}




