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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repoProvider: (android.content.Context) -> cl.gymtastic.app.data.repository.AuthRepository
) : ViewModel() {
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun login(context: android.content.Context, emailRaw: String, passRaw: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading = true
            error = null
            // Normalización de inputs (debe calzar con AuthRepository)
            val email = emailRaw.trim().lowercase()
            val pass = passRaw.trim()
            val ok = repoProvider(context).login(email, pass)
            loading = false
            if (ok) onSuccess() else error = "Credenciales inválidas"
        }
    }
}

@Composable
fun LoginScreen(nav: NavController) {
    val ctx = LocalContext.current
    val vm = remember { LoginViewModel { ServiceLocator.auth(it) } }

    var email by rememberSaveable { mutableStateOf("admin@gymtastic.cl") }
    var pass by rememberSaveable { mutableStateOf("admin123") }
    var passVisible by rememberSaveable { mutableStateOf(false) }

    // Animación de entrada (Card)
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { show = true }

    // Animación “shake” en error
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(vm.error) {
        if (vm.error != null) {
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

    val cs = MaterialTheme.colorScheme
    val bg = Brush.verticalGradient(listOf(cs.primary.copy(alpha = 0.25f), cs.surface))

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
                colors = CardDefaults.cardColors(containerColor = cs.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color.Black, cs.primary)),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "GymTastic",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.SansSerif,
                                letterSpacing = 2.sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Bienvenido, inicia sesión para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = cs.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = cs.primary,
                            unfocusedBorderColor = cs.onSurface.copy(alpha = 0.4f),
                            cursorColor = cs.primary
                        ),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Password con toggle
                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passVisible = !passVisible }) {
                                Icon(
                                    imageVector = if (passVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (passVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = cs.primary,
                            unfocusedBorderColor = cs.onSurface.copy(alpha = 0.4f),
                            cursorColor = cs.primary
                        ),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    Spacer(Modifier.height(20.dp))

                    // Botón principal
                    Button(
                        onClick = {
                            vm.login(ctx, email, pass) {
                                nav.navigate(NavRoutes.HOME) {
                                    popUpTo(NavRoutes.LOGIN) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        },
                        enabled = !vm.loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        if (vm.loading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(end = 8.dp),
                                color = cs.onPrimary
                            )
                        }
                        Text(if (vm.loading) "Ingresando..." else "Ingresar")
                    }

                    TextButton(
                        onClick = { nav.navigate(NavRoutes.REGISTER) },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text("Crear cuenta", color = cs.primary)
                    }

                    // Error
                    AnimatedVisibility(
                        visible = vm.error != null,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 4 })
                    ) {
                        vm.error?.let { msg ->
                            Text(
                                text = msg,
                                color = cs.error,
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
