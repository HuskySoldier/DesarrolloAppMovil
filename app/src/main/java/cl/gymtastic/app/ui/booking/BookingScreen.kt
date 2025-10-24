package cl.gymtastic.app.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    nav: NavController,
    windowSizeClass: WindowSizeClass // <-- PARÁMETRO AÑADIDO
) {
    val ctx = LocalContext.current
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    // Trainers desde Room
    val trainersFlow = remember { ServiceLocator.trainers(ctx).observeAll() }
    val trainers by trainersFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    // Próximos 7 días
    val dateOptions = remember {
        (0..6).map { plus ->
            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, plus) }
            Triple(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        }
    }
    var selectedDateIndex by remember { mutableStateOf(0) }

    // Horarios predefinidos
    val timeOptions = listOf("07:00", "09:00", "12:00", "15:00", "18:00", "20:00")
    var selectedTimeIndex by remember { mutableStateOf(2) } // 12:00

    // Dropdown states
    var trainerExpanded by remember { mutableStateOf(false) }
    var dateExpanded by remember { mutableStateOf(false) }
    var timeExpanded by remember { mutableStateOf(false) }
    var selectedTrainerIndex by remember { mutableStateOf(0) }

    // Loading para confirmar
    var loading by remember { mutableStateOf(false) }

    // Fondo
    val bg = Brush.verticalGradient(listOf(cs.primary.copy(alpha = 0.20f), cs.surface))

    // Reacciona al tamaño de pantalla
    val widthSizeClass = windowSizeClass.widthSizeClass
    val isCompact = widthSizeClass == WindowWidthSizeClass.Compact
    val cardModifier = if (isCompact) {
        Modifier.fillMaxWidth(0.95f)
    } else {
        Modifier.width(550.dp) // Ancho fijo para tablets
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Trainer", color = cs.onBackground) },
                navigationIcon = {
                    IconButton(onClick = { nav.navigate(NavRoutes.HOME) { launchSingleTop = true } }) {
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
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = cardModifier // <-- APLICAMOS MODIFICADOR
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Reserva tu sesión personalizada", style = MaterialTheme.typography.headlineSmall)
                    Text("Elige entrenador, fecha y horario.", style = MaterialTheme.typography.bodyMedium, color = cs.onSurfaceVariant)

                    // === Trainer ===
                    ExposedDropdownMenuBox(
                        expanded = trainerExpanded,
                        onExpandedChange = { trainerExpanded = !trainerExpanded }
                    ) {
                        val trainerLabel =
                            if (trainers.isNotEmpty()) trainers.getOrNull(selectedTrainerIndex)?.nombre ?: "Entrenador"
                            else "Cargando entrenadores…"

                        TextField(
                            value = trainerLabel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Entrenador") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = trainerExpanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = trainerExpanded,
                            onDismissRequest = { trainerExpanded = false }
                        ) {
                            trainers.forEachIndexed { idx, t ->
                                DropdownMenuItem(
                                    text = { Text(t.nombre) },
                                    onClick = {
                                        selectedTrainerIndex = idx
                                        trainerExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // === Fecha ===
                    ExposedDropdownMenuBox(
                        expanded = dateExpanded,
                        onExpandedChange = { dateExpanded = !dateExpanded }
                    ) {
                        val (y, m, d) = dateOptions[selectedDateIndex]
                        val dateLabel = "%02d/%02d/%d".format(d, (m + 1), y)

                        TextField(
                            value = dateLabel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fecha") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dateExpanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = dateExpanded,
                            onDismissRequest = { dateExpanded = false }
                        ) {
                            dateOptions.forEachIndexed { idx, triple ->
                                val (yy, mm, dd) = triple
                                DropdownMenuItem(
                                    text = { Text("%02d/%02d/%d".format(dd, (mm + 1), yy)) },
                                    onClick = {
                                        selectedDateIndex = idx
                                        dateExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // === Hora ===
                    ExposedDropdownMenuBox(
                        expanded = timeExpanded,
                        onExpandedChange = { timeExpanded = !timeExpanded }
                    ) {
                        val timeLabel = timeOptions[selectedTimeIndex]
                        TextField(
                            value = timeLabel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Horario") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeExpanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = timeExpanded,
                            onDismissRequest = { timeExpanded = false }
                        ) {
                            timeOptions.forEachIndexed { idx, t ->
                                DropdownMenuItem(
                                    text = { Text(t) },
                                    onClick = {
                                        selectedTimeIndex = idx
                                        timeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // === Botones ===
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { nav.popBackStack() },
                            modifier = Modifier.weight(1f)
                        ) { Text("Cancelar") }

                        Button(
                            onClick = {
                                if (trainers.isEmpty()) return@Button
                                loading = true

                                // Etiquetas para snackbar
                                val (yy, mm, dd) = dateOptions[selectedDateIndex]
                                val dateLabel = "%02d/%02d/%d".format(dd, (mm + 1), yy)
                                val timeLabel = timeOptions[selectedTimeIndex]

                                scope.launch {
                                    try {
                                        val trainerId = trainers[selectedTrainerIndex].id
                                        val millis = combineDateAndTime(
                                            date = dateOptions[selectedDateIndex],
                                            time = timeOptions[selectedTimeIndex]
                                        )

                                        ServiceLocator.bookings(ctx).create(
                                            uid = 1L,           // TODO: reemplazar por userId real desde DataStore
                                            trainerId = trainerId,
                                            fechaHora = millis
                                        )

                                        snackbar.showSnackbar("Reserva creada para $timeLabel del $dateLabel")
                                        // nav.popBackStack() // opcional

                                    } catch (e: Exception) {
                                        snackbar.showSnackbar("Error al crear la reserva: ${e.message ?: "desconocido"}")
                                    } finally {
                                        loading = false
                                    }
                                }
                            },
                            enabled = trainers.isNotEmpty() && !loading,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (loading) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp).padding(end = 8.dp)
                                )
                            }
                            Text(if (loading) "Guardando..." else "Confirmar")
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Recibirás un recordatorio local antes de tu sesión.",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/** Convierte (año, mes, día) + "HH:mm" a millis */
private fun combineDateAndTime(date: Triple<Int, Int, Int>, time: String): Long {
    val (year, month, day) = date           // month es 0-based
    val (hh, mm) = time.split(":").map { it.toInt() }
    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)          // 0 = Enero
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hh)
        set(Calendar.MINUTE, mm)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return cal.timeInMillis
}
