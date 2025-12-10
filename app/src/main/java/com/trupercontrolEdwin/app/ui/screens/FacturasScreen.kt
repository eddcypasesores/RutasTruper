package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import com.trupercontrolEdwin.app.utils.FolioStatusManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturasScreen(navController: NavController, folioId: Long) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()
    val folioStatusManager = remember { FolioStatusManager(db) }

    val folioPadre by db.folioDao().getById(folioId).collectAsState(initial = null)
    val facturas by db.facturaDao().getByFolio(folioId).collectAsState(initial = emptyList())

    var mostrarDialogoNueva by remember { mutableStateOf(false) }
    var facturaParaCancelar by remember { mutableStateOf<Factura?>(null) }
    var facturaParaEditar by remember { mutableStateOf<Factura?>(null) }

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
                    IconButton(onClick = { mostrarDialogoNueva = true }) {
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
                    onEditar = { facturaParaEditar = factura },
                    onMarcarPagada = {
                        scope.launch {
                            db.facturaDao().update(factura.copy(estado = "Pagado"))
                            folioStatusManager.actualizarEstadoFolio(folioId)
                        }
                    },
                    onCancelar = {
                        facturaParaCancelar = factura
                    }
                )
            }
        }
    }

    if (mostrarDialogoNueva) {
        folioPadre?.let { folio ->
            val m2 = folio.m2Final ?: folio.m2Reportados ?: 0.0
            val figuras = folio.figuras ?: 0
            val (subtotal, iva, total) = CalculoRotulacion.calcular(m2, folio.tarifaTipo ?: "1-100", figuras)

            DialogNuevaFactura(
                totalCalculado = total,
                onDismiss = { mostrarDialogoNueva = false },
                onSave = { folioFactura, fechaSeleccionada ->
                    scope.launch {
                        db.facturaDao().insert(
                            Factura(
                                folioId = folioId,
                                folioFactura = folioFactura,
                                subtotal = subtotal,
                                iva = iva,
                                total = total,
                                estado = "Pendiente",
                                fechaCreacion = fechaSeleccionada
                            )
                        )
                        folioStatusManager.actualizarEstadoFolio(folioId)
                        mostrarDialogoNueva = false
                    }
                }
            )
        }
    }

    if (facturaParaEditar != null) {
        DialogoEditarFactura(
            factura = facturaParaEditar!!,
            onDismiss = { facturaParaEditar = null },
            onSave = { facturaActualizada ->
                scope.launch {
                    db.facturaDao().update(facturaActualizada)
                    folioStatusManager.actualizarEstadoFolio(folioId)
                    facturaParaEditar = null
                }
            }
        )
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
                    folioStatusManager.actualizarEstadoFolio(folioId)
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
    onEditar: () -> Unit,
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
            DropdownMenuItem(
                text = { Text("Editar") },
                onClick = { onEditar(); menuExpanded = false },
                leadingIcon = { Icon(Icons.Default.Edit, null) }
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogNuevaFactura(
    totalCalculado: Double,
    onDismiss: () -> Unit,
    onSave: (String, Long) -> Unit
) {
    var folioFactura by remember { mutableStateOf("") }
    var fechaSeleccionada by remember { mutableStateOf(System.currentTimeMillis()) }
    var mostrarDatePicker by remember { mutableStateOf(false) }

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = fechaSeleccionada)
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { fechaSeleccionada = it }
                    mostrarDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
                OutlinedButton(onClick = { mostrarDatePicker = true }) {
                    Text("Fecha: ${sdf.format(Date(fechaSeleccionada))}")
                }
                Spacer(Modifier.height(16.dp))
                Text("Total a facturar: $${ "%,.2f".format(totalCalculado)}", fontWeight = FontWeight.Bold)
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(folioFactura, fechaSeleccionada) },
                enabled = folioFactura.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoEditarFactura(
    factura: Factura,
    onDismiss: () -> Unit,
    onSave: (Factura) -> Unit
) {
    var folioFactura by remember { mutableStateOf(factura.folioFactura) }
    var subtotal by remember { mutableStateOf(factura.subtotal.toString()) }
    var iva by remember { mutableStateOf(factura.iva.toString()) }
    var total by remember { mutableStateOf(factura.total.toString()) }
    var fechaSeleccionada by remember { mutableStateOf(factura.fechaCreacion) }
    var estado by remember { mutableStateOf(factura.estado) }

    var mostrarDatePicker by remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = fechaSeleccionada)
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { fechaSeleccionada = it }
                    mostrarDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Factura") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = folioFactura, onValueChange = { folioFactura = it }, label = { Text("Folio de Factura") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = subtotal, onValueChange = { subtotal = it }, label = { Text("Subtotal") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = iva, onValueChange = { iva = it }, label = { Text("IVA") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = total, onValueChange = { total = it }, label = { Text("Total") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { mostrarDatePicker = true }) {
                    Text("Fecha: ${sdf.format(Date(fechaSeleccionada))}")
                }
                Spacer(Modifier.height(8.dp))
                DropdownSelector(label = "Estado", selected = estado, options = listOf("Pendiente", "Pagado", "Cancelado"), onSelected = { estado = it })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val facturaActualizada = factura.copy(
                    folioFactura = folioFactura,
                    subtotal = subtotal.toDoubleOrNull() ?: factura.subtotal,
                    iva = iva.toDoubleOrNull() ?: factura.iva,
                    total = total.toDoubleOrNull() ?: factura.total,
                    fechaCreacion = fechaSeleccionada,
                    estado = estado
                )
                onSave(facturaActualizada)
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
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

// Re-utilizable DropdownSelector de FoliosScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
