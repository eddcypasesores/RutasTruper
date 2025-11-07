package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Factura
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasScreen(navController: NavController, folioId: Long) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    var facturas by remember { mutableStateOf(listOf<Factura>()) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(folioId) {
        db.facturaDao().getByFolio(folioId).collect { lista ->
            facturas = lista
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Facturas del folio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarDialogo = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar factura")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(facturas) { f ->
                    Card(Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Factura: ${f.folioFactura}")
                            Text("Total: $${ "%,.2f".format(f.total)}")
                            Text("Estatus: ${f.estatus}")
                            if (!f.motivoCancelacion.isNullOrEmpty()) {
                                Text("Motivo cancelación: ${f.motivoCancelacion}")
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogNuevaFactura(
            onDismiss = { mostrarDialogo = false },
            onSave = { folioFactura, total, estatus ->
                scope.launch {
                    db.facturaDao().insert(
                        Factura(
                            folioId = folioId,
                            folioFactura = folioFactura,
                            total = total,
                            subtotal = total / 1.16,
                            iva = total - (total / 1.16),
                            estatus = estatus
                        )
                    )
                }
                mostrarDialogo = false
            }
        )
    }
}

@Composable
private fun DialogNuevaFactura(
    onDismiss: () -> Unit,
    onSave: (String, Double, String) -> Unit
) {
    var folio by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }
    var estatus by remember { mutableStateOf("Pendiente") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva factura") },
        text = {
            Column {
                OutlinedTextField(value = folio, onValueChange = { folio = it }, label = { Text("Folio factura (EZ...)") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = total, onValueChange = { total = it }, label = { Text("Total") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = estatus, onValueChange = { estatus = it }, label = { Text("Estatus") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val t = total.toDoubleOrNull() ?: 0.0
                if (folio.isNotBlank()) onSave(folio, t, estatus)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
