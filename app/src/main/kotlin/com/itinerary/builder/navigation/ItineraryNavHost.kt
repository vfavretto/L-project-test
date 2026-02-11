package com.itinerary.builder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.itinerary.feature.details.DetailsScreen
import com.itinerary.feature.home.HomeScreen
import com.itinerary.feature.trip.TripScreen
import kotlinx.serialization.Serializable

// Navigation routes using type-safe Navigation 3
@Serializable
object HomeRoute

@Serializable
data class TripRoute(val tripId: Long)

@Serializable
data class InterestMarkDetailsRoute(val markId: Long)

@Composable
fun ItineraryNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToTrip = { tripId ->
                    navController.navigate(TripRoute(tripId))
                },
                onAddTrip = {
                    // onAddTrip callback não é mais usado - dialog é gerenciado pelo ViewModel
                }
            )
        }

        composable<TripRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<TripRoute>()
            TripScreen(
                tripId = args.tripId,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetails = { markId ->
                    navController.navigate(InterestMarkDetailsRoute(markId))
                }
            )
        }

        composable<InterestMarkDetailsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<InterestMarkDetailsRoute>()
            DetailsScreen(
                markId = args.markId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
