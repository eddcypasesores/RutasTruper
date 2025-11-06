package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InicioScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Control de Rotulación Truper") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate("rutas") }) {
                Text("📦 Rutas")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.navigate("reportes") }) {
                Text("📊 Reportes")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.navigate("config") }) {
                Text("⚙️ Configuración")
            }
        }
    }
}
