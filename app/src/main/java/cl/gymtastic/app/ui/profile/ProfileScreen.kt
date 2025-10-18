package cl.gymtastic.app.ui.profile

import android.Manifest
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cl.gymtastic.app.R
import cl.gymtastic.app.data.local.datastore.ProfileDataStore
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.ui.navigation.Screen
import cl.gymtastic.app.util.ImageUriUtils
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(nav: NavController) {
    val ctx = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Email (placeholder o tu fuente real)
    var email by remember { mutableStateOf("admin@gymtastic.cl") }

    // Perfil desde DataStore
    val profileFlow = remember { ProfileDataStore.observe(ctx) }
    val saved by profileFlow.collectAsState(initial = ProfileDataStore.Profile())

    var name by remember(saved.name) { mutableStateOf(saved.name) }
    var phone by remember(saved.phone) { mutableStateOf(saved.phone) }
    var bio by remember(saved.bio) { mutableStateOf(saved.bio) }

    // Avatar actual
    var avatarUri by remember(saved.avatarUri) { mutableStateOf(saved.avatarUri?.let(Uri::parse)) }

    // Animación de entrada
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { show = true }

    val bg = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
            MaterialTheme.colorScheme.surface
        )
    )

    // ====== Pickers ======

    // Estado del archivo temporal de cámara
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    // 1) Lanzador de CÁMARA (debe declararse antes de quien lo use)
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) {
            val u = pendingCameraUri!!
            avatarUri = u
            scope.launch {
                ProfileDataStore.saveAvatar(ctx, u.toString())
                snackbar.showSnackbar("Foto tomada con cámara ✅")
            }
        }
        // limpiar siempre
        pendingCameraUri = null
    }

    // 2) Permiso de cámara: crea Uri y lanza cámara si es concedido
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

    // 3) Photo Picker (galería) – no requiere permisos de almacenamiento
    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            avatarUri = uri
            scope.launch {
                ProfileDataStore.saveAvatar(ctx, uri.toString())
                snackbar.showSnackbar("Foto actualizada desde galería ✅")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
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
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = show,
                enter = fadeIn() + slideInVertically { it / 3 },
                exit = fadeOut() + slideOutVertically { it / 3 }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .shadow(8.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // ==== Avatar ====
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (avatarUri != null) {
                                AsyncImage(
                                    model = avatarUri,
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Image(
                                    painter = androidx.compose.ui.res.painterResource(R.drawable.ic_launcher_foreground),
                                    contentDescription = "Avatar",
                                    modifier = Modifier.size(72.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Botones Galería / Cámara
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ElevatedButton(
                                onClick = {
                                    pickMedia.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            ) {
                                Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Galería")
                            }
                            ElevatedButton(
                                onClick = {
                                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                                }
                            ) {
                                Icon(Icons.Filled.CameraAlt, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Cámara")
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        // ==== Datos editables ====
                        Text(
                            email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Teléfono") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Bio") },
                            singleLine = false,
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    ProfileDataStore.save(
                                        ctx,
                                        ProfileDataStore.Profile(
                                            name = name,
                                            phone = phone,
                                            bio = bio,
                                            avatarUri = avatarUri?.toString()
                                        )
                                    )
                                    snackbar.showSnackbar("Perfil guardado ✅")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar cambios")
                        }

                        TextButton(onClick = {
                            nav.navigate(Screen.Home.route) { launchSingleTop = true }
                        }) { Text("Volver al Home") }

                        Button(
                            onClick = {
                                scope.launch {
                                    ServiceLocator.auth(ctx).logout()
                                    nav.navigate(NavRoutes.LOGIN) {
                                        popUpTo(0)
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cerrar sesión")
                        }

                    }
                }
            }
        }
    }
}

