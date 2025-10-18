package cl.gymtastic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import cl.gymtastic.app.ui.theme.GymTasticTheme
import cl.gymtastic.app.ui.navigation.NavGraph
import cl.gymtastic.app.ui.navigation.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ✅ Control de barra de estado
            val systemUiController = rememberSystemUiController()
            val darkIcons = false // ⚡ Íconos blancos sobre fondo oscuro

            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Black,   // 👈 Cambia aquí el color de la barra de estado
                    darkIcons = darkIcons
                )
            }

            GymTasticTheme {
                NavGraph(startDestination = Screen.Login.route)
            }
        }
    }
}
