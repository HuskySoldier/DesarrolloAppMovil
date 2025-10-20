package cl.gymtastic.app.ui.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

private fun daysFromNow(days: Int): Long {
    val now = System.currentTimeMillis()
    return now + days * 24L * 60L * 60L * 1000L
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(nav: NavController) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val cs = MaterialTheme.colorScheme

    val useGoogleMap = true

    val metodos = listOf("Débito", "Crédito", "Transferencia", "Efectivo")
    var metodo by remember { mutableStateOf(metodos.first()) }
    var metodoExpanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    // Carrito
    val cartFlow = remember { ServiceLocator.cart(ctx).observeCart() }
    val items by cartFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val total = items.sumOf { it.qty * it.unitPrice }.toInt()

    // Repo de productos: types + names (nombres reales)
    val productsRepo = remember { ServiceLocator.products(ctx) }
    var types by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }
    var names by remember { mutableStateOf<Map<Long, String>>(emptyMap()) }

    LaunchedEffect(items) {
        val ids = items.map { it.productId }.distinct()
        types = productsRepo.getTypesById(ids)
        names = productsRepo.getNamesById(ids) // ← añade esto a tu repo si aún no existe
    }

    val hasPlan = remember(items, types) { items.any { types[it.productId] == "plan" } }
    val totalPlans = remember(items, types) {
        items.filter { types[it.productId] == "plan" }.sumOf { it.qty * it.unitPrice }.toInt()
    }
    val totalMerch = total - totalPlans

    // Sedes (solo si hay plan)
    val sedes = SedesRepo.sedes
    var sedeExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    val safeIndex = selectedIndex.coerceIn(0, (sedes.size - 1).coerceAtLeast(0))
    val sede = sedes.getOrNull(safeIndex)
    val sedeLatLng = remember(safeIndex) {
        LatLng(sede?.lat ?: -33.45, sede?.lng ?: -70.67) // fallback STGO
    }

    // Mapa
    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sedeLatLng, 14f)
    }

    // Diálogo bloqueo
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
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Pago simulado", style = MaterialTheme.typography.headlineSmall)

                        // Resumen + detalle de ítems con nombres reales
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Items en carrito: ${items.size}", color = cs.onSurfaceVariant)

                            // ▶︎ Lista con nombre real, cantidad y subtotal
                            items.forEach { item ->
                                val nombre = names[item.productId] ?: "Producto #${item.productId}"
                                val subtotal = item.qty * item.unitPrice
                                Text(
                                    text = "• $nombre × ${item.qty} — CLP $subtotal",
                                    color = cs.onSurfaceVariant
                                )
                            }

                            if (hasPlan) Text("Subtotal planes: CLP $totalPlans", color = cs.onSurfaceVariant)
                            if (totalMerch > 0) Text("Subtotal tienda: CLP $totalMerch", color = cs.onSurfaceVariant)
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
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = metodoExpanded) },
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

                        // === Sede / Mapa solo si hay plan ===
                        if (hasPlan) {
                            ExposedDropdownMenuBox(
                                expanded = sedeExpanded,
                                onExpandedChange = { sedeExpanded = !sedeExpanded }
                            ) {
                                TextField(
                                    value = if (sede != null)
                                        "${sede.nombre} • ${sede.direccion}" else "Selecciona una sede",
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

                            // Mapa
                            Box(Modifier.fillMaxWidth().height(180.dp)) {
                                if (useGoogleMap) {
                                    GoogleMap(
                                        modifier = Modifier.fillMaxSize(),
                                        cameraPositionState = cameraState,
                                        uiSettings = MapUiSettings(zoomControlsEnabled = false)
                                    ) {
                                        Marker(
                                            state = MarkerState(position = sedeLatLng),
                                            title = sede?.nombre ?: "Sede",
                                            snippet = sede?.direccion ?: ""
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
                        }

                        // ===== Acciones =====
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
                                    if (total <= 0 || loading) return@Button
                                    loading = true
                                    scope.launch {
                                        try {
                                            if (hasPlan) {
                                                val canBuy = MembershipPrefs.canPurchaseNewPlan(ctx, thresholdDays = 3)
                                                if (!canBuy) {
                                                    val onlyMerchTotal = items
                                                        .filter { types[it.productId] != "plan" }
                                                        .sumOf { it.qty * it.unitPrice }
                                                        .toInt()

                                                    if (onlyMerchTotal > 0) {
                                                        val merchIds = items
                                                            .filter { types[it.productId] != "plan" }
                                                            .map { it.productId }
                                                            .distinct()
                                                        ServiceLocator.cart(ctx).removeByProductIds(merchIds)
                                                        nav.navigate(Screen.PaymentSuccess.withPlan(false)) { launchSingleTop = true }
                                                    } else {
                                                        showBlocked =
                                                            "Ya tienes un plan activo. Podrás contratar uno nuevo cuando falten 3 días o menos para que termine."
                                                    }
                                                    return@launch
                                                }

                                                // Flujo normal con plan permitido
                                                val planEnd = daysFromNow(30)
                                                val s = sede ?: run {
                                                    showBlocked = "Selecciona una sede para asociar tu plan."
                                                    return@launch
                                                }
                                                MembershipPrefs.setActiveWithSede(
                                                    ctx = ctx,
                                                    id = safeIndex,
                                                    name = s.nombre,
                                                    lat = s.lat,
                                                    lng = s.lng,
                                                    planEndMillis = planEnd
                                                )
                                                ServiceLocator.cart(ctx).clear()
                                                nav.navigate(Screen.PaymentSuccess.withPlan(true)) { launchSingleTop = true }
                                            } else {
                                                // Sólo merch
                                                ServiceLocator.cart(ctx).clear()
                                                nav.navigate(Screen.PaymentSuccess.withPlan(false)) { launchSingleTop = true }
                                            }
                                        } catch (e: Exception) {
                                            nav.navigate(Screen.Home.route) { popUpTo(0) }
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

                        if (hasPlan) {
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

    if (showBlocked != null) {
        AlertDialog(
            onDismissRequest = { showBlocked = null },
            confirmButton = { TextButton(onClick = { showBlocked = null }) { Text("Entendido") } },
            title = { Text("No puedes contratar ahora") },
            text = { Text(showBlocked!!) }
        )
    }
}
