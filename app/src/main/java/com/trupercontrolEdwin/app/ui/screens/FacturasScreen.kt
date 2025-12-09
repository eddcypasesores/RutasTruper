package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Factura
import com.trupercontrolEdwin.app.utils.CalculoRotulacion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasScreen(navController: NavController, folioId: Long) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    val folioPadre by db.folioDao().getById(folioId).collectAsState(initial = null)
    val facturas by db.facturaDao().getByFolio(folioId).collectAsState(initial = emptyList())

    var mostrarDialogo by remember { mutableStateOf(false) }
    var facturaParaCancelar by remember { mutableStateOf<Factura?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Facturas del Folio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarDialogo = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar Factura")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(facturas) { factura ->
                FacturaCard(
                    factura = factura,
                    onMarcarPagada = {
                        scope.launch {
                            // Actualizar estado de la factura
                            db.facturaDao().update(factura.copy(estado = "Pagado"))
                            
                            // Actualizar estado del folio padre
                            folioPadre?.let { 
                                db.folioDao().update(it.copy(estado = "Pagado"))
                            }
                        }
                    },
                    onCancelar = {
                        facturaParaCancelar = factura
                    }
                )
            }
        }
    }

    if (mostrarDialogo) {
        folioPadre?.let { folio ->
            val m2 = folio.m2Final ?: folio.m2Reportados ?: 0.0
            val figuras = folio.figuras ?: 0
            val (subtotal, iva, total) = CalculoRotulacion.calcular(m2, folio.tarifaTipo ?: "1-100", figuras)

            DialogNuevaFactura(
                totalCalculado = total,
                onDismiss = { mostrarDialogo = false },
                onSave = { folioFactura ->
                    scope.launch {
                        db.facturaDao().insert(
                            Factura(
                                folioId = folioId,
                                folioFactura = folioFactura,
                                subtotal = subtotal,
                                iva = iva,
                                total = total,
                                estado = "Pendiente"
                            )
                        )
                        mostrarDialogo = false
                    }
                }
            )
        }
    }

    if (facturaParaCancelar != null) {
        DialogCancelarFactura(
            factura = facturaParaCancelar!!,
            onDismiss = { facturaParaCancelar = null },
            onConfirm = { motivo ->
                scope.launch {
                    db.facturaDao().update(
                        facturaParaCancelar!!.copy(
                            estado = "Cancelado",
                            motivoCancelacion = motivo
                        )
                    )
                    facturaParaCancelar = null
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FacturaCard(
    factura: Factura,
    onMarcarPagada: () -> Unit,
    onCancelar: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = { menuExpanded = true }
            )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Factura: ${factura.folioFactura}", fontWeight = FontWeight.Bold)
            Text("Total: $${ "%,.2f".format(factura.total)}")
            Text("Estado: ${factura.estado}")
            factura.motivoCancelacion?.let {
                Text("Motivo: $it")
            }
        }

        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            if (factura.estado == "Pendiente") {
                DropdownMenuItem(
                    text = { Text("Marcar como Pagada") },
                    onClick = { onMarcarPagada(); menuExpanded = false }
                )
                DropdownMenuItem(
                    text = { Text("Cancelar Factura") },
                    onClick = { onCancelar(); menuExpanded = false }
                )
            }
        }
    }
}

@Composable
private fun DialogNuevaFactura(
    totalCalculado: Double,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var folioFactura by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Factura") },
        text = {
            Column {
                OutlinedTextField(
                    value = folioFactura,
                    onValueChange = { folioFactura = it },
                    label = { Text("Folio de la Factura (ej. EZ123)") },
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                Text("Total a facturar: $${ "%,.2f".format(totalCalculado)}", fontWeight = FontWeight.Bold)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(folioFactura) },
                enabled = folioFactura.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DialogCancelarFactura(
    factura: Factura,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var motivo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancelar Factura ${factura.folioFactura}") },
        text = {
            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo de cancelación") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(motivo) },
                enabled = motivo.isNotBlank()
            ) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
