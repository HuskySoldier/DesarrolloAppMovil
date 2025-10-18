package cl.gymtastic.app.ui.checkin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import cl.gymtastic.app.data.local.entity.AttendanceEntity
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ServiceLocator
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(nav: NavController) {
    val ctx = LocalContext.current
    val userId = 1L // TODO: vincular a DataStore real
    val flow = remember { ServiceLocator.attendance(ctx).observe(userId) }
    val list: List<AttendanceEntity> by flow.collectAsStateWithLifecycle(initialValue = emptyList())
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val hasOpen = remember(list) { list.any { it.checkOutTimestamp == null } }

    fun fmt(ts: Long?): String =
        ts?.let { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(it)) } ?: "—"

    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(onClick = { nav.navigate(NavRoutes.HOME) }) {
                    Text("← Volver al Home")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            ServiceLocator.attendance(ctx).checkIn(userId)
                            snackbar.showMessage("Check-In registrado")
                        }
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    // Deshabilita IN si ya hay un registro abierto
                    containerColor = if (hasOpen) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
                ) { Text("IN") }

                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            ServiceLocator.attendance(ctx).checkOut(userId)
                            snackbar.showMessage("Check-Out registrado")
                        }
                    },
                    // Deshabilita OUT si no hay registro abierto
                    containerColor = if (!hasOpen) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
                ) { Text("OUT") }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Historial de Asistencia", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))

            LazyColumn {
                items(list.size) { i ->
                    val e = list[i]
                    ListItem(
                        headlineContent = {
                            Text("Entrada: ${fmt(e.timestamp)}")
                        },
                        supportingContent = {
                            Text("Salida: ${fmt(e.checkOutTimestamp)}")
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

/** Helper para snackbars desde coroutines */
private suspend fun SnackbarHostState.showMessage(msg: String) {
    showSnackbar(message = msg, withDismissAction = true)
}
