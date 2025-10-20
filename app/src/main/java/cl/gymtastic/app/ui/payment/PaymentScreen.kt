package cl.gymtastic.app.ui.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.gymtastic.app.R
import cl.gymtastic.app.data.datastore.MembershipPrefs
import cl.gymtastic.app.data.local.SedesRepo
import cl.gymtastic.app.ui.navigation.Screen
import cl.gymtastic.app.util.ServiceLocator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(nav: NavController) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val cs = MaterialTheme.colorScheme

    // Cambia a true cuando tengas API key configurada en el Manifest
    val useGoogleMap = true

    val metodos = listOf("Débito", "Crédito", "Transferencia", "Efectivo")
    var metodo by remember { mutableStateOf(metodos.first()) }
    var metodoExpanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    // Carrito
    val cartFlow = remember { ServiceLocator.cart(ctx).observeCart() }
    val items by cartFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val total = items.sumOf { it.qty * it.unitPrice }.toInt()

    // Sedes
    val sedes = SedesRepo.sedes
    var sedeExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    val sede = sedes[selectedIndex]
    val sedeLatLng = LatLng(sede.lat, sede.lng)

    // Mapa
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sedeLatLng, 14f)
    }

    // 🔔 Mensaje de bloqueo para el diálogo
    var showBlocked by remember { mutableStateOf<String?>(null) }

    val bg = Brush.verticalGradient(listOf(cs.primary.copy(alpha = 0.22f), cs.surface))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pago", color = cs.onBackground) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = cs.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cs.background,
                    titleContentColor = cs.onBackground,
                    navigationIconContentColor = cs.onBackground
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cs.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth(0.95f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Pago simulado", style = MaterialTheme.typography.headlineSmall)

                        // Resumen
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Productos en carrito: ${items.size}", color = cs.onSurfaceVariant)
                            Text(
                                "Total: CLP $total",
                                style = MaterialTheme.typography.titleMedium,
                                color = cs.primary
                            )
                        }

                        // Método de pago
                        ExposedDropdownMenuBox(
                            expanded = metodoExpanded,
                            onExpandedChange = { metodoExpanded = !metodoExpanded }
                        ) {
                            TextField(
                                value = metodo,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Método de pago") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = metodoExpanded)
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = metodoExpanded,
                                onDismissRequest = { metodoExpanded = false }
                            ) {
                                metodos.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m) },
                                        onClick = { metodo = m; metodoExpanded = false }
                                    )
                                }
                            }
                        }

                        // Sede
                        ExposedDropdownMenuBox(
                            expanded = sedeExpanded,
                            onExpandedChange = { sedeExpanded = !sedeExpanded }
                        ) {
                            TextField(
                                value = "${sede.nombre} • ${sede.direccion}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Sede") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sedeExpanded)
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = sedeExpanded,
                                onDismissRequest = { sedeExpanded = false }
                            ) {
                                sedes.forEachIndexed { idx, s ->
                                    DropdownMenuItem(
                                        text = { Text("${s.nombre} • ${s.direccion}") },
                                        onClick = {
                                            selectedIndex = idx
                                            sedeExpanded = false
                                            scope.launch {
                                                cameraState.animate(
                                                    update = CameraUpdateFactory.newLatLngZoom(
                                                        LatLng(s.lat, s.lng), 14f
                                                    )
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // Mapa (o placeholder si no hay API key)
                        Box(Modifier
                            .fillMaxWidth()
                            .height(180.dp)) {
                            if (useGoogleMap) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraState
                                ) {
                                    Marker(
                                        state = MarkerState(position = sedeLatLng),
                                        title = sede.nombre,
                                        snippet = sede.direccion
                                    )
                                }
                            } else {
                                Image(
                                    painter = painterResource(R.drawable.map_placeholder),
                                    contentDescription = "Mapa sede",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // Acciones
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { nav.popBackStack() },
                                modifier = Modifier.weight(1f)
                            ) { Text("Cancelar") }

                            Button(
                                onClick = {
                                    if (total <= 0) return@Button
                                    loading = true
                                    scope.launch {
                                        try {
                                            // Valida política de compra (evitar contratar con plan vigente)
                                            val canBuy = MembershipPrefs.canPurchaseNewPlan(
                                                ctx,
                                                thresholdDays = 3
                                            )
                                            if (!canBuy) {
                                                showBlocked =
                                                    "Ya tienes un plan activo. Podrás contratar uno nuevo cuando falten 3 días o menos para que termine."
                                                return@launch
                                            }

                                            // Calcula fin del plan (ej.: 30 días)
                                            val planEnd = daysFromNow(days = 30)

                                            // Simular pago: limpiar carrito + habilitar plan y sede con fecha fin
                                            ServiceLocator.cart(ctx).clear()
                                            MembershipPrefs.setActiveWithSede(
                                                ctx = ctx,
                                                id = sede.id,       // 👈 usa el nombre real del parámetro en tu MembershipPrefs
                                                name = sede.nombre,
                                                lat = sede.lat,
                                                lng = sede.lng,
                                                planEndMillis = planEnd
                                            )

                                            nav.navigate(Screen.PaymentSuccess.route) {
                                                launchSingleTop = true
                                            }
                                        } catch (e: Exception) {
                                            nav.navigate(cl.gymtastic.app.ui.navigation.NavRoutes.HOME) {
                                                popUpTo(0)
                                            }
                                        } finally {
                                            loading = false
                                        }
                                    }
                                },
                                enabled = !loading && total > 0,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (loading) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .padding(end = 8.dp)
                                    )
                                }
                                Text(if (loading) "Procesando..." else "Pagar")
                            }
                        }

                        // Diálogo de bloqueo (cuando no se puede contratar)
                        if (showBlocked != null) {
                            AlertDialog(
                                onDismissRequest = { showBlocked = null },
                                confirmButton = {
                                    TextButton(onClick = { showBlocked = null }) { Text("Entendido") }
                                },
                                title = { Text("No puedes contratar ahora") },
                                text = { Text(showBlocked!!) }
                            )
                        }

                        Text(
                            "Tu plan se asociará a la sede seleccionada.",
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/** Utilidad para sumar días desde “ahora” y devolver millis */
private fun daysFromNow(days: Int): Long {
    val now = System.currentTimeMillis()
    return now + days * 24L * 60L * 60L * 1000L
}
