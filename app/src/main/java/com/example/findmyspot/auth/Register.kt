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
import com.example.findmyspot.auth.model.User
import com.example.findmyspot.ui.theme.FindMySpotTheme

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterComponent()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterComponent(){
        val context = LocalContext.current

        val primaryColor = Color(0xFF228B22) // Puedes definir el color aquí o usar uno de los colores personalizados definidos anteriormente
        var firstNameInputValue by remember { mutableStateOf("") }
        var lastNameInputValue by remember { mutableStateOf("") }
        var emailInputValue by remember { mutableStateOf("") }
        var passwordInputValue by remember { mutableStateOf("") }
        var passwordConfirmationInputValue by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }


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
            Text(text = "FINDMYSPOT", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(80.dp))

            Text(text = "Crear cuenta", fontSize = 25.sp)
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = firstNameInputValue,
                onValueChange = {firstNameInputValue =it},
                colors = textFieldColors,
                label = { Text("Nombre") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = lastNameInputValue,
                onValueChange = {lastNameInputValue =it},
                colors = textFieldColors,
                label = { Text("Apellido") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                ),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = emailInputValue,
                onValueChange = {emailInputValue =it},
                colors = textFieldColors,
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                ),

                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = passwordInputValue,
                onValueChange = {passwordInputValue = it},
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
                value = passwordConfirmationInputValue,
                onValueChange = {passwordConfirmationInputValue =it},
                colors = textFieldColors,
                label = { Text("Confirma la contraseña") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                ),
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                val user = User(firstNameInputValue,lastNameInputValue,emailInputValue,passwordInputValue)
                createAccount(user)
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