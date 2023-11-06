package com.example.findmyspot.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findmyspot.Data
import com.example.findmyspot.components.MenuLateral
import com.example.findmyspot.ui.icons.CustomIcons

class NoInternet:ComponentActivity() {
    private val icon = CustomIcons()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            NoInternetScreen()
        }
    }

    @Composable
    fun NoInternetScreen(){
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(80.dp))

                    Text(
                        text = "Revisa tu conexion a internet e intentalo de nuevo",
                        fontSize = 50.sp,
                        modifier =
                                Modifier
                                . fillMaxWidth ()
                            .wrapContentHeight(align = Alignment.Top),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Icon(imageVector = icon.SignalWifiOff(), contentDescription = "No internet")
                }
            }


}