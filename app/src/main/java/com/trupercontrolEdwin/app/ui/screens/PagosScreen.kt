package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Factura
import com.trupercontrolEdwin.app.data.entities.Pago
import com.trupercontrolEdwin.app.data.model.PagoConFactura
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagosScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var facturasEncontradas by remember { mutableStateOf<List<Factura>>(emptyList()) }
    var facturaSeleccionada by remember { mutableStateOf<Factura?>(null) }

    val pagosConFactura by db.pagoDao().getPagosConFactura().collectAsState(initial = emptyList())

    fun buscarFacturas() {
        if (searchQuery.isNotBlank()) {
            scope.launch {
                facturasEncontradas = db.facturaDao().buscarPorFolioFactura(searchQuery)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Pagos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            
            Text("Registrar Nuevo Pago", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    if (it.length > 2) buscarFacturas()
                },
                label = { Text("Buscar Folio de Factura (ej. EZ123)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (facturasEncontradas.isNotEmpty()) {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(facturasEncontradas) { factura ->
                        ListItem(
                            headlineContent = { Text(factura.folioFactura) },
                            supportingContent = { Text("Total: $${factura.total}") },
                            modifier = Modifier.clickable { 
                                facturaSeleccionada = factura
                                facturasEncontradas = emptyList()
                                searchQuery = factura.folioFactura
                            }
                        )
                    }
                }
            }

            facturaSeleccionada?.let { factura ->
                var montoPago by remember { mutableStateOf("") }

                Spacer(Modifier.height(16.dp))
                Text("Factura: ${factura.folioFactura}", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = montoPago,
                    onValueChange = { montoPago = it },
                    label = { Text("Monto del Pago") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        val monto = montoPago.toDoubleOrNull()
                        if (monto != null) {
                            scope.launch {
                                db.pagoDao().insert(Pago(facturaId = factura.id, monto = monto))
                                facturaSeleccionada = null
                                searchQuery = ""
                            }
                        }
                    },
                    enabled = montoPago.isNotBlank(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Registrar Pago")
                }
            }

            Spacer(Modifier.height(24.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            Text("Historial de Pagos Recientes", style = MaterialTheme.typography.titleLarge)
            LazyColumn {
                items(pagosConFactura) { pagoConFactura ->
                    ListItem(
                        headlineContent = { Text("Pago a Factura: ${pagoConFactura.folioFactura}") },
                        supportingContent = { Text("Monto: $${pagoConFactura.montoPago}") },
                        trailingContent = { Text(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(pagoConFactura.fechaPago))) }
                    )
                }
            }
        }
    }
}
