package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagosScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Control de Pagos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            // Aquí iría la lógica para mostrar los pagos pendientes y liquidados
            LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                item {
                    Text("Facturas pendientes de pago", style = MaterialTheme.typography.titleMedium)
                }
                // Lista de facturas pendientes

                item {
                    Spacer(Modifier.height(24.dp))
                    Text("Facturas liquidadas", style = MaterialTheme.typography.titleMedium)
                }
                // Lista de facturas liquidadas
            }
        }
    }
}
