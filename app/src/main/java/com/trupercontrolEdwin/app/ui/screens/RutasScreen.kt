package com.trupercontrolEdwin.app.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Ruta
import com.trupercontrolEdwin.app.utils.OcrProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectAsState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RutasScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    val rutas by db.rutaDao().getAll().collectAsState(initial = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialogoRuta by remember { mutableStateOf(false) }
    var rutaParaListado by remember { mutableStateOf<Ruta?>(null) }

    val listadoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val rutaSeleccionada = rutaParaListado
        if (uri != null && rutaSeleccionada != null) {
            scope.launch {
                val bitmap = decodeBitmap(context, uri)
                if (bitmap == null) {
                    snackbarHostState.showSnackbar("No se pudo leer la imagen del listado")
                } else {
                    val texto = OcrProcessor.procesarImagen(bitmap)
                    val folios = OcrProcessor.extraerFolios(texto)
                    val notasExtra = if (folios.isNotEmpty()) {
                        "Listado procesado: ${folios.size} folios detectados"
                    } else {
                        "Listado procesado sin coincidencias claras"
                    }
                    val actualizada = rutaSeleccionada.copy(
                        fotoListadoUri = uri.toString(),
                        foliosEsperados = if (folios.isNotEmpty()) folios.joinToString(",") else rutaSeleccionada.foliosEsperados,
                        notas = listOfNotNull(rutaSeleccionada.notas, notasExtra).joinToString("\n").ifBlank { notasExtra }
                    )
                    db.rutaDao().insert(actualizada)
                    snackbarHostState.showSnackbar(notasExtra)
                }
            }
        }
        rutaParaListado = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rutas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogoRuta = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar ruta")
            }
        }
    ) { padding ->
        if (rutas.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No hay rutas registradas todavía")
                Spacer(Modifier.height(12.dp))
                Text("Usa el botón + para crear una nueva")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(rutas) { ruta ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(ruta.nombre, style = MaterialTheme.typography.titleMedium)
                            ruta.fecha?.let { Text("Fecha: $it") }
                            Spacer(Modifier.height(8.dp))
                            if (!ruta.foliosEsperados.isNullOrBlank()) {
                                val lista = ruta.foliosEsperados.split(',').map { it.trim() }.filter { it.isNotEmpty() }
                                AssistChip(
                                    onClick = {},
                                    label = { Text("Folios esperados: ${lista.size}") },
                                    leadingIcon = { Icon(Icons.Default.ListAlt, contentDescription = null) }
                                )
                                if (lista.isNotEmpty()) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(lista.chunked(5).joinToString("\n") { it.joinToString(", ") }, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            ruta.notas?.takeIf { it.isNotBlank() }?.let {
                                Spacer(Modifier.height(8.dp))
                                Text("Notas:\n$it")
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(onClick = { navController.navigate("folios/${ruta.id}") }) {
                                    Text("Abrir folios")
                                }
                                OutlinedButton(onClick = {
                                    rutaParaListado = ruta
                                    listadoLauncher.launch("image/*")
                                }) {
                                    Text(if (ruta.fotoListadoUri == null) "Cargar listado" else "Actualizar listado")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoRuta) {
        DialogNuevaRuta(
            onDismiss = { mostrarDialogoRuta = false },
            onSave = { nombre, fecha, notas ->
                scope.launch {
                    db.rutaDao().insert(Ruta(nombre = nombre, fecha = fecha, notas = notas))
                    mostrarDialogoRuta = false
                    snackbarHostState.showSnackbar("Ruta creada")
                }
            }
        )
    }
}

@Composable
private fun DialogNuevaRuta(
    onDismiss: () -> Unit,
    onSave: (String, String?, String?) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva ruta") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha (opcional)") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = notas,
                    onValueChange = { notas = it },
                    label = { Text("Notas iniciales") },
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nombre.isNotBlank()) onSave(nombre.trim(), fecha.takeIf { it.isNotBlank() }, notas.takeIf { it.isNotBlank() })
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private suspend fun decodeBitmap(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
    return@withContext try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: Exception) {
        null
    }
}
