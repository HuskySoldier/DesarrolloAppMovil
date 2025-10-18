package cl.gymtastic.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.R
import cl.gymtastic.app.data.datastore.MembershipPrefs
import cl.gymtastic.app.ui.navigation.NavRoutes
import cl.gymtastic.app.ui.navigation.Screen
import cl.gymtastic.app.util.ServiceLocator
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val cs = MaterialTheme.colorScheme
    val ctx = LocalContext.current

    // 🔐 Estado de membresía (activa o no)
    val membership by remember { MembershipPrefs.observe(ctx) }
        .collectAsStateWithLifecycle(initialValue = MembershipPrefs.State())

    // Snackbar (útil si decides deshabilitar y avisar)
    val snackbar = remember { SnackbarHostState() }

    // Items base (siempre visibles)
    val baseItems = listOf(
        "Home" to Screen.Home.route,
        "Planes" to Screen.Planes.route,
        "Tienda" to Screen.Store.route,
        "Carrito" to Screen.Cart.route
    )
    // Items que dependen de la membresía
    val gatedItems = listOf(
        "Check-In" to Screen.CheckIn.route,
        "Trainers" to Screen.Trainers.route
    )

    // 👇 Opción A (recomendada): ocultar si no hay plan activo
    val drawerItems =
        if (membership.hasActivePlan) baseItems + gatedItems + listOf("Cerrar sesión" to "logout")
        else baseItems + listOf("Cerrar sesión" to "logout")

    // --------
    // (Opción B alternativa: mostrar deshabilitado y avisar con snackbar)
    // fun isLocked(label: String) =
    //     (label == "Check-In" || label == "Trainers") && !membership.hasActivePlan
    // --------

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = cs.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "GymTastic",
                            style = MaterialTheme.typography.titleLarge,
                            color = cs.primary,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        drawerItems.forEach { (label, route) ->
                            NavigationDrawerItem(
                                label = { Text(label, color = cs.onSurface) },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        if (route == "logout") {
                                            ServiceLocator.auth(ctx).logout()
                                            nav.navigate(NavRoutes.LOGIN) { popUpTo(0) }
                                        } else {
                                            // --------
                                            // (Si usas Opción B deshabilitada)
                                            // if (isLocked(label)) {
                                            //     snackbar.showSnackbar("Activa un plan para usar $label")
                                            // } else {
                                            //     nav.navigate(route) { launchSingleTop = true }
                                            // }
                                            // --------

                                            // Opción A (ocultar): navegar normal
                                            nav.navigate(route) { launchSingleTop = true }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(cs.surfaceVariant)
                                    .padding(horizontal = 8.dp),
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = cs.primary.copy(alpha = 0.20f),
                                    unselectedContainerColor = Color.Transparent,
                                    selectedTextColor = cs.primary,
                                    unselectedTextColor = cs.onSurface
                                )
                                // , enabled = !isLocked(label) // <- Opción B (mostrar deshabilitado)
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home", color = cs.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Abrir menú",
                                tint = cs.onBackground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            nav.navigate(Screen.Profile.route) { launchSingleTop = true }
                        }) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Perfil",
                                tint = cs.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = cs.background,
                        titleContentColor = cs.onBackground,
                        navigationIconContentColor = cs.onBackground,
                        actionIconContentColor = cs.onBackground
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbar) } // <- útil si usas Opción B
        ) { innerPadding ->
            HomeContent(Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun HomeContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(R.drawable.gym_background),
            contentDescription = "Fondo",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura semitransparente para legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        // Contenido principal centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Bienvenido a GymTastic 💪",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "¡Entrena, progresa y supera tus límites!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}
