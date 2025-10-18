package cl.gymtastic.app.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(nav: NavController) {
    val ctx = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf<String?>(null) }
    var msgIsError by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    // Animación de entrada
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { show = true }

    // Animación de “shake” al mostrar error
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(msg, msgIsError) {
        if (msg != null && msgIsError) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 500
                    (-14f) at 50 using LinearEasing
                    (14f) at 100 using LinearEasing
                    (-10f) at 150 using LinearEasing
                    (10f) at 200 using LinearEasing
                    (-6f) at 250 using LinearEasing
                    (6f) at 300 using LinearEasing
                    (-3f) at 350 using LinearEasing
                    (3f) at 400 using LinearEasing
                    (0f) at 500 using LinearEasing
                }
            )
        }
    }

    // Fondo con gradiente suave
    val bg = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
            MaterialTheme.colorScheme.surface
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = show,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 3 })
        ) {
            Card(
                modifier = Modifier
                    .offset(x = shakeOffset.value.dp)
                    .fillMaxWidth(0.9f)
                    .shadow(8.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Crear cuenta",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Completa tus datos para registrarte",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))

                    // Campos centrados
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Password (≥ 6)") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    Spacer(Modifier.height(20.dp))

                    // Botón Registrar con loading
                    Button(
                        onClick = {
                            // Validación básica
                            when {
                                !email.contains("@") -> {
                                    msgIsError = true
                                    msg = "Ingresa un email válido"
                                }
                                pass.length < 6 -> {
                                    msgIsError = true
                                    msg = "La contraseña debe tener al menos 6 caracteres"
                                }
                                name.isBlank() -> {
                                    msgIsError = true
                                    msg = "Ingresa tu nombre"
                                }
                                else -> {
                                    loading = true
                                    msg = null
                                    scope.launch {
                                        val ok = ServiceLocator.auth(ctx).register(email, pass, name)
                                        loading = false
                                        if (ok) {
                                            msgIsError = false
                                            msg = "✅ Registrado: ahora puedes iniciar sesión"
                                        } else {
                                            msgIsError = true
                                            msg = "Este email ya existe"
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(end = 8.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Text(if (loading) "Guardando..." else "Registrar")
                    }

                    TextButton(
                        onClick = { nav.popBackStack() },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text("Volver")
                    }

                    // Mensaje (error o éxito) con animación
                    AnimatedVisibility(
                        visible = msg != null,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 4 })
                    ) {
                        msg?.let { text ->
                            val color = if (msgIsError) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                            Text(
                                text = text,
                                color = color,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
