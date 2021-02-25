package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.domain.Landmark
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme

const val LANDMARK_ID = "landmark_id"

object Destinations {
    const val DETAILS = "details"
    const val LANDMARKS = "landmarks"
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val landmarks = landmarks()
        setContent {
            JetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Navigation(landmarks = landmarks)
                }
            }
        }
    }
}

@Composable
fun Navigation(landmarks: List<Landmark>) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.LANDMARKS,
        builder = {
            composable(Destinations.LANDMARKS) {
                ContentView(navController, landmarks)
            }
            composable(
                route = "${Destinations.DETAILS}/{$LANDMARK_ID}",
                arguments = listOf(navArgument(LANDMARK_ID) {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val landmarkId = backStackEntry.arguments?.getInt(LANDMARK_ID) ?: -1
                val landmark = landmarks.firstOrNull { it.id == landmarkId }
                DetailsView(landmark)
            }
        }
    )
}

@Composable
fun ContentView(
    navController: NavController,
    landmarks: List<Landmark>
) {
    val padding = 8.dp
    var showAsFavoriteOnly by remember { mutableStateOf(false) }
    val filteredList = landmarks.filter { landmark ->
        showAsFavoriteOnly.not() || landmark.isFavorite
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.padding(padding),
            text = "Landmarks",
            style = MaterialTheme.typography.h4
        )
        Divider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Text(text = "Favorites only")
            Switch(checked = showAsFavoriteOnly, onCheckedChange = {
                showAsFavoriteOnly = showAsFavoriteOnly.not()
            })
        }
        Divider()
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(count = filteredList.size) { index ->
                val landmark = filteredList[index]
                LandmarkRow(landmark, modifier = Modifier.clickable {
                    navController.navigate("${Destinations.DETAILS}/${landmark.id}")
                })
                if (index != filteredList.lastIndex) {
                    Divider()
                }
            }
        }
    }
}

@Composable
fun DetailsView(landmark: Landmark?) {
    val name = landmark?.name ?: "Not found"
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box {
                // TODO: Replace Image with Google Maps using AndroidView
                Image(
                    painter = painterResource(id = R.drawable.passarela),
                    contentDescription = landmark?.name ?: "Isso aqui e uma imagem",
                    modifier = Modifier.fillMaxWidth()
                )
                if (landmark != null) {
                    CircleImage(
                        landmark = landmark,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 75.dp)
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        top = 80.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    )
            ) {
                Text(
                    style = MaterialTheme.typography.h4,
                    text = name
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        style = MaterialTheme.typography.subtitle1,
                        text = landmark?.park ?: "Not found"
                    )
                    Text(
                        style = MaterialTheme.typography.subtitle1,
                        text = landmark?.state ?: "Not found"
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    style = MaterialTheme.typography.h6,
                    text = "About $name"
                )
                Text(
                    style = MaterialTheme.typography.subtitle1,
                    text = landmark?.description ?: "Not found"
                )
            }
        }
    }
}

@Composable
fun CircleImage(landmark: Landmark, modifier: Modifier = Modifier) {
    val imageId = LocalContext.current.run {
        resources.getIdentifier(landmark.imageName, "drawable", packageName)
    }
    Image(
        painter = painterResource(id = imageId),
        contentDescription = landmark.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(150.dp)
            .border(width = 4.dp, color = Color.White, shape = CircleShape)
            .shadow(elevation = 7.dp, shape = CircleShape)
        //.then(modifier)
    )
    /*val image = loadImageResource(id = R.drawable.turtlerock)
    image.resource.resource?.let {
        Surface(color = Color.Blue) {
            Image(
                    bitmap = it,
                    modifier = Modifier
                            .offset(x = 20.dp, y = 20.dp)
                            //.preferredSize(120.dp)
                            //.clip(shape = CircleShape)
                            .border(width = 4.dp, color = Color.Gray, shape = CircleShape)
                            .shadow(elevation = 10.dp, shape = CircleShape)
            )
        }
    }*/
}

@Composable
fun LandmarkRow(landmark: Landmark, modifier: Modifier = Modifier) {
    val imageId = LocalContext.current.run {
        resources.getIdentifier(landmark.imageName, "drawable", packageName)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = landmark.name,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.weight(weight = 0.05f))
        Text(text = landmark.name)
        Spacer(modifier = Modifier.weight(weight = 1f))
        if (landmark.isFavorite) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Favorite",
                tint = Color(0xFFF5BD1F)
            )
        }
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Arrow forward")
    }
}