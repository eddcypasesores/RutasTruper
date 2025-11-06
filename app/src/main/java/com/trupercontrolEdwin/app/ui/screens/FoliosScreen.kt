package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Folio
import kotlinx.coroutines.launch

@Composable
fun FoliosScreen(navController: NavController, rutaId: Long? = null) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    var folios by remember { mutableStateOf(listOf<Folio>()) }
    var busqueda by remember { mutableStateOf("") }

    LaunchedEffect(rutaId) {
        if (rutaId != null) {
            db.folioDao().getByRuta(rutaId).collect { lista ->
                folios = lista
            }
        } else {
            // si no viene ruta, podrías crear un DAO getAll()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Folios") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.padding(12.dp))
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar folio TRUPER") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            val listaFiltrada = if (busqueda.isNotBlank()) {
                folios.filter { it.folioTruper.contains(busqueda, ignoreCase = true) }
            } else folios

            LazyColumn(Modifier.fillMaxSize()) {
                items(listaFiltrada) { folio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                // vamos a facturas de este folio
                                navController.navigate("facturas?folioId=${folio.id}")
                            }
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Folio Truper: ${folio.folioTruper}", style = MaterialTheme.typography.titleMedium)
                            Text("Establecimiento: ${folio.nombreEstablecimiento ?: "-"}")
                            Text("m² reportados: ${folio.m2Reportados ?: 0.0}")
                            Text("Estado: ${folio.estado}")
                        }
                    }
                }
            }
        }
    }
}
