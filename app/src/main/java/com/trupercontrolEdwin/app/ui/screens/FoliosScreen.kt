package com.trupercontrolEdwin.app.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.utils.CalculoRotulacion
import com.trupercontrolEdwin.app.utils.ExcelGenerator
import com.trupercontrolEdwin.app.utils.ExcelReader
import com.trupercontrolEdwin.app.utils.FolioParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoliosScreen(navController: NavController, rutaId: Long) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    val todosLosFolios by db.folioDao().getByRuta(rutaId).collectAsState(initial = emptyList())
    var busqueda by remember { mutableStateOf("") }

    val foliosFiltrados = remember(busqueda, todosLosFolios) {
        if (busqueda.isBlank()) {
            todosLosFolios
        } else {
            todosLosFolios.filter {
                it.folioTruper.contains(busqueda, ignoreCase = true) ||
                it.nombreEstablecimiento?.contains(busqueda, ignoreCase = true) == true
            }
        }
    }

    // Agrupación por secciones
    val seccionesDefinidas = listOf("Pendientes", "En validacion", "Validados", "En facturacion", "Facturados", "Pagados", "Cancelados")
    
    val foliosAgrupados = remember(foliosFiltrados) {
        foliosFiltrados.groupBy { folio ->
            when (folio.estado) {
                "Coincide", "No coincide" -> "Pendientes"
                "En validacion" -> "En validacion"
                "Validado" -> "Validados"
                "En facturacion" -> "En facturacion"
                "Facturado" -> "Facturados"
                "Pagado" -> "Pagados"
                "Cancelado" -> "Cancelados"
                else -> "Pendientes"
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    
    // Estados para diálogos
    var folioEnEdicion by remember { mutableStateOf<Folio?>(null) }
    var folioEnDetalle by remember { mutableStateOf<Folio?>(null) }
    var folioParaCambioM2 by remember { mutableStateOf<Folio?>(null) }
    var mensajeValidacion by remember { mutableStateOf<String?>(null) }
    var reporteGenerado by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoAgregarManual by remember { mutableStateOf(false) }
    
    // Estado de expansión para todas las secciones
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    var addMenuExpanded by remember { mutableStateOf(false) }

    val excelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                val foliosParaExcel = todosLosFolios.filter { it.estado == "Validado" }
                if (foliosParaExcel.isEmpty()) {
                    snackbarHostState.showSnackbar("No hay folios 'Validados' para exportar")
                    return@launch
                }
                val (ok, errorMsg) = generarExcel(context, foliosParaExcel, it)
                if (ok) {
                    withContext(Dispatchers.IO) {
                        foliosParaExcel.forEach { f ->
                            db.folioDao().update(f.copy(estado = "En facturacion"))
                        }
                    }
                    snackbarHostState.showSnackbar("Excel generado y folios actualizados")
                } else {
                    snackbarHostState.showSnackbar("Error: $errorMsg")
                }
            }
        }
    }

    val agregarExcelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                snackbarHostState.showSnackbar("Leyendo Excel...")
                val solicitudesNuevas = withContext(Dispatchers.IO) {
                    ExcelReader.leerExcelSolicitudes(context, it)
                }
                if (solicitudesNuevas.isEmpty()) {
                    snackbarHostState.showSnackbar("No se encontraron datos en el Excel")
                    return@launch
                }

                var agregados = 0
                withContext(Dispatchers.IO) {
                    solicitudesNuevas.forEach { sol ->
                        val existente = db.folioDao().findByFolioAndRuta(sol.folio.trim(), rutaId)
                        if (existente == null) {
                            // Se detecta si es MY o MS desde extraInfo (columna rotulación del Excel)
                            // Si extraInfo contiene "MY", se asume Mayorista.
                            var observacionesIniciales = ""
                            if (!sol.extraInfo.isNullOrBlank()) {
                                observacionesIniciales = "Solicitud: ${sol.extraInfo}"
                            }

                            db.folioDao().insert(
                                Folio(
                                    rutaId = rutaId,
                                    folioTruper = sol.folio.trim(),
                                    nombreEstablecimiento = sol.nombreNegocio,
                                    direccion = sol.direccion,
                                    m2Reportados = sol.metrosReportados,
                                    // Se usa observaciones para guardar info de MS/MY por ahora
                                    observaciones = observacionesIniciales.ifBlank { null },
                                    tipoFachada = sol.tipoFachada, // "Sin Opción" si venía vacío
                                    estado = "Coincide"
                                )
                            )
                            agregados++
                        }
                    }
                }
                snackbarHostState.showSnackbar("$agregados folios nuevos agregados a la ruta")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Folios de la Ruta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { addMenuExpanded = true }) {
                            Icon(Icons.Filled.Add, contentDescription = "Agregar Folio")
                        }
                        DropdownMenu(
                            expanded = addMenuExpanded,
                            onDismissRequest = { addMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Agregar Manualmente") },
                                onClick = { 
                                    mostrarDialogoAgregarManual = true
                                    addMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Agregar desde Excel") },
                                onClick = { 
                                    agregarExcelLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                    addMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por folio o cliente") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.End) {
                Button(onClick = {
                    val hayValidados = todosLosFolios.any { it.estado == "Validado" }
                    if (!hayValidados) {
                        scope.launch { snackbarHostState.showSnackbar("No hay folios validados") }
                    } else {
                        excelLauncher.launch("facturacion_rotulaciones.xlsx")
                    }
                }) {
                    Text("Generar Excel")
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(Modifier.fillMaxSize()) {
                seccionesDefinidas.forEach { seccion ->
                    val foliosDeSeccion = foliosAgrupados[seccion]
                    val isExpanded = expandedStates[seccion] ?: false
                    
                    if (!foliosDeSeccion.isNullOrEmpty()) {
                        item {
                            SectionHeader(
                                titulo = seccion, 
                                cantidad = foliosDeSeccion.size, 
                                isExpanded = isExpanded, 
                                onToggle = { expandedStates[seccion] = !isExpanded }
                            )
                        }
                        if (isExpanded) {
                             items(foliosDeSeccion) { folio ->
                                FolioCardCompact(
                                    folio = folio,
                                    onVerDetalle = { folioEnDetalle = folio },
                                    onEditar = { folioEnEdicion = folio },
                                    onCambiarM2 = { folioParaCambioM2 = folio },
                                    onGenerarValidacion = { 
                                        scope.launch {
                                            mensajeValidacion = FolioParser.generarMensajeValidacion(folio, null)
                                            db.folioDao().update(folio.copy(estado = "En validacion"))
                                            snackbarHostState.showSnackbar("Folio puesto 'En validación'")
                                        }
                                    },
                                    onFacturas = { navController.navigate("facturas/${folio.id}") },
                                    onMarcarValidado = {
                                        scope.launch {
                                            db.folioDao().update(folio.copy(estado = "Validado"))
                                            snackbarHostState.showSnackbar("Folio ${folio.folioTruper} validado")
                                        }
                                    },
                                    onMarcarFacturado = {
                                        scope.launch {
                                            db.folioDao().update(folio.copy(estado = "Facturado"))
                                            snackbarHostState.showSnackbar("Folio ${folio.folioTruper} marcado como Facturado")
                                        }
                                    },
                                    onCancelarFolio = { 
                                        scope.launch {
                                            db.folioDao().update(folio.copy(estado = "Cancelado"))
                                            snackbarHostState.showSnackbar("Folio ${folio.folioTruper} cancelado")
                                        }
                                    },
                                    onReactivarFolio = { 
                                        scope.launch {
                                            db.folioDao().update(folio.copy(estado = "Coincide"))
                                            snackbarHostState.showSnackbar("Folio ${folio.folioTruper} reactivado")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // --- DIÁLOGOS ---
    if (mostrarDialogoAgregarManual) {
        DialogAgregarFolioManual(
            onDismiss = { mostrarDialogoAgregarManual = false },
            onSave = { nuevoFolio ->
                scope.launch {
                    // Validar que el folio no exista ya en esta ruta
                    val existente = withContext(Dispatchers.IO) { 
                        db.folioDao().findByFolioAndRuta(nuevoFolio.folioTruper, rutaId) 
                    }
                    if (existente != null) {
                        snackbarHostState.showSnackbar("El folio ${nuevoFolio.folioTruper} ya existe en esta ruta")
                    } else {
                        db.folioDao().insert(nuevoFolio.copy(rutaId = rutaId))
                        mostrarDialogoAgregarManual = false
                        snackbarHostState.showSnackbar("Folio agregado manualmente")
                    }
                }
            }
        )
    }

    if (folioEnDetalle != null) {
        DialogDetalleFolio(
            folio = folioEnDetalle!!,
            onDismiss = { folioEnDetalle = null },
            onModificar = {
                val f = folioEnDetalle!!
                folioEnDetalle = null
                folioEnEdicion = f
            }
        )
    }

    if (folioEnEdicion != null) {
        DialogEditarFolio(
            folio = folioEnEdicion!!,
            onDismiss = { folioEnEdicion = null },
            onSave = { actualizado ->
                scope.launch {
                    db.folioDao().update(actualizado)
                    folioEnEdicion = null
                    snackbarHostState.showSnackbar("Folio actualizado")
                }
            }
        )
    }

    if (folioParaCambioM2 != null) {
        DialogCambiarM2(
            folio = folioParaCambioM2!!,
            onDismiss = { folioParaCambioM2 = null },
            onGuardar = { m2Finales ->
                scope.launch {
                    val folio = folioParaCambioM2!!
                    val textoReporte = FolioParser.generarTextoReporteCambio(folio, m2Finales)
                    val actualizado = folio.copy(
                        m2Final = m2Finales, 
                        cambioTexto = textoReporte, 
                        estado = "Coincide"
                    )
                    db.folioDao().update(actualizado)
                    folioParaCambioM2 = null
                    reporteGenerado = textoReporte
                }
            }
        )
    }

    if (mensajeValidacion != null) {
        DialogMensajeCopiable(
            titulo = "Mensaje de validación",
            mensaje = mensajeValidacion!!,
            onDismiss = { mensajeValidacion = null }
        )
    }

    if (reporteGenerado != null) {
        DialogMensajeCopiable(
            titulo = "Reporte de Modificación",
            mensaje = reporteGenerado!!,
            onDismiss = { reporteGenerado = null }
        )
    }
}

@Composable
fun SectionHeader(
    titulo: String, 
    cantidad: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
            contentDescription = if (isExpanded) "Contraer" else "Expandir"
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        Badge {
            Text(cantidad.toString())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FolioCardCompact(
    folio: Folio,
    onVerDetalle: () -> Unit,
    onEditar: () -> Unit,
    onCambiarM2: () -> Unit,
    onGenerarValidacion: () -> Unit,
    onFacturas: () -> Unit,
    onMarcarValidado: () -> Unit,
    onMarcarFacturado: () -> Unit,
    onCancelarFolio: () -> Unit,
    onReactivarFolio: () -> Unit
) {
    val tarifa = folio.tarifaTipo ?: "1-100"
    val figuras = folio.figuras ?: 0
    val m2Final = folio.m2Final ?: folio.m2Reportados ?: 0.0
    val (_, _, total) = CalculoRotulacion.calcular(m2Final, tarifa, figuras)
    
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = onVerDetalle,
                onLongClick = { menuExpanded = true }
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = folio.folioTruper,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = folio.estado,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = "${"%.2f".format(m2Final)} m²",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$${"%,.2f".format(total)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            val estado = folio.estado
            val esPendiente = estado == "Coincide" || estado == "No coincide" || estado == "Pendientes"
            val esEnValidacion = estado == "En validacion"
            val esEnFacturacion = estado == "En facturacion"
            val esFacturadoOPagado = estado == "Facturado" || estado == "Pagado"
            val esCancelado = estado == "Cancelado"

            if (esPendiente) {
                DropdownMenuItem(text = { Text("Cambiar m²") }, onClick = { menuExpanded = false; onCambiarM2() })
                DropdownMenuItem(text = { Text("Enviar a Validación") }, onClick = { menuExpanded = false; onGenerarValidacion() })
                DropdownMenuItem(text = { Text("Cancelar Folio") }, onClick = { menuExpanded = false; onCancelarFolio() })
            }
            if (esEnValidacion) { DropdownMenuItem(text = { Text("Aprobar Validación") }, onClick = { menuExpanded = false; onMarcarValidado() }) }
            if (esEnFacturacion) { DropdownMenuItem(text = { Text("Marcar Facturado") }, onClick = { menuExpanded = false; onMarcarFacturado() }) }
            if (esFacturadoOPagado) { DropdownMenuItem(text = { Text("Ver Facturas") }, onClick = { menuExpanded = false; onFacturas() }) }
            if (esCancelado) { DropdownMenuItem(text = { Text("Reactivar Folio") }, onClick = { menuExpanded = false; onReactivarFolio() }) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogAgregarFolioManual(onDismiss: () -> Unit, onSave: (Folio) -> Unit) {
    var folioTruper by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var m2Reportados by remember { mutableStateOf("") }
    var figuras by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    val tarifasOptions = listOf("1-100", "101-300", "301+")
    var tarifa by remember { mutableStateOf(tarifasOptions.first()) }

    val fachadaOptions = listOf("Opción A", "Opción B", "Master", "Sin Opción")
    var tipo by remember { mutableStateOf(fachadaOptions.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Folio Manualmente") },
        text = {
            Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = folioTruper, onValueChange = { folioTruper = it }, label = { Text("Folio TRUPER") }, isError = folioTruper.isBlank())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del establecimiento") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") })
                Spacer(Modifier.height(8.dp))
                DropdownSelector(label = "Tarifa", selected = tarifa, options = tarifasOptions, onSelected = { tarifa = it })
                Spacer(Modifier.height(8.dp))
                DropdownSelector(label = "Tipo de fachada", selected = tipo, options = fachadaOptions, onSelected = { tipo = it })
                Spacer(Modifier.height(8.dp))
                val decimalPattern = remember { Regex("^\\d*\\.?\\d{0,2}$$") }
                OutlinedTextField(
                    value = m2Reportados,
                    onValueChange = { if (it.isEmpty() || it.matches(decimalPattern)) m2Reportados = it },
                    label = { Text("m² Solicitados") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(Modifier.height(8.dp))
                val intPattern = remember { Regex("^\\d*$") }
                OutlinedTextField(
                    value = figuras,
                    onValueChange = { if (it.isEmpty() || it.matches(intPattern)) figuras = it },
                    label = { Text("Figuras/Cajones") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas") }, minLines = 2)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val nuevoFolio = Folio(
                        folioTruper = folioTruper.trim(),
                        nombreEstablecimiento = nombre.ifBlank { null },
                        direccion = direccion.ifBlank { null },
                        tipoFachada = tipo,
                        m2Reportados = m2Reportados.toDoubleOrNull(),
                        figuras = figuras.toIntOrNull(),
                        tarifaTipo = tarifa,
                        estado = "Coincide", // Estado inicial por defecto
                        rutaId = 0 // El ID se asigna en el `onSave`
                    )
                    onSave(nuevoFolio)
                },
                enabled = folioTruper.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}


@Composable
private fun DialogDetalleFolio(
    folio: Folio,
    onDismiss: () -> Unit,
    onModificar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de Folio") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                DetalleItem("Folio TRUPER", folio.folioTruper)
                DetalleItem("Cliente", folio.nombreEstablecimiento ?: "-")
                DetalleItem("Dirección", folio.direccion ?: "-")
                DetalleItem("Estado", folio.estado)
                
                Divider(Modifier.padding(vertical = 8.dp))
                
                // Mostrar datos reales sin forzar valores por defecto si vienen nulos o raros
                DetalleItem("Tarifa", folio.tarifaTipo ?: "No definida")
                DetalleItem("Fachada", folio.tipoFachada ?: "No definida")
                DetalleItem("Figuras", (folio.figuras ?: 0).toString())
                
                Divider(Modifier.padding(vertical = 8.dp))
                
                val m2Rep = folio.m2Reportados ?: 0.0
                val m2Fin = folio.m2Final ?: m2Rep
                val (subtotal, iva, total) = CalculoRotulacion.calcular(m2Fin, folio.tarifaTipo ?: "1-100", folio.figuras ?: 0)
                
                DetalleItem("m² Solicitados", "%.2f".format(m2Rep))
                DetalleItem("m² Finales", "%.2f".format(m2Fin))
                
                Spacer(Modifier.height(8.dp))
                
                DetalleItem("Subtotal", "$${"%,.2f".format(subtotal)}")
                DetalleItem("IVA", "$${"%,.2f".format(iva)}")
                DetalleItem("TOTAL", "$${"%,.2f".format(total)}", resaltado = true)
                
                val observaciones = folio.observaciones
                if (!observaciones.isNullOrBlank()) {
                    Divider(Modifier.padding(vertical = 8.dp))
                    Text("Notas:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                    Text(observaciones, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            // El botón Modificar solo aparece si el estado lo permite
            if (folio.estado !in listOf("En facturacion", "Facturado", "Pagado")) {
                Button(onClick = onModificar) { 
                    Text("Modificar") 
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
fun DetalleItem(label: String, value: String, resaltado: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            value, 
            style = if(resaltado) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if(resaltado) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun DialogCambiarM2(
    folio: Folio,
    onDismiss: () -> Unit,
    onGuardar: (Double) -> Unit
) {
    var m2Finales by remember { mutableStateOf(folio.m2Final?.toString() ?: "") }
    val decimalPattern = remember { Regex("^\\d*\\.?\\d{0,2}$$") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modificar Metros") },
        text = {
            Column {
                Text("Folio: ${folio.folioTruper}")
                Text("Solicitud: ${folio.m2Reportados ?: 0.0} m²")
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = m2Finales,
                    onValueChange = { if (it.isEmpty() || it.matches(decimalPattern)) m2Finales = it },
                    label = { Text("Metros Finales (Total)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val valor = m2Finales.toDoubleOrNull()
                    if (valor != null) {
                        onGuardar(valor)
                    }
                },
                enabled = m2Finales.isNotBlank()
            ) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogEditarFolio(
    folio: Folio,
    onDismiss: () -> Unit,
    onSave: (Folio) -> Unit
) {
    var nombre by remember { mutableStateOf(folio.nombreEstablecimiento.orEmpty()) }
    var direccion by remember { mutableStateOf(folio.direccion.orEmpty()) }
    var m2Reportados by remember { mutableStateOf(folio.m2Reportados?.toString().orEmpty()) }
    var m2Finales by remember { mutableStateOf(folio.m2Final?.toString().orEmpty()) }
    var figuras by remember { mutableStateOf(folio.figuras?.toString().orEmpty()) }
    var notas by remember { mutableStateOf(folio.observaciones.orEmpty()) }

    // Opciones predeterminadas
    val tarifasOptions = listOf("1-100", "101-300", "301+")
    val fachadaOptions = listOf("Opción A", "Opción B", "Master", "Sin Opción")

    // Lógica para inicializar mostrando el valor real, aunque no esté en la lista estándar
    var tarifa by remember { mutableStateOf(folio.tarifaTipo ?: "") }
    var tipo by remember { mutableStateOf(folio.tipoFachada ?: "") }
    
    // Si el valor actual no está en la lista y no es vacío, lo agregamos temporalmente para que el dropdown lo muestre
    val currentTarifasOptions = remember(tarifa) {
        if (tarifa.isNotBlank() && tarifa !in tarifasOptions) tarifasOptions + tarifa else tarifasOptions
    }
    
    val currentFachadaOptions = remember(tipo) {
        if (tipo.isNotBlank() && tipo !in fachadaOptions) fachadaOptions + tipo else fachadaOptions
    }

    val estadoOptions = listOf("Coincide", "No coincide", "En validacion", "Validado", "En facturacion", "Facturado", "Pagado", "Cancelado")
    var estado by remember { mutableStateOf(if (estadoOptions.contains(folio.estado)) folio.estado else "Coincide") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar folio ${folio.folioTruper}") },
        text = {
            Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del establecimiento") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))

                DropdownSelector(label = "Tarifa", selected = tarifa, options = currentTarifasOptions, onSelected = { tarifa = it })
                Spacer(Modifier.height(8.dp))

                DropdownSelector(label = "Tipo de fachada", selected = tipo, options = currentFachadaOptions, onSelected = { tipo = it })
                Spacer(Modifier.height(8.dp))
                
                val decimalPattern = remember { Regex("^\\d*\\.?\\d{0,2}$$") }
                
                OutlinedTextField(
                    value = m2Reportados,
                    onValueChange = { if (it.isEmpty() || it.matches(decimalPattern)) m2Reportados = it },
                    label = { Text("m² Solicitados") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = m2Finales,
                    onValueChange = { if (it.isEmpty() || it.matches(decimalPattern)) m2Finales = it },
                    label = { Text("m² Finales (Actualizados)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                
                val intPattern = remember { Regex("^\\d*$") }
                OutlinedTextField(
                    value = figuras,
                    onValueChange = { if (it.isEmpty() || it.matches(intPattern)) figuras = it },
                    label = { Text("Figuras/Cajones") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                DropdownSelector(label = "Estado", selected = estado, options = estadoOptions, onSelected = { estado = it })
                Spacer(Modifier.height(8.dp))
                
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas u observaciones") }, minLines = 2, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val m2R = m2Reportados.toDoubleOrNull()
                val m2F = m2Finales.toDoubleOrNull()
                val figs = figuras.toIntOrNull()
                onSave(
                    folio.copy(
                        nombreEstablecimiento = nombre.ifBlank { null },
                        direccion = direccion.ifBlank { null },
                        tipoFachada = tipo.ifBlank { null },
                        m2Reportados = m2R,
                        m2Final = m2F,
                        figuras = figs,
                        tarifaTipo = tarifa.ifBlank { null },
                        estado = estado.ifBlank { folio.estado },
                        observaciones = notas.ifBlank { null }
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

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

@Composable
fun DialogMensajeCopiable(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = {
                clipboardManager.setText(AnnotatedString(mensaje))
                onDismiss()
            }) { Text("Copiar y cerrar") }
        }
    )
}

private suspend fun generarExcel(context: Context, folios: List<Folio>, uri: Uri): Pair<Boolean, String?> {
    return withContext(Dispatchers.IO) {
        try {
            System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
            System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
            System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")
            
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                ExcelGenerator.generarExcelParaFolios(folios, outputStream)
            }
            Pair(true, null)
        } catch (e: Throwable) {
            e.printStackTrace()
            Pair(false, e.message ?: "Error desconocido")
        }
    }
}
