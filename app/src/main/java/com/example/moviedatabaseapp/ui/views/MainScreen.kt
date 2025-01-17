package com.example.moviedatabaseapp.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviedatabaseapp.ui.viewmodels.MoviesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: MoviesViewModel = viewModel()
    val navController = rememberNavController()
    val uiState by viewModel.MainScreenUIState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = uiState.title)) })
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = AppScreens.MoviesScreen.name,
            modifier = modifier.padding(it)
        ) {
            composable(route = AppScreens.MoviesScreen.name) {
                MoviesScreen(viewModel = viewModel, navController = navController)
            }
            composable(AppScreens.MovieDescription.name + "/{id}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("id")
                if (movieId != null) {
                    MovieDescription(id = movieId, viewModel = viewModel, navController = navController)
                }
            }
        }
    }

}

enum class AppScreens {
    MoviesScreen,
    MovieDescription
}