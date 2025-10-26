package cl.gymtastic.app.ui.auth

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import cl.gymtastic.app.data.repository.AuthRepository
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch


class LoginViewModel(

    // Acepta una función que provea el AuthRepository
    private val repoProvider: (context: android.content.Context) -> AuthRepository
) : ViewModel() {
    // Estado de carga (true mientras se verifica el login)
    var loading by mutableStateOf(false)
        private set // Solo modificable desde el ViewModel

    // Estado de error (String con mensaje si falla, null si no hay error)
    var error by mutableStateOf<String?>(null)
        private set // Solo modificable desde el ViewModel



    // Función llamada desde la UI para intentar iniciar sesión
    fun login(context: android.content.Context, emailRaw: String, passRaw: String, onSuccess: () -> Unit) {
        // Lanzar coroutine para operaciones asíncronas (llamada al repo)
        viewModelScope.launch {
            loading = true // Inicia estado de carga
            error = null   // Limpia errores previos
            // Normaliza email y contraseña (quitar espacios, minúsculas para email)
            val email = emailRaw.trim().lowercase()
            val pass = passRaw.trim()
            // Llama al AuthRepository (obtenido via repoProvider) para verificar credenciales
            val loginSuccessful = repoProvider(context).login(email, pass)
            loading = false // Finaliza estado de carga
            if (loginSuccessful) {
                onSuccess() // Llama a la lambda si el login fue exitoso (para navegar)
            } else {
                error = "Credenciales inválidas" // Establece mensaje de error si falló
            }
        }
    }
    fun clearError() {
        error = null
    }
}

// -----------------------------
// Pantalla de Login
// -----------------------------
@SuppressLint("UnrememberedMutableState") // Justificado si el estado se maneja con ViewModel/rememberSaveable
@Composable
fun LoginScreen(
    nav: NavController,
    windowSizeClass: WindowSizeClass // Recibe WindowSizeClass para diseño adaptativo
) {
    // --- Preparativos ---
    val ctx = LocalContext.current // Contexto actual
    // Crea y recuerda la instancia del ViewModel
    val vm: LoginViewModel = remember { LoginViewModel { ServiceLocator.auth(it) } }

    // --- Estado de UI / Campos del Formulario ---
    // Usamos rememberSaveable para que el texto sobreviva a cambios de configuración (rotación)
    var email by rememberSaveable { mutableStateOf("admin@gymtastic.cl") } // Valor inicial para pruebas
    var pass by rememberSaveable { mutableStateOf("admin123") } // Valor inicial para pruebas
    var passVisible by rememberSaveable { mutableStateOf(false) } // Estado para mostrar/ocultar contraseña

    val keyboard = LocalSoftwareKeyboardController.current // Controlador para ocultar teclado

    // --- Animaciones ---
    // Estado para la animación de entrada de la tarjeta
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { show = true } // Inicia la animación al entrar a la pantalla

    // Estado animable para el efecto "shake" de la tarjeta en caso de error
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(vm.error) { // Se ejecuta cada vez que vm.error cambia
        if (vm.error != null) { // Si hay un error...
            shakeOffset.snapTo(0f) // Resetea la posición
            // Anima el desplazamiento horizontal (efecto shake)
            shakeOffset.animateTo( /* ... keyframes ... */
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

    // --- Estilo y Layout ---
    val cs = MaterialTheme.colorScheme // Colores del tema
    // Define el fondo con gradiente
    val bg = Brush.verticalGradient(listOf(cs.primary.copy(alpha = 0.22f), cs.surface))
    // Estado para scroll interno de la tarjeta
    val scroll = rememberScrollState()

    // --- Validaciones (Estados Derivados) ---
    // Se recalculan automáticamente si 'email' o 'pass' cambian
    val isEmailValid by derivedStateOf { email.contains("@") && email.contains(".") && email.length >= 6 }
    val isPassValid by derivedStateOf { pass.length >= 6 }

    // --- Función de Acción ---
    // Función que se llama al presionar el botón Ingresar o Done en teclado
    fun doLogin() {
        keyboard?.hide() // Oculta el teclado
        // Llama a la función login del ViewModel
        vm.login(ctx, email, pass) {
            // onSuccess lambda: Navega a Home si el login es exitoso
            nav.navigate(NavRoutes.HOME) {
                popUpTo(NavRoutes.LOGIN) { inclusive = true } // Limpia pantalla Login del backstack
                launchSingleTop = true // Evita múltiples instancias de Home
            }
        }
    }

    // -----------------------------
    // LAYOUT principal (Composable UI)
    // -----------------------------
    Box( // Contenedor principal que centra la tarjeta
        modifier = Modifier.fillMaxSize().background(bg).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Muestra la tarjeta con animación de entrada
        AnimatedVisibility(visible = show, enter = fadeIn() + slideInVertically { it / 3 }, exit = fadeOut() + slideOutVertically { it / 3 }) {

            // Define el modificador de la tarjeta según el ancho de pantalla
            val widthSizeClass = windowSizeClass.widthSizeClass
            val cardModifier = if (widthSizeClass == WindowWidthSizeClass.Compact) {
                Modifier.offset(x = shakeOffset.value.dp).fillMaxWidth(0.92f).shadow(10.dp, RoundedCornerShape(28.dp))
            } else {
                Modifier.offset(x = shakeOffset.value.dp).width(480.dp).shadow(10.dp, RoundedCornerShape(28.dp))
            }

            // La tarjeta principal del formulario
            Card(
                modifier = cardModifier, // Aplica modificador con shake y ancho adaptativo
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface)
            ) {
                // Columna interna con scroll para el contenido del formulario
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 24.dp).verticalScroll(scroll),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // --- Encabezado ---
                    Box( // Box con fondo gradiente para el título
                        modifier = Modifier.fillMaxWidth().background(brush = Brush.horizontalGradient(listOf(cs.primary, cs.secondary)), shape = RoundedCornerShape(20.dp)).padding(vertical = 14.dp, horizontal = 12.dp)
                    ) {
                        Text(text = "GymTastic", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif, letterSpacing = 1.5.sp), color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("Bienvenido 👋\nInicia sesión para continuar", style = MaterialTheme.typography.bodyMedium, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(24.dp))

                    // --- Campo Email ---
                    OutlinedTextField(
                        value = email, onValueChange = { email = it; vm.clearError() }, // Limpia error al escribir
                        label = { Text("Email") }, singleLine = true, leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        isError = (email.isNotBlank() && !isEmailValid) || vm.error != null, // Marca error si formato inválido O si vm.error existe
                        supportingText = { if (email.isNotBlank() && !isEmailValid) Text("Ingresa un email válido") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = cs.primary, unfocusedBorderColor = cs.onSurface.copy(alpha = 0.3f), cursorColor = cs.primary),
                        modifier = Modifier.fillMaxWidth(0.94f)
                    )
                    Spacer(Modifier.height(12.dp))

                    // --- Campo Contraseña ---
                    OutlinedTextField(
                        value = pass, onValueChange = { pass = it; vm.clearError() }, // Limpia error al escribir
                        label = { Text("Contraseña") }, singleLine = true, leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = { IconButton(onClick = { passVisible = !passVisible }) { Icon(imageVector = if (passVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = if (passVisible) "Ocultar" else "Mostrar") } },
                        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { if (!vm.loading && isEmailValid && isPassValid) doLogin() }),
                        isError = (pass.isNotBlank() && !isPassValid) || vm.error != null, // Marca error si largo inválido O si vm.error existe
                        supportingText = { if (pass.isNotBlank() && !isPassValid) Text("Mínimo 6 caracteres") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = cs.primary, unfocusedBorderColor = cs.onSurface.copy(alpha = 0.3f), cursorColor = cs.primary),
                        modifier = Modifier.fillMaxWidth(0.94f)
                    )
                    Spacer(Modifier.height(8.dp)) // Espacio reducido antes del mensaje de error/botones

                    // ---  NUEVO: Mensaje de Error ---
                    // Se muestra/oculta con animación si vm.error tiene un valor
                    AnimatedVisibility(visible = vm.error != null) {
                        Text(
                            text = vm.error ?: "", // Muestra el mensaje de error del ViewModel
                            color = cs.error, // Color rojo para errores
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.94f).padding(bottom = 8.dp) // Añade padding abajo
                        )
                    }
                    // --- FIN Mensaje de Error ---


                    // --- Botón Ingresar ---
                    Button(
                        onClick = { doLogin() }, // Llama a la función de login
                        enabled = !vm.loading && isEmailValid && isPassValid, // Habilitado si no carga y campos válidos
                        colors = ButtonDefaults.buttonColors(containerColor = if (!vm.loading && isEmailValid && isPassValid) cs.primary else cs.primary.copy(alpha = 0.6f), contentColor = cs.onPrimary),
                        modifier = Modifier.fillMaxWidth(0.94f).height(48.dp)
                    ) { // Contenido del botón
                        if (vm.loading) { // Muestra indicador si está cargando
                            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp).padding(end = 8.dp), color = cs.onPrimary)
                        }
                        Text(if (vm.loading) "Ingresando..." else "Ingresar") // Texto cambia si carga
                    }
                    Spacer(Modifier.height(10.dp))

                    // --- Botón Crear Cuenta ---
                    Button(
                        onClick = { nav.navigate(NavRoutes.REGISTER) }, // Navega a Registro
                        colors = ButtonDefaults.buttonColors(containerColor = cs.primary, contentColor = cs.onPrimary), // Mismo estilo que Ingresar
                        modifier = Modifier.fillMaxWidth(0.94f).height(48.dp)
                    ) {
                        Text("Crear cuenta")
                    }
                    Spacer(Modifier.height(16.dp)) // Espacio final
                }
            }
        }
    }
}

// --- Añadir función clearError a ViewModel ---
// Debes añadir esta función a tu clase LoginViewModel
