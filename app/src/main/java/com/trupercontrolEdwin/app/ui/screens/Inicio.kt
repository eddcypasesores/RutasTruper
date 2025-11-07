package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Control de Rotulaciones") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigate("rutas") }, modifier = Modifier.fillMaxWidth()) {
                Text("Rutas")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.navigate("reportes") }, modifier = Modifier.fillMaxWidth()) {
                Text("Reportes")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.navigate("pagos") }, modifier = Modifier.fillMaxWidth()) {
                Text("Pagos")
            }
            Spacer(Modifier.height(32.dp))
            OutlinedButton(onClick = { navController.navigate("configuracion") }, modifier = Modifier.fillMaxWidth()) {
                Text("Configuración")
            }
        }
    }
}
