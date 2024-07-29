package com.example.moviedatabaseapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviedatabaseapp.R
import com.example.moviedatabaseapp.data.Movie
import com.example.moviedatabaseapp.ui.viewmodels.MoviesUIState
import com.example.moviedatabaseapp.ui.viewmodels.MoviesViewModel

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    when (uiState) {
        is MoviesUIState.Loading -> LoadingScreen()
        is MoviesUIState.Success -> MovieList(movies = (uiState as MoviesUIState.Success).movies, navController = navController, viewModel = viewModel)
        is MoviesUIState.Error -> ErrorScreen()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "CARREGANDO...",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ALGO DEU ERRADO :(",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    navController: NavController,
    viewModel: MoviesViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "POPULAR MOVIES",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 4.dp
                ),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(movies) { movie ->
                MovieEntry(movie = movie, onMovieClick = {
                    viewModel.navigate(
                    movie = movie,
                    navController = navController
                )})
            }
        }
    }
}

@Composable
fun MovieEntry(
    movie: Movie,
    onMovieClick: () -> Unit,
) {
    val density = LocalDensity.current.density
    val width = remember {
        mutableStateOf(0F)
    }
    val height = remember {
        mutableStateOf(0F)
    }

    Card(
        modifier = Modifier.padding(6.dp).clickable { onMovieClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box() {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500/" + movie.poster)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(
                    R.drawable.movies
                ),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RectangleShape)
                    .onGloballyPositioned {
                        width.value = it.size.width / density
                        height.value = it.size.height / density
                    }

            )
            Box(
                modifier = Modifier
                    .size(
                        width = width.value.dp,
                        height = height.value.dp
                    )
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black),
                            100F,
                            900F,
                        )
                    )
            )
            Text(
                text = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                )
            )
        }
    }
}