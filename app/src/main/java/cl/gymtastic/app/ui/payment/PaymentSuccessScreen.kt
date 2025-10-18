package cl.gymtastic.app.ui.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.ui.navigation.goHome

@Composable
fun PaymentSuccessScreen(nav: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Gracias por tu compra!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Tu pedido ha sido procesado correctamente.")

            Spacer(Modifier.height(24.dp))
            Row {
                Button(onClick = { nav.goHome() }) {
                    Text("Volver al Home")
                }
                Spacer(Modifier.width(12.dp))
                // Si luego tienes “Pedidos”, puedes navegar ahí:
                // Button(onClick = { nav.navigate(Screen.Orders.route) }) { Text("Ver pedidos") }
            }
        }
    }
}
