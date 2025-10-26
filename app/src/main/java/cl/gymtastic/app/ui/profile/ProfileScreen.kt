package cl.gymtastic.app.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
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
import cl.gymtastic.app.data.local.datastore.ProfileDataStore
import cl.gymtastic.app.data.local.db.GymTasticDatabase // <-- Importar DB
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.util.ImageUriUtils
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.Dispatchers // <-- Importar Dispatchers
import kotlinx.coroutines.flow.flowOf // <-- Importar flowOf
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.withContext // <-- Importar withContext

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    nav: NavController,
    windowSizeClass: WindowSizeClass
) {
    val ctx = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- Datos Editables (Nombre, Fono, Bio) desde ProfileDataStore ---
    // Mantenemos estos aquí por ahora.
    val profileFlow = remember { ProfileDataStore.observe(ctx) }
    val savedProfile by profileFlow.collectAsState(initial = ProfileDataStore.Profile())

    // --- Sesión y UserEntity (para Email y Avatar) ---
    val session = remember { ServiceLocator.auth(ctx).prefs() }
    val authEmail by session.userEmailFlow.collectAsStateWithLifecycle(initialValue = "")
    val usersDao = remember { GymTasticDatabase.get(ctx).users() } // <-- Obtener DAO

    // Observar UserEntity desde Room
    val userEntity by remember(authEmail) {
        if (authEmail.isNotBlank()) usersDao.observeByEmail(authEmail) else flowOf(null)
    }.collectAsStateWithLifecycle(initialValue = null)

    // Email mostrado
    val displayEmail = if (authEmail.isNotBlank()) authEmail else userEntity?.email ?: "Sin email registrado"

    // --- Estado Editable (Nombre, Fono, Bio leen de ProfileDataStore) ---
    var name by remember { mutableStateOf(savedProfile.name) }
    var phone by remember { mutableStateOf(savedProfile.phone) }
    var bio by remember { mutableStateOf(savedProfile.bio) }
    // --- Estado Editable (Avatar lee de UserEntity) ---
    var avatarUriInternal by remember { mutableStateOf<Uri?>(null) } // Uri para Coil/UI
    var avatarUriString by remember { mutableStateOf<String?>(null) } // String actual en DB

    // Sincronizar estado local cuando cambia el ProfileDataStore o UserEntity
    LaunchedEffect(savedProfile) {
        name = savedProfile.name
        phone = savedProfile.phone
        bio = savedProfile.bio
    }
    // Sincronizar estado local del avatar cuando UserEntity cambia
    LaunchedEffect(userEntity) {
        avatarUriString = userEntity?.avatarUri
        avatarUriInternal = userEntity?.avatarUri?.let { Uri.parse(it) }
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

    // Estado UI
    var saving by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }

    // Animación de entrada
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { show = true }

    val bg = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.20f), MaterialTheme.colorScheme.surface))

    // ====== Pickers ======
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    // --- FUNCIÓN PARA GUARDAR NUEVA URI DE AVATAR (en Room) ---
    fun saveNewAvatarUri(newUriString: String?, oldUriString: String?) {
        if (authEmail.isBlank()) {
            scope.launch { snackbar.showSnackbar("Error: No se pudo identificar al usuario.") }
            return // Necesitamos el email para saber a quién actualizar
        }

        scope.launch {
            // 1. Guardar la nueva URI en la BD (Room)
            val updatedRows = usersDao.updateAvatarUri(authEmail, newUriString)

            if (updatedRows > 0) {
                // 2. Si se guardó y la URI antigua es diferente (y no nula), borrar el archivo antiguo
                if (oldUriString != null && oldUriString != newUriString) {
                    Log.d("ProfileScreen", "Borrando imagen antigua: $oldUriString")
                    withContext(Dispatchers.IO) {
                        ImageUriUtils.deleteFileFromInternalStorage(oldUriString)
                    }
                }
                // 3. Actualizar estado local (aunque el Flow lo hará eventualmente)
                avatarUriString = newUriString // Actualiza el string base
                avatarUriInternal = newUriString?.let { Uri.parse(it) } // Actualiza la Uri para Coil

                snackbar.showSnackbar("Foto de perfil actualizada ✅")
            } else {
                snackbar.showSnackbar("Error al guardar la foto de perfil en la base de datos")
                // Opcional: Si falló el guardado en BD, borrar el archivo nuevo que se copió
                if (newUriString != null) {
                    Log.w("ProfileScreen", "Error al guardar en BD, borrando archivo copiado: $newUriString")
                    withContext(Dispatchers.IO) { ImageUriUtils.deleteFileFromInternalStorage(newUriString) }
                }
            }
        }
    }


    // Cámara
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && pendingCameraUri != null) {
            // Guardamos la URI temporal de la cámara
            val newUriString = pendingCameraUri.toString()
            Log.d("ProfileScreen", "Foto tomada, guardando URI: $newUriString")
            saveNewAvatarUri(newUriString = newUriString, oldUriString = avatarUriString)
        } else {
            Log.d("ProfileScreen", "Foto cancelada o fallida")
        }
        pendingCameraUri = null // Limpiar URI temporal de cámara en cualquier caso
    }
    val requestCameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            // Creamos URI temporal ANTES de lanzar la cámara
            try {
                val u = ImageUriUtils.createTempImageUri(ctx).also {
                    pendingCameraUri = it
                    Log.d("ProfileScreen", "URI temporal creada para cámara: $it")
                }
                takePictureLauncher.launch(u)
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Error al crear URI temporal para cámara", e)
                scope.launch { snackbar.showSnackbar("Error al preparar la cámara") }
            }
        } else {
            scope.launch { snackbar.showSnackbar("Permiso de cámara denegado") }
        }
    }

    // Galería (Photo Picker)
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("ProfileScreen", "Imagen seleccionada de galería: $uri")
            // Copiar la imagen a almacenamiento interno ANTES de llamar a saveNewAvatarUri
            scope.launch(Dispatchers.IO) { // Usar IO para operaciones de archivo
                Log.d("ProfileScreen", "Iniciando copia a almacenamiento interno...")
                val internalUriString = ImageUriUtils.copyUriToInternalStorage(ctx, uri, "avatar_${authEmail.replace("@", "_")}") // Nombre de archivo único
                withContext(Dispatchers.Main) { // Volver al hilo principal para UI y DB
                    if (internalUriString != null) {
                        Log.d("ProfileScreen", "Copia exitosa, URI interna: $internalUriString. Guardando en BD...")
                        // Guardar la URI INTERNA (String) en la BD
                        saveNewAvatarUri(newUriString = internalUriString, oldUriString = avatarUriString)
                    } else {
                        Log.e("ProfileScreen", "Error al copiar la imagen de galería")
                        snackbar.showSnackbar("Error al copiar la imagen seleccionada")
                    }
                }
            }
        } else {
            Log.d("ProfileScreen", "Selección de galería cancelada")
        }
    }

    // Lógica de Ancho
    val widthSizeClass = windowSizeClass.widthSizeClass
    val isCompact = widthSizeClass == WindowWidthSizeClass.Compact
    val cardWidthModifier = if (isCompact) Modifier.fillMaxWidth(0.95f) else Modifier.width(550.dp)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", color = MaterialTheme.colorScheme.onBackground) }, // Usar colores del tema
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground) // Usar colores del tema
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background, // Usar colores del tema
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(bg).padding(padding).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(visible = show, enter = fadeIn() + slideInVertically { it / 3 }, exit = fadeOut() + slideOutVertically { it / 3 }) {
                Card(
                    modifier = cardWidthModifier.shadow(8.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // ==== Avatar (Usa avatarUriInternal) ====
                        Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier.size(110.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                // --- Usa avatarUriInternal (Uri?) ---
                                if (avatarUriInternal != null) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(ctx)
                                            .data(avatarUriInternal) // <-- Usa la Uri del estado local
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        loading = { CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(28.dp)) },
                                        error = { Image(painter = painterResource(R.drawable.ic_launcher_foreground), contentDescription = "Avatar por defecto", modifier = Modifier.size(72.dp)) },
                                        success = { SubcomposeAsyncImageContent() }
                                    )
                                } else {
                                    Image(painter = painterResource(R.drawable.ic_launcher_foreground), contentDescription = "Avatar por defecto", modifier = Modifier.size(72.dp))
                                }
                            }
                            // Botón flotante para quitar
                            if (avatarUriInternal != null) {
                                IconButton(
                                    onClick = { showRemoveDialog = true }, // Abre diálogo
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd) // Posición más estándar
                                        .offset(x = 8.dp, y = 8.dp) // Ajuste fino
                                        .size(32.dp)
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), CircleShape) // Fondo semi-opaco
                                        .shadow(1.dp, CircleShape)
                                ) {
                                    Icon(Icons.Default.Clear, "Quitar foto", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Botones Galería / Cámara
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

                        // Email (solo lectura)
                        Text(displayEmail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(16.dp))

                        // Datos editables (Nombre, Fono, Bio -> ProfileDataStore)
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, singleLine = true, keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = phone, onValueChange = { input -> phone = input }, label = { Text("Teléfono") }, singleLine = true, isError = phoneError != null, supportingText = { phoneError?.let { Text(it) } }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(value = bio, onValueChange = { bio = if (it.length <= maxBioChars) it else it.take(maxBioChars) }, label = { Text("Bio") }, singleLine = false, minLines = 3, supportingText = { Text("${bioCount}/${maxBioChars} caracteres") }, keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done), modifier = Modifier.fillMaxWidth())

                        Spacer(Modifier.height(20.dp))

                        // Botón Guardar (SOLO guarda Nombre, Fono, Bio en ProfileDataStore)
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        saving = true
                                        ProfileDataStore.save(
                                            ctx,
                                            ProfileDataStore.Profile(
                                                name = name.trim(),
                                                phone = phone.trim(),
                                                bio = bio.trim(),
                                                // Ya no guardamos avatarUri aquí
                                                email = savedProfile.email // Mantiene el email original de ProfileDataStore
                                            )
                                        )
                                        snackbar.showSnackbar("Datos del perfil guardados")
                                    } catch (e: Exception) {
                                        snackbar.showSnackbar("Error al guardar datos: ${e.message ?: "desconocido"}")
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
                                Text("Guardar Datos")
                            }
                        }

                        // Botón Cerrar Sesión
                        Button(
                            onClick = {
                                scope.launch {
                                    ServiceLocator.auth(ctx).logout()
                                    nav.navigate(NavRoutes.LOGIN) { popUpTo(0) }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) { Text("Cerrar sesión") }
                    }
                }
            }
        }
    }

    // Diálogo: quitar foto (Actualizado para usar saveNewAvatarUri)
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Quitar foto de perfil") },
            text = { Text("¿Seguro que quieres quitar tu foto y volver al avatar por defecto?") },
            confirmButton = {
                TextButton(onClick = {
                    showRemoveDialog = false
                    // Llama a saveNewAvatarUri con null para quitar y borrar archivo
                    Log.d("ProfileScreen", "Quitando foto, URI antigua: $avatarUriString")
                    saveNewAvatarUri(newUriString = null, oldUriString = avatarUriString)
                }) { Text("Quitar") }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

