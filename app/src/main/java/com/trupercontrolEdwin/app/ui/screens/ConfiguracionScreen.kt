package com.trupercontrolEdwin.app.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.utils.BackupManager
import com.trupercontrolEdwin.app.utils.GoogleDriveManager
import com.trupercontrolEdwin.app.workers.BackupWorker
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val backupManager = remember { BackupManager(context, db) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Estado para guardar el último archivo generado
    var lastBackupFile by remember { mutableStateOf<File?>(null) }
    var publicBackupPath by remember { mutableStateOf<String?>(null) }

    // --- MANEJO DE PREFERENCIAS PARA RESPALDO AUTOMÁTICO ---
    val sharedPreferences = remember { context.getSharedPreferences("backup_prefs", Context.MODE_PRIVATE) }
    var isAutoBackupEnabled by remember { 
        mutableStateOf(sharedPreferences.getBoolean("auto_backup_enabled", true)) 
    }

    // Configuración de Google Sign-In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
    }
    
    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    // Estado para saber si el usuario ha iniciado sesión
    var isSignedIn by remember { mutableStateOf(GoogleSignIn.getLastSignedInAccount(context) != null) }

    // --- LANZADORES DE ACTIVIDADES ---

    // Launcher para el inicio de sesión con Google
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    isSignedIn = true
                    scope.launch { snackbarHostState.showSnackbar("Sesión iniciada: ${account.email}") }
                } catch (e: ApiException) {
                    scope.launch { snackbarHostState.showSnackbar("Error en inicio de sesión: ${e.statusCode}") }
                }
            }
        }
    )

    // Launcher para restaurar desde un archivo local
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            scope.launch {
                val success = backupManager.importarDatos(it)
                if (success) {
                    snackbarHostState.showSnackbar("Datos importados con éxito")
                } else {
                    snackbarHostState.showSnackbar("Error al importar los datos")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración y Respaldo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Sección de Respaldo Automático
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Respaldo Automático", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Se realizará una copia local (y en Drive si hay sesión) todos los Martes y Viernes.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text("Activar Respaldo Automático")
                        Switch(
                            checked = isAutoBackupEnabled,
                            onCheckedChange = { enabled ->
                                isAutoBackupEnabled = enabled
                                with(sharedPreferences.edit()) {
                                    putBoolean("auto_backup_enabled", enabled)
                                    apply()
                                }
                                if (enabled) {
                                    scheduleDailyBackupCheck(context)
                                    scope.launch { snackbarHostState.showSnackbar("Respaldo automático activado") }
                                } else {
                                    WorkManager.getInstance(context).cancelUniqueWork("BackupWorker")
                                    scope.launch { snackbarHostState.showSnackbar("Respaldo automático desactivado") }
                                }
                            }
                        )
                    }
                }
            }

            // Sección de Respaldo Local
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Respaldo Local", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    
                    Button(
                        onClick = { 
                            scope.launch {
                                val file = backupManager.guardarBackupLocalPermanente()
                                if (file != null) {
                                    lastBackupFile = file
                                    publicBackupPath = "Descargas/RutasTruperBackup/${file.name}"
                                    snackbarHostState.showSnackbar("Respaldo creado en: $publicBackupPath")
                                } else {
                                    snackbarHostState.showSnackbar("Error al crear respaldo local")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear Copia Local Ahora")
                    }
                    
                    if (publicBackupPath != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Archivo guardado en:\n$publicBackupPath",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.setDataAndType(Uri.parse("content://downloads/public_downloads"), "*/*")
                                    // Esto intentará abrir la carpeta de descargas
                                    // No siempre funciona en todos los dispositivos Android debido a la fragmentación
                                    // Si falla, intentamos abrir el archivo específico
                                    try {
                                        context.startActivity(Intent.createChooser(intent, "Abrir Descargas"))
                                    } catch (e: Exception) {
                                        // Fallback: abrir el archivo específico
                                        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", lastBackupFile!!)
                                        val fileIntent = Intent(Intent.ACTION_VIEW)
                                        fileIntent.setDataAndType(uri, "application/json") 
                                        fileIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        context.startActivity(Intent.createChooser(fileIntent, "Abrir respaldo"))
                                    }
                                } catch (e: Exception) {
                                    scope.launch { snackbarHostState.showSnackbar("No se puede abrir la ubicación") }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Abrir Ubicación")
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            importLauncher.launch(arrayOf("application/json"))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Restaurar desde Archivo Local")
                    }
                }
            }

            // Sección de Respaldo en la Nube
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Respaldo en la Nube", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))

                    if (!isSignedIn) {
                        Button(
                            onClick = { 
                                googleSignInLauncher.launch(googleSignInClient.signInIntent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Iniciar Sesión con Google")
                        }
                    } else {
                        Button(
                            onClick = {
                                scope.launch {
                                    val account = GoogleSignIn.getLastSignedInAccount(context)
                                    if (account != null) {
                                        // Usamos el método de crear temporal para Drive
                                        val file = backupManager.generarArchivoJsonLocal()
                                        if (file != null) {
                                            val driveManager = GoogleDriveManager(context, account)
                                            val fileId = driveManager.uploadFile(file)
                                            if (fileId != null) {
                                                snackbarHostState.showSnackbar("Respaldo subido con éxito a Drive")
                                            } else {
                                                snackbarHostState.showSnackbar("Error al subir a Drive")
                                            }
                                            file.delete()
                                        } else {
                                            snackbarHostState.showSnackbar("Error al generar archivo local")
                                        }
                                    } else {
                                        isSignedIn = false
                                        snackbarHostState.showSnackbar("Sesión inválida, inicie sesión nuevamente")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar Copia en Google Drive Ahora")
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedButton(
                            onClick = {
                                googleSignInClient.signOut().addOnCompleteListener {
                                    isSignedIn = false
                                    scope.launch { snackbarHostState.showSnackbar("Sesión cerrada") }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar Sesión")
                        }
                    }
                }
            }
        }
    }
}

fun scheduleDailyBackupCheck(context: Context) {
    val workManager = WorkManager.getInstance(context)
    
    // Se ejecuta cada 1 día. El Worker verificará si es Martes o Viernes.
    val backupWorkRequest = PeriodicWorkRequestBuilder<BackupWorker>(1, TimeUnit.DAYS)
        .build()

    workManager.enqueueUniquePeriodicWork(
        "BackupWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        backupWorkRequest
    )
}
