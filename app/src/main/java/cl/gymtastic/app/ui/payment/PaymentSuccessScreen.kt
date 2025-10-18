package cl.gymtastic.app.ui.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.ui.navigation.NavRoutes

@Composable
fun PaymentSuccessScreen(nav: NavController) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("¡Suscripción activada!", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text("Tu plan quedó asociado a la sede elegida.")
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    nav.navigate(NavRoutes.HOME) { popUpTo(0) }
                }) {
                    Text("Ir al Home")
                }
            }
        }
    }
}
