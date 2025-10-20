package cl.gymtastic.app.ui.checkin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
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
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(nav: NavController) {
    val ctx = LocalContext.current
    // TODO: reemplazar por userId real de Session/DataStore
    val userId = 1L

    val flow = remember { ServiceLocator.attendance(ctx).observe(userId) }
    val list: List<AttendanceEntity> by flow.collectAsStateWithLifecycle(initialValue = emptyList())

    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    // ¿Hay una sesión abierta?
    val hasOpen = remember(list) { list.any { it.checkOutTimestamp == null } }

    val bg = Brush.verticalGradient(
        listOf(cs.primary.copy(alpha = 0.22f), cs.surface)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Check-In", color = cs.onBackground) },
                navigationIcon = {
                    IconButton(onClick = {
                        nav.navigate(NavRoutes.HOME) { launchSingleTop = true }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = cs.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cs.background,
                    titleContentColor = cs.onBackground,
                    navigationIconContentColor = cs.onBackground
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        // 🔁 Reemplazo de FABs por botones “pill”
        floatingActionButton = {
            PillButtonsRow(
                hasOpen = hasOpen,
                onCheckIn = {
                    scope.launch {
                        ServiceLocator.attendance(ctx).checkIn(userId)
                        snackbar.showMessage("Check-In registrado")
                    }
                },
                onCheckOut = {
                    scope.launch {
                        ServiceLocator.attendance(ctx).checkOut(userId)
                        snackbar.showMessage("Check-Out registrado")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(16.dp)
        ) {
            // Resumen / encabezado
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = cs.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.History, contentDescription = null, tint = cs.primary)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Historial de asistencia", style = MaterialTheme.typography.titleMedium)
                        val total = list.size
                        Text(
                            "$total registro${if (total == 1) "" else "s"} • ${if (hasOpen) "Sesión en curso" else "Sin sesión abierta"}",
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (list.isEmpty()) {
                // Vacío elegante
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = cs.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Aún no tienes registros", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text("Usa el botón IN para registrar tu entrada.", color = cs.onSurfaceVariant)
                    }
                }
            } else {
                Spacer(Modifier.height(4.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp) // espacio para los botones
                ) {
                    items(list.size) { i ->
                        val e = list[i]
                        AttendanceCard(e)
                    }
                }
            }
        }
    }
}

/* ---------- Botones “pill” ---------- */

@Composable
private fun PillButtonsRow(
    hasOpen: Boolean,
    onCheckIn: () -> Unit,
    onCheckOut: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        // IN
        Button(
            onClick = onCheckIn,
            enabled = !hasOpen,
            shape = MaterialTheme.shapes.extraLarge,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = cs.primary,
                contentColor = cs.onPrimary,
                disabledContainerColor = cs.surfaceVariant,
                disabledContentColor = cs.onSurfaceVariant
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Filled.Login, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("IN")
        }

        // OUT
        Button(
            onClick = onCheckOut,
            enabled = hasOpen,
            shape = MaterialTheme.shapes.extraLarge,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = cs.primary,
                contentColor = cs.onPrimary,
                disabledContainerColor = cs.surfaceVariant,
                disabledContentColor = cs.onSurfaceVariant
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Filled.Logout, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("OUT")
        }
    }
}

/* ---------- Tarjeta de asistencia ---------- */

@Composable
private fun AttendanceCard(e: AttendanceEntity) {
    val cs = MaterialTheme.colorScheme
    val inTxt = fmt(e.timestamp)
    val outTxt = fmt(e.checkOutTimestamp)
    val inCourse = e.checkOutTimestamp == null
    val dur = durationText(e.timestamp, e.checkOutTimestamp)

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = cs.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Entrada", style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
                StatusPill(inCourse)
            }
            Text(inTxt, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(8.dp))

            Text("Salida", style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
            Text(outTxt, style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            Text("Duración", style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
            Text(dur, style = MaterialTheme.typography.bodyLarge, color = cs.primary)
        }
    }
}

@Composable
private fun StatusPill(inCourse: Boolean) {
    val cs = MaterialTheme.colorScheme
    val label = if (inCourse) "En curso" else "Finalizado"
    val bg = if (inCourse) cs.primary.copy(alpha = 0.12f) else cs.surfaceVariant
    val fg = if (inCourse) cs.primary else cs.onSurfaceVariant

    Surface(
        color = bg,
        contentColor = fg,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

/* ---------- Helpers ---------- */

private fun fmt(ts: Long?): String =
    ts?.let { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it)) } ?: "—"

private fun durationText(start: Long?, end: Long?): String {
    val s = start ?: return "—"
    val e = end ?: System.currentTimeMillis()
    val diff = (e - s).coerceAtLeast(0L)
    val h = TimeUnit.MILLISECONDS.toHours(diff)
    val m = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

/** Helper para snackbars desde coroutines */
private suspend fun SnackbarHostState.showMessage(msg: String) {
    showSnackbar(message = msg, withDismissAction = true)
}
