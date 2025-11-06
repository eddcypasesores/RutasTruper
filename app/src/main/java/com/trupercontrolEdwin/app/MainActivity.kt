package com.trupercontrolEdwin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.trupercontrolEdwin.app.navigation.NavGraph
import com.trupercontrolEdwin.app.theme.ControlRotulacionesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControlRotulacionesTheme {
                NavGraph()
            }
        }
    }
}
