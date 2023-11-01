package com.example.findmyspot.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.findmyspot.activities.Home
import com.example.findmyspot.auth.model.ApiAuthService
import com.example.findmyspot.auth.model.NewUser
import com.example.findmyspot.auth.model.User
import com.example.findmyspot.auth.model.isValid
import com.example.findmyspot.helpers.getRetrofitUsuarios
import com.example.findmyspot.ui.theme.FindMySpotTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterComponent()
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
        editor.putString("id",usuario.id_Usuario)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterComponent(){
        val context = LocalContext.current

        val primaryColor = Color(0xFF228B22)
        var nombre by remember { mutableStateOf("") }
        var apellidoPaterno by remember { mutableStateOf("") }
        var apellidoMaterno by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var telefono by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordConfirmation by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        var showLoadingDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var msgError by remember { mutableStateOf("") }

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

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "FindMySpot", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Crear cuenta", fontSize = 25.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = {nombre =it},
                colors = textFieldColors,
                label = { Text("Nombre") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = apellidoPaterno,
                onValueChange = {apellidoPaterno =it},
                colors = textFieldColors,
                label = { Text("Apellido paterno") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = apellidoMaterno,
                onValueChange = {apellidoMaterno =it},
                colors = textFieldColors,
                label = { Text("Apellido materno") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {email =it},
                colors = textFieldColors,
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                ),

                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = {telefono =it},
                colors = textFieldColors,
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text("Contraseña") },
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusRequester.requestFocus() // Mover el foco al siguiente campo de texto
                    }),
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = passwordConfirmation,
                onValueChange = {passwordConfirmation =it},
                colors = textFieldColors,
                label = { Text("Confirma la contraseña") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                ),
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                if ( password == passwordConfirmation ){
                    showLoadingDialog = true
                    val newUser = NewUser(nombre,apellidoPaterno,apellidoMaterno,email,telefono,password)
                    if (newUser.isValid()){
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val call = getRetrofitUsuarios().create(ApiAuthService::class.java).createUser(newUser)
                                if(call.code()== 200){
                                    val usuario = call.body()

                                    if (usuario != null) {
                                        setSharedPreferences(usuario)
                                        val intent = Intent(context, Home::class.java)
                                        context.startActivity(intent)
                                    }

                                } else{
                                    msgError = "Hubo un error al crear la cuenta."
                                    showLoadingDialog = false
                                    showErrorDialog = true
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
                    }else{
                        msgError = "Completa todos los campos"
                        showLoadingDialog = false
                        showErrorDialog = true
                    }

                }else{
                    msgError = "Las contraseñas deben coincidir"
                    showLoadingDialog = false
                    showErrorDialog = true
                }


            },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(primaryColor)
            ) {
                Text(text = "Crear cuenta")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row() {
                Text(text = "¿Ya tienes una cuenta?")
                Spacer(modifier = Modifier.width(5.dp))

                ClickableText(
                    text = AnnotatedString("Inicia sesión"),
                    style = textStyle,
                    onClick = {
                        openLogin(context)
                    })
            }
        }
    }

    private fun openLogin(context: Context){
        val intent = Intent(context, Login::class.java)
        context.startActivity(intent)
    }

    private fun createAccount(user:User){
        println("Creando cuenta de $user")
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FindMySpotTheme {
            RegisterComponent()
        }
    }

}