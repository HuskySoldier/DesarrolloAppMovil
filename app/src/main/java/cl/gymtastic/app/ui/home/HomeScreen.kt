package cl.gymtastic.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // ✅ nuevo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.gymtastic.app.ui.navigation.Screen
import cl.gymtastic.app.ui.navigation.NavRoutes   // ✅ nuevo
import cl.gymtastic.app.util.ServiceLocator      // ✅ nuevo
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import cl.gymtastic.app.R




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val cs = MaterialTheme.colorScheme
    val ctx = LocalContext.current // ✅ lo usamos para logout

    val drawerItems = listOf(
        "Home" to Screen.Home.route,
        "Planes" to Screen.Planes.route,
        "Tienda" to Screen.Store.route,
        "Carrito" to Screen.Cart.route,
        "Check-In" to Screen.CheckIn.route,
        "Trainers" to Screen.Trainers.route,
        "Cerrar sesión" to "logout" // 👈 clave para detectar logout
    )

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
                                            // ✅ Cerrar sesión y limpiar back stack
                                            ServiceLocator.auth(ctx).logout()
                                            nav.navigate(NavRoutes.LOGIN) {
                                                popUpTo(0)
                                            }
                                        } else {
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
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú", tint = cs.onBackground)
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            nav.navigate(Screen.Profile.route) { launchSingleTop = true }
                        }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Perfil", tint = cs.onBackground)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = cs.background,
                        titleContentColor = cs.onBackground,
                        navigationIconContentColor = cs.onBackground,
                        actionIconContentColor = cs.onBackground
                    )
                )
            }
        ) { innerPadding ->
            HomeContent(Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun HomeContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(R.drawable.gym_background), // 👈 tu imagen en res/drawable
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
