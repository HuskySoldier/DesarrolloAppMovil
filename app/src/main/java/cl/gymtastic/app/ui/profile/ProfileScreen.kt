package cl.gymtastic.app.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import cl.gymtastic.app.R
import cl.gymtastic.app.data.local.datastore.ProfileDataStore // Mantenemos para Nombre, Fono, Bio, Avatar
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ImageUriUtils
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    nav: NavController,
    windowSizeClass: WindowSizeClass // <-- Parámetro WSC
) {
    val ctx = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- Mantenemos ProfileDataStore para datos editables ---
    val profileFlow = remember { ProfileDataStore.observe(ctx) }
    val saved by profileFlow.collectAsState(initial = ProfileDataStore.Profile())

    // --- Obtenemos el Email desde AuthPrefs ---
    val session = ServiceLocator.auth(ctx).prefs()
    val authEmail by session.userEmailFlow.collectAsStateWithLifecycle(initialValue = "")

    // Email mostrado (prioriza AuthPrefs, fallback a ProfileDataStore si Auth está vacío)
    val displayEmail = if (authEmail.isNotBlank()) authEmail else saved.email.ifBlank { "Sin email registrado" }

    // Estado editable (sincronizado con ProfileDataStore)
    var name by remember { mutableStateOf(saved.name) }
    var phone by remember { mutableStateOf(saved.phone) }
    var bio by remember { mutableStateOf(saved.bio) }
    var avatarUri by remember { mutableStateOf(saved.avatarUri?.let(Uri::parse)) }

    LaunchedEffect(saved) {
        name = saved.name
        phone = saved.phone
        bio = saved.bio
        avatarUri = saved.avatarUri?.let(Uri::parse)
    }

    // Validaciones (sin cambios)
    var phoneError by remember { mutableStateOf<String?>(null) }
    val maxBioChars = 240
    val bioCount = bio.length
    val isPhoneValid by derivedStateOf {
        val digits = phone.filter { it.isDigit() }
        digits.length in 8..12
    }
    LaunchedEffect(phone) {
        phoneError = if (phone.isBlank()) null
        else if (!isPhoneValid) "Ingresa un teléfono válido (8 a 12 dígitos)" else null
    }

    // Estado UI (sin cambios)
    var saving by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }

    // Animación de entrada (sin cambios)
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { show = true }

    val bg = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
            MaterialTheme.colorScheme.surface
        )
    )

    // Pickers (sin cambios)
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) {
            val u = pendingCameraUri!!
            avatarUri = u
            scope.launch {
                // Sigue guardando en ProfileDataStore
                ProfileDataStore.saveAvatar(ctx, u.toString())
                snackbar.showSnackbar("Foto tomada con cámara ✅")
            }
        }
        pendingCameraUri = null
    }
    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val u = ImageUriUtils.createTempImageUri(ctx).also { pendingCameraUri = it }
            takePictureLauncher.launch(u)
        } else {
            scope.launch { snackbar.showSnackbar("Permiso de cámara denegado") }
        }
    }
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            avatarUri = uri
            scope.launch {
                // Sigue guardando en ProfileDataStore
                ProfileDataStore.saveAvatar(ctx, uri.toString())
                snackbar.showSnackbar("Foto actualizada desde galería ✅")
            }
        }
    }

    // Lógica de Ancho
    val widthSizeClass = windowSizeClass.widthSizeClass
    val isCompact = widthSizeClass == WindowWidthSizeClass.Compact
    val cardWidthModifier = if (isCompact) Modifier.fillMaxWidth(0.95f) else Modifier.width(550.dp)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", color = Color.White) }, // Ajusta color si tu tema cambió
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White) // Ajusta color
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212), // Ajusta color
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center // Centra Card en tablets
        ) {
            AnimatedVisibility(
                visible = show,
                enter = fadeIn() + slideInVertically { it / 3 },
                exit = fadeOut() + slideOutVertically { it / 3 }
            ) {
                Card(
                    modifier = cardWidthModifier // <-- Ancho adaptativo
                        .shadow(8.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    // Scroll interno
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()) // <-- Scroll añadido aquí
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // ==== Avatar (sin cambios) ====
                        Box(
                            modifier = Modifier.wrapContentSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (avatarUri != null) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(ctx)
                                            .data(avatarUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                                    ) { /* Loading/Error state */
                                        when (painter.state) {
                                            is coil.compose.AsyncImagePainter.State.Loading -> {
                                                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(28.dp))
                                            }
                                            is coil.compose.AsyncImagePainter.State.Error -> {
                                                Image(painter = painterResource(R.drawable.ic_launcher_foreground), contentDescription = "Avatar por defecto", modifier = Modifier.size(72.dp))
                                            }
                                            else -> SubcomposeAsyncImageContent()
                                        }
                                    }
                                } else {
                                    Image(painter = painterResource(R.drawable.ic_launcher_foreground), contentDescription = "Avatar por defecto", modifier = Modifier.size(72.dp))
                                }
                            }
                            if (avatarUri != null) {
                                IconButton(
                                    onClick = { showRemoveDialog = true },
                                    modifier = Modifier
                                        .absoluteOffset(x = 64.dp, y = (-55).dp)
                                        .size(32.dp)
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f), CircleShape)
                                        .shadow(1.dp, CircleShape)
                                ) {
                                    Icon(Icons.Default.Clear, "Quitar foto", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Botones Galería / Cámara (sin cambios)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ElevatedButton(onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                                Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Galería")
                            }
                            ElevatedButton(onClick = { requestCameraPermission.launch(Manifest.permission.CAMERA) }) {
                                Icon(Icons.Filled.CameraAlt, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Cámara")
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        // ==== Email (solo lectura, desde AuthPrefs) ====
                        Text(
                            displayEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(16.dp))

                        // ==== Datos editables (leídos/escritos en ProfileDataStore, sin cambios) ====
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { input -> phone = input },
                            label = { Text("Teléfono") },
                            singleLine = true,
                            isError = phoneError != null,
                            supportingText = { phoneError?.let { Text(it) } },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = if (it.length <= maxBioChars) it else it.take(maxBioChars) },
                            label = { Text("Bio") },
                            singleLine = false,
                            minLines = 3,
                            supportingText = { Text("${bioCount}/${maxBioChars} caracteres") },
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))

                        // Botón Guardar (guarda en ProfileDataStore, sin cambios)
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        saving = true
                                        // Guardamos en ProfileDataStore
                                        ProfileDataStore.save(
                                            ctx,
                                            ProfileDataStore.Profile(
                                                name = name.trim(),
                                                phone = phone.trim(),
                                                bio = bio.trim(),
                                                avatarUri = avatarUri?.toString(),
                                                // El email de ProfileDataStore se mantiene (aunque displayEmail usa AuthPrefs)
                                                email = saved.email
                                            )
                                        )
                                        snackbar.showSnackbar("Perfil guardado")
                                    } catch (e: Exception) {
                                        snackbar.showSnackbar("Error al guardar: ${e.message ?: "desconocido"}")
                                    } finally {
                                        saving = false
                                    }
                                }
                            },
                            enabled = !saving && (phoneError == null),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (saving) {
                                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(10.dp))
                                Text("Guardando…")
                            } else {
                                Text("Guardar cambios")
                            }
                        }

                        // Botón Cerrar Sesión (sin cambios)
                        Button(
                            onClick = {
                                scope.launch {
                                    ServiceLocator.auth(ctx).logout()
                                    nav.navigate(NavRoutes.LOGIN) { popUpTo(0) }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp) // Añadido padding top
                        ) { Text("Cerrar sesión") }
                    }
                }
            }
        }
    }

    // Diálogo: quitar foto (sin cambios)
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Quitar foto de perfil") },
            text = { Text("¿Seguro que quieres quitar tu foto y volver al avatar por defecto?") },
            confirmButton = {
                TextButton(onClick = {
                    showRemoveDialog = false
                    avatarUri = null
                    scope.launch {
                        // Borra de ProfileDataStore
                        ProfileDataStore.saveAvatar(ctx, "")
                        snackbar.showSnackbar("Foto eliminada")
                    }
                }) { Text("Quitar") }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

