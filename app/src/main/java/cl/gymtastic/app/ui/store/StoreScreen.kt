package cl.gymtastic.app.ui.store

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.data.local.entity.ProductEntity
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ServiceLocator
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun StoreScreen(nav: NavController) {
    val ctx = LocalContext.current
    val merchFlow = remember { ServiceLocator.products(ctx).observeMerch() }
    val merch by merchFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(16.dp)) {
        // 🔙 Botón de volver al Home
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = { nav.navigate(NavRoutes.HOME) }) {
                Text("← Volver al Home")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Tienda", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = { nav.navigate(NavRoutes.CART) }) {
                Text("Carrito")
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(merch.size) { i ->
                val p: ProductEntity = merch[i]
                Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(p.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("CLP ${p.precio.toInt()}")
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                scope.launch { ServiceLocator.cart(ctx).add(p.id, 1, p.precio) }
                            }
                        ) { Text("Agregar al carrito") }
                    }
                }
            }
        }
    }
}
