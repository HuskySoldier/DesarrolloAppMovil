package cl.gymtastic.app.ui.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.ui.navigation.Screen
import cl.gymtastic.app.util.ServiceLocator
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(nav: NavController) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val cs = MaterialTheme.colorScheme

    // Métodos disponibles
    val metodos = listOf("Débito", "Crédito", "Transferencia", "Efectivo")
    var metodo by remember { mutableStateOf(metodos.first()) }
    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    // Total del carrito
    val cartFlow = remember { ServiceLocator.cart(ctx).observeCart() }
    val items by cartFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val total = items.sumOf { it.qty * it.unitPrice }.toInt()

    // Fondo con gradiente
    val bg = Brush.verticalGradient(
        listOf(cs.primary.copy(alpha = 0.22f), cs.surface)
    )

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
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Pago simulado", style = MaterialTheme.typography.headlineSmall)

                        // Resumen de compra
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Productos en carrito: ${items.size}", color = cs.onSurfaceVariant)
                            Text("Total: CLP $total", style = MaterialTheme.typography.titleMedium, color = cs.primary)
                        }

                        // Selector de método de pago (Material 3)
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = metodo,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Método de pago") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                metodos.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m) },
                                        onClick = {
                                            metodo = m
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // Botones acción
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
                                    loading = true
                                    scope.launch {
                                        // Simular pago: vaciar carrito
                                        ServiceLocator.cart(ctx).clear()
                                        loading = false
                                        nav.navigate(Screen.PaymentSuccess.route) { launchSingleTop = true }
                                    }
                                },
                                enabled = !loading && total > 0,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (loading) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp).padding(end = 8.dp)
                                    )
                                }
                                Text(if (loading) "Procesando..." else "Pagar")
                            }
                        }

                        // Nota
                        Text(
                            "Tus datos no serán procesados realmente. Esta es una simulación.",
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
