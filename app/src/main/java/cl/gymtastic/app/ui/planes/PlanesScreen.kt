package cl.gymtastic.app.ui.planes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cl.gymtastic.app.data.datastore.MembershipPrefs
import cl.gymtastic.app.data.local.entity.ProductEntity
import cl.gymtastic.app.ui.navigation.Screen   // ✅ usa Screen.* en vez de NavRoutes
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanesScreen(nav: NavController) {
    val ctx = LocalContext.current
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    // ✅ requiere repo/DAO con observePlanes()
    val planesFlow = remember { ServiceLocator.products(ctx).observePlanes() }
    val planes by planesFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    // Estado de membresía
    val membership by remember { MembershipPrefs.observe(ctx) }
        .collectAsStateWithLifecycle(initialValue = MembershipPrefs.State())

    val remainingDays: Long? = remember(membership.planEndMillis) {
        membership.planEndMillis?.let { end ->
            val diff = end - System.currentTimeMillis()
            if (diff <= 0) 0L else TimeUnit.MILLISECONDS.toDays(diff)
        }
    }

    // Política de compra
    val canBuy = !membership.hasActivePlan || ((remainingDays ?: 0L) <= 3L)

    val bg = Brush.verticalGradient(listOf(cs.primary.copy(alpha = 0.20f), cs.surface))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planes", color = cs.onBackground) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) { // ✅ back real
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
        Column(
            Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text("Elige tu plan", style = MaterialTheme.typography.titleMedium, color = cs.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))

            // Banner si aún faltan >3 días
            if (membership.hasActivePlan && (remainingDays ?: 0L) > 3L) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = cs.surfaceVariant),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Plan activo", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Podrás contratar otro plan cuando falten 3 días o menos para que termine. " +
                                    "Días restantes: ${remainingDays ?: "—"}",
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(planes.size) { idx ->
                    val p: ProductEntity = planes[idx]
                    val unitPrice = p.precio.toInt() // ✅ carrito usa Int

                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = cs.surface),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                if (!canBuy) {
                                    scope.launch {
                                        snackbar.showSnackbar(
                                            "Ya tienes un plan activo. Disponible cuando falten ≤ 3 días. " +
                                                    "Restan: ${remainingDays ?: "—"} día(s)."
                                        )
                                    }
                                    return@clickable
                                }
                                scope.launch {
                                    ServiceLocator.cart(ctx).add(p.id, 1, unitPrice) // ✅ Int
                                    snackbar.showSnackbar("Agregado: ${p.nombre}")
                                    nav.navigate(Screen.Payment.route) { launchSingleTop = true } // ✅ ruta correcta
                                }
                            }
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(p.nombre, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(6.dp))
                            Text("CLP ${unitPrice}", style = MaterialTheme.typography.titleMedium, color = cs.primary)
                            Spacer(Modifier.height(12.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            if (!canBuy) {
                                                snackbar.showSnackbar(
                                                    "No puedes agregar planes aún. Restan ${remainingDays ?: "—"} día(s)."
                                                )
                                                return@launch
                                            }
                                            ServiceLocator.cart(ctx).add(p.id, 1, unitPrice) // ✅ Int
                                            snackbar.showSnackbar("Agregado al carrito")
                                        }
                                    },
                                    enabled = canBuy,
                                    modifier = Modifier.weight(1f)
                                ) { Text("Agregar") }

                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (!canBuy) {
                                                snackbar.showSnackbar(
                                                    "No puedes contratar aún. Restan ${remainingDays ?: "—"} día(s)."
                                                )
                                                return@launch
                                            }
                                            ServiceLocator.cart(ctx).add(p.id, 1, unitPrice) // ✅ Int
                                            nav.navigate(Screen.Payment.route) { launchSingleTop = true } // ✅
                                        }
                                    },
                                    enabled = canBuy,
                                    modifier = Modifier.weight(1f)
                                ) { Text("Contratar") }
                            }
                        }
                    }
                }
            }
        }
    }
}
