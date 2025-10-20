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


fun PaymentSuccessScreen(nav: NavController, planActivated: Boolean) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            if (planActivated) "¡Membresía activada! 🎉" else "¡Compra completada! 🛍️",
            style = MaterialTheme.typography.headlineSmall,
            color = cs.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (planActivated)
                "Ya puedes usar Check-In y agendar con Trainers."
            else
                "Gracias por tu compra. Tus productos estarán disponibles en tu retiro/envío.",
            color = cs.onSurfaceVariant
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = { nav.navigate("home") { popUpTo(0) } }) {
            Text("Volver al Home")
        }
    }
}

