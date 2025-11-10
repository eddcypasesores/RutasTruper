package com.trupercontrolEdwin.app.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.utils.CalculoRotulacion
import com.trupercontrolEdwin.app.utils.ExcelGenerator
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

    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialogoCambio by remember { mutableStateOf(false) }
    var textoCambio by remember { mutableStateOf("") }
    var folioEnEdicion by remember { mutableStateOf<Folio?>(null) }
    var mensajeValidacion by remember { mutableStateOf<Folio?>(null) }

    val excelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                val ok = generarExcel(context, foliosFiltrados, it)
                if (ok) {
                    snackbarHostState.showSnackbar("Excel generado correctamente")
                } else {
                    snackbarHostState.showSnackbar("Error al generar Excel")
                }
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
                    IconButton(onClick = { /* Lógica para agregar un folio manualmente si se desea */ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar Folio")
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { mostrarDialogoCambio = true }) {
                    Text("Registrar cambio")
                }
                Button(onClick = {
                    if (foliosFiltrados.isEmpty()) {
                        scope.launch { snackbarHostState.showSnackbar("No hay folios para generar Excel") }
                    } else {
                        excelLauncher.launch("facturacion_rotulaciones.xlsx")
                    }
                }) {
                    Text("Generar Excel")
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(Modifier.fillMaxSize()) {
                items(foliosFiltrados) { folio ->
                    FolioCard(
                        folio = folio,
                        onEditar = { folioEnEdicion = folio },
                        onGenerarValidacion = { mensajeValidacion = folio },
                        onFacturas = { navController.navigate("facturas/${folio.id}") },
                        onMarcarValidado = {
                            scope.launch {
                                db.folioDao().update(folio.copy(estado = "Validado"))
                                snackbarHostState.showSnackbar("Folio ${folio.folioTruper} validado")
                            }
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogoCambio) {
        DialogCambioFolio(
            onDismiss = { mostrarDialogoCambio = false },
            onProcesar = { texto ->
                scope.launch {
                    val cambio = FolioParser.parseCambio(texto)
                    if (cambio == null) {
                        snackbarHostState.showSnackbar("No se pudo interpretar el texto del cambio")
                    } else {
                        val folio = db.folioDao().findByFolioTruper(cambio.folio)
                        if (folio == null) {
                            snackbarHostState.showSnackbar("No existe un folio ${cambio.folio} en esta ruta")
                        } else {
                            val actualizado = folio.copy(
                                m2Reportados = cambio.metrosSolicitud ?: folio.m2Reportados,
                                m2Final = cambio.metrosFinales ?: folio.m2Final,
                                cambioTexto = texto,
                                observaciones = listOfNotNull(folio.observaciones, cambio.comentarios)
                                    .filter { it.isNotBlank() }
                                    .joinToString("\n")
                                    .ifBlank { null },
                                estado = "Cambio reportado"
                            )
                            db.folioDao().update(actualizado)
                            snackbarHostState.showSnackbar("Cambio registrado para folio ${folio.folioTruper}")
                        }
                    }
                    mostrarDialogoCambio = false
                    textoCambio = ""
                }
            },
            textoInicial = textoCambio,
            onTextoChange = { textoCambio = it }
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

    if (mensajeValidacion != null) {
        val mensaje = FolioParser.generarMensajeValidacion(mensajeValidacion!!, null)
        DialogMensajeValidacion(
            mensaje = mensaje,
            onDismiss = { mensajeValidacion = null }
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FolioCard(
    folio: Folio,
    onEditar: () -> Unit,
    onGenerarValidacion: () -> Unit,
    onFacturas: () -> Unit,
    onMarcarValidado: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Folio TRUPER: ${folio.folioTruper}", style = MaterialTheme.typography.titleMedium)
            folio.nombreEstablecimiento?.let { Text("Cliente: $it") }
            folio.direccion?.let { Text("Dirección: $it") }

            Spacer(Modifier.height(8.dp))
            val metros = folio.m2Reportados ?: 0.0
            val finales = folio.m2Final ?: metros
            val figuras = folio.figuras ?: 0
            val tarifa = folio.tarifaTipo ?: "1-100"
            val (subtotal, iva, total) = CalculoRotulacion.calcular(finales, tarifa, figuras)
            Text("m² solicitud: ${"%.2f".format(metros)}")
            Text("m² finales: ${"%.2f".format(finales)}")
            Text("Figuras: $figuras | Tarifa: $tarifa")
            Text("Subtotal: $${ "%,.2f".format(subtotal)} | IVA: $${ "%,.2f".format(iva)} | Total: $${ "%,.2f".format(total)}")
            Spacer(Modifier.height(8.dp))
            Text("Estado: ${folio.estado}", style = MaterialTheme.typography.titleSmall)
            folio.observaciones?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(4.dp))
                Text("Notas: $it")
            }
            Spacer(Modifier.height(12.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onEditar) { Text("Editar") }
                OutlinedButton(onClick = onGenerarValidacion) { Text("Validación") }
                OutlinedButton(onClick = onMarcarValidado) { Text("Marcar Validado") }
                TextButton(onClick = onFacturas) { Text("Facturas") }
            }
        }
    }
}

@Composable
private fun DialogCambioFolio(
    onDismiss: () -> Unit,
    onProcesar: (String) -> Unit,
    textoInicial: String,
    onTextoChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar cambio de metros") },
        text = {
            OutlinedTextField(
                value = textoInicial,
                onValueChange = onTextoChange,
                label = { Text("Texto del reporte") },
                minLines = 6
            )
        },
        confirmButton = {
            TextButton(onClick = { onProcesar(textoInicial) }) { Text("Procesar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DialogEditarFolio(
    folio: Folio,
    onDismiss: () -> Unit,
    onSave: (Folio) -> Unit
) {
    var nombre by remember { mutableStateOf(folio.nombreEstablecimiento.orEmpty()) }
    var direccion by remember { mutableStateOf(folio.direccion.orEmpty()) }
    var tipo by remember { mutableStateOf(folio.tipoFachada.orEmpty()) }
    var m2Reportados by remember { mutableStateOf(folio.m2Reportados?.toString().orEmpty()) }
    var m2Finales by remember { mutableStateOf(folio.m2Final?.toString().orEmpty()) }
    var figuras by remember { mutableStateOf(folio.figuras?.toString().orEmpty()) }
    var tarifa by remember { mutableStateOf(folio.tarifaTipo ?: "1-100") }
    var estado by remember { mutableStateOf(folio.estado) }
    var notas by remember { mutableStateOf(folio.observaciones.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar folio ${folio.folioTruper}") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del establecimiento") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo de fachada") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = m2Reportados, onValueChange = { m2Reportados = it }, label = { Text("m² solicitados") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = m2Finales, onValueChange = { m2Finales = it }, label = { Text("m² finales") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = figuras, onValueChange = { figuras = it }, label = { Text("Figuras/Cajones") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = tarifa, onValueChange = { tarifa = it }, label = { Text("Tarifa (1-100 / 101-300 / 301+)") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas u observaciones") }, minLines = 2)
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
                        tarifaTipo = tarifa.ifBlank { "1-100" },
                        estado = estado.ifBlank { folio.estado },
                        observaciones = notas.ifBlank { null }
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun DialogMensajeValidacion(
    mensaje: String,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Mensaje de validación") },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = {
                clipboardManager.setText(AnnotatedString(mensaje))
                onDismiss()
            }) { Text("Copiar y cerrar") }
        }
    )
}

private suspend fun generarExcel(context: Context, folios: List<Folio>, uri: Uri): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                ExcelGenerator.generarExcelParaFolios(folios, outputStream)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
