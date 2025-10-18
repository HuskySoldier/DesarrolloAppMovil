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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.gymtastic.app.data.local.entity.ProductEntity
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanesScreen(nav: NavController) {
    val ctx = LocalContext.current
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val planesFlow = remember { ServiceLocator.products(ctx).observePlanes() }
    val planes by planesFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    val bg = Brush.verticalGradient(listOf(cs.primary.copy(alpha = 0.20f), cs.surface))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planes", color = cs.onBackground) },
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
        Column(
            Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text("Elige tu plan", style = MaterialTheme.typography.titleMedium, color = cs.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(planes.size) { idx ->
                    val p: ProductEntity = planes[idx]

                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = cs.surface),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                // 👉 Al tocar la card también agrega y va a pagar
                                scope.launch {
                                    ServiceLocator.cart(ctx).add(p.id, 1, p.precio)
                                    snackbar.showSnackbar("Agregado: ${p.nombre}")
                                    nav.navigate(NavRoutes.PAYMENT) { launchSingleTop = true }
                                }
                            }
                    ) {
                        Column(Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(p.nombre, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(6.dp))
                            Text("CLP ${p.precio.toInt()}", style = MaterialTheme.typography.titleMedium, color = cs.primary)
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            ServiceLocator.cart(ctx).add(p.id, 1, p.precio)
                                            snackbar.showSnackbar("Agregado al carrito")
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) { Text("Agregar") }

                                Button(
                                    onClick = {
                                        scope.launch {
                                            ServiceLocator.cart(ctx).add(p.id, 1, p.precio)
                                            nav.navigate(NavRoutes.PAYMENT) { launchSingleTop = true }
                                        }
                                    },
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
