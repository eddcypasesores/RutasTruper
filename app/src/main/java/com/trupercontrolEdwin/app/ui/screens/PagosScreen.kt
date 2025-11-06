package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Pago
import kotlinx.coroutines.launch

@Composable
fun PagosScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    // en una versión más avanzada traerás facturas y sus pagos
    var pagos by remember { mutableStateOf(listOf<Pago>()) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // aquí deberías crear un DAO para traer todos los pagos (por simplicidad no lo hicimos)
    // podrías mostrar sólo formulario

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarDialogo = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo pago")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Text("Aquí se listarán los pagos detectados automáticamente por PDF", Modifier.padding(16.dp))

            LazyColumn {
                items(pagos) { p ->
                    Card(Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Factura ID: ${p.facturaId}")
                            Text("Monto: $${"%,.2f".format(p.monto)}")
                            Text("Fecha: ${p.fechaPago ?: "-"}")
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogNuevoPago(
            onDismiss = { mostrarDialogo = false },
            onSave = { facturaId, monto ->
                scope.launch {
                    db.pagoDao().insert(
                        Pago(
                            facturaId = facturaId,
                            monto = monto
                        )
                    )
                    // aquí también podrías marcar la factura como pagada
                    db.facturaDao().updateEstatus(facturaId, "Pagada")
                }
                mostrarDialogo = false
            }
        )
    }
}

@Composable
private fun DialogNuevoPago(
    onDismiss: () -> Unit,
    onSave: (Long, Double) -> Unit
) {
    var facturaIdTxt by remember { mutableStateOf("") }
    var montoTxt by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar pago") },
        text = {
            Column {
                OutlinedTextField(value = facturaIdTxt, onValueChange = { facturaIdTxt = it }, label = { Text("ID de factura") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = montoTxt, onValueChange = { montoTxt = it }, label = { Text("Monto pagado") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val id = facturaIdTxt.toLongOrNull()
                val monto = montoTxt.toDoubleOrNull()
                if (id != null && monto != null) onSave(id, monto)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
