
package cl.gymtastic.app.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import androidx.navigation.navArgument
import androidx.navigation.NavController

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Planes : Screen("planes")
    data object Payment : Screen("payment")
    data object Store : Screen("store")
    data object Cart : Screen("cart")
    data object CheckIn : Screen("checkin")
    data object Trainers : Screen("trainers")

    data object PaymentSuccess : Screen("payment_success")
    data object Booking : Screen("booking?trainerId={trainerId}") {
        fun routeWith(trainerId: Long?) =
            if (trainerId != null) "booking?trainerId=$trainerId" else "booking?trainerId=-1"
    }
    data object Profile : Screen("profile")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    startDestination: String = Screen.Login.route
) {
    val navController = rememberAnimatedNavController()

    val enterRight = { slideInHorizontally(animationSpec = tween(300)) { it } }
    val exitLeft   = { slideOutHorizontally(animationSpec = tween(300)) { -it } }
    val enterLeft  = { slideInHorizontally(animationSpec = tween(300)) { -it } }
    val exitRight  = { slideOutHorizontally(animationSpec = tween(300)) { it } }

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(
            route = Screen.Login.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.auth.LoginScreen(navController)
        }

        composable(
            route = Screen.Register.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.auth.RegisterScreen(navController)
        }

        composable(
            route = Screen.Home.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.home.HomeScreen(navController)
        }

        composable(
            route = Screen.Planes.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.planes.PlanesScreen(navController)
        }

        composable(
            route = Screen.Payment.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.payment.PaymentScreen(navController)
        }

        composable(
            route = Screen.Store.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.store.StoreScreen(navController)
        }

        composable(
            route = Screen.Cart.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.cart.CartScreen(navController)
        }

        composable(
            route = Screen.CheckIn.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.checkin.CheckInScreen(navController)
        }

        composable(
            route = Screen.Trainers.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.trainers.TrainersScreen(navController)
        }

        composable(
            route = Screen.Booking.route,
            arguments = listOf(
                navArgument("trainerId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            ),
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            val trainerId = it.arguments?.getLong("trainerId") ?: -1L
            cl.gymtastic.app.ui.booking.BookingScreen(navController /* , trainerId */)
        }

        composable(
            route = Screen.PaymentSuccess.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.payment.PaymentSuccessScreen(navController)
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = { enterRight() },
            exitTransition = { exitLeft() },
            popEnterTransition = { enterLeft() },
            popExitTransition = { exitRight() }
        ) {
            cl.gymtastic.app.ui.profile.ProfileScreen(navController)
        }

    }
}

fun NavController.goToBooking(trainerId: Long? = null) {
    navigate(Screen.Booking.routeWith(trainerId))
}

fun NavController.goHome(clearStack: Boolean = true) {
    if (clearStack) {
        navigate(Screen.Home.route) {
            popUpTo(graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    } else {
        navigate(Screen.Home.route)
    }
}
