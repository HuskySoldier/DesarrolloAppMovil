package cl.gymtastic.app.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.data.local.entity.CartItemEntity
import cl.gymtastic.app.util.ServiceLocator
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.gymtastic.app.ui.navigation.goHome
import kotlinx.coroutines.launch

@Composable
fun CartScreen(nav: NavController) {
    val ctx = LocalContext.current
    val cartFlow = remember { ServiceLocator.cart(ctx).observeCart() }
    val items: List<CartItemEntity> by cartFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val total = items.sumOf { it.qty * it.unitPrice }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Carrito", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))

            LazyColumn(Modifier.weight(1f, fill = false)) {
                items(items.size) { i ->
                    val it = items[i]
                    ListItem(
                        headlineContent = { Text("Producto #${it.productId}") },
                        supportingContent = { Text("x${it.qty}  • CLP ${it.unitPrice.toInt()}") }
                    )
                    Divider()
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Total: CLP ${total.toInt()}")
            Spacer(Modifier.height(8.dp))

            Row {
                // 🧹 Botón manual de vaciar
                Button(onClick = { scope.launch { ServiceLocator.cart(ctx).clear() } }) {
                    Text("Vaciar")
                }

                Spacer(Modifier.width(8.dp))


                // Reemplaza el botón Pagar actual por:
                Button(onClick = { nav.navigate("payment") }) {
                    Text("Pagar")
                }


                Spacer(Modifier.width(8.dp))

                // 🏠 Volver al home sin comprar
                Button(onClick = { nav.goHome() }) {
                    Text("Volver al Home")
                }
            }
        }
    }
}
