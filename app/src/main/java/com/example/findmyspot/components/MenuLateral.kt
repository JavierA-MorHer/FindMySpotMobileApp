package com.example.findmyspot.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.findmyspot.activities.Home
import com.example.findmyspot.activities.MetodosPago
import com.example.findmyspot.auth.Login
import com.example.findmyspot.ui.icons.CustomIcons
import kotlinx.coroutines.launch

class MenuLateral: ComponentActivity() {
    private var icons= CustomIcons()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MenuLateral(composableToWrap: @Composable () -> Unit) {
        val activity = LocalContext.current as ComponentActivity
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val sharedPreferences = activity.getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val nombre = sharedPreferences.getString("nombre", null)

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.Person, contentDescription = "Nombre")
                        Text("Hola, $nombre",fontSize = 20.sp, modifier = Modifier.padding(16.dp))
                    }
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Inicio") },
                        selected = false,
                        onClick = {
                            home(activity)
                        },
                        icon = {
                            Icon(imageVector = Icons.Filled.Home, contentDescription = "Inicio")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Métodos de pago") },
                        selected = false,
                        onClick = {
                            metodosPago(activity)
                        },
                        icon = {
                            Icon(imageVector = icons.CreditCardIcon(), contentDescription = "Metodos de pago")
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text(text = "Cerrar sesión") },
                        selected = false,
                        onClick = {
                            logOut(activity)
                        },
                        icon = {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Cerrar sesion")
                        }
                    )

                    // ...other drawer items
                }
            },
            modifier = Modifier.padding(10.dp),
            drawerState =drawerState
        ) {
            Scaffold (
                topBar = {
                    TopAppBar(title = {
                        Text(text = "FindMySpot")
                    },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            ){
                composableToWrap()
            }
        }
    }

    private fun logOut(activity: ComponentActivity){
        val sharedPreferences = activity.getSharedPreferences("FindMySpot", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("isLoggedIn")
        editor.apply()

        val intent = Intent(activity, Login::class.java)
        ContextCompat.startActivity(activity,intent,null)
        activity.finish()
    }

    private fun home(activity: ComponentActivity){
        val intent = Intent(activity, Home::class.java)
        ContextCompat.startActivity(activity,intent,null)
        activity.finish()
    }

    private fun metodosPago(activity: ComponentActivity){
        val intent = Intent(activity, MetodosPago::class.java)
        ContextCompat.startActivity(activity,intent,null)
        activity.finish()
    }


}