package com.example.moviedatabaseapp.ui.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.moviedatabaseapp.R
import com.example.moviedatabaseapp.models.Movie
import com.example.moviedatabaseapp.ui.viewmodels.MoviesUIState
import com.example.moviedatabaseapp.ui.viewmodels.MoviesViewModel


@Composable
fun MovieDescription(id: String, viewModel: MoviesViewModel, navController: NavController) {

    getMovieData(id = id, viewModel = viewModel)
}

@Composable
fun getMovieData(id: String, viewModel: MoviesViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is MoviesUIState.Success -> {
            val movies = (uiState as MoviesUIState.Success).movies
            val movie = movies.find { it.id == id.toInt() }
            movie?.let {
                MovieCard(movie = it)
            } ?: run {
                Text(text = "Movie not found")
            }
        }

        is MoviesUIState.Error -> {
            Text(text = "Error loading movies")
        }

        MoviesUIState.Loading -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun MovieCard(movie: Movie) {
    val density = LocalDensity.current.density
    val width = remember {
        mutableStateOf(0F)
    }
    val height = remember {
        mutableStateOf(0F)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Card(
            modifier = Modifier,
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
                        .height(400.dp)
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
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 6.em,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp, horizontal = 12.dp)
        ) {
            DataRow(label = "OVERVIEW", value = movie.overview)
            DataRow(label = "EVALUATION", value = movie.evaluationNote.toString())
            DataRow(label = "LANGUAGE", value = movie.language)
        }
    }
}


@Composable
fun DataRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontSize = 4.em
            ),
            modifier = Modifier.weight(2f)
        )
    }
}