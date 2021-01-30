package com.example.jetpackcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
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
            }
        }
    }
}

@Composable
fun ContentView(navController: NavController, landmarks: List<Landmark>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Landmarks",
            style = MaterialTheme.typography.h4
        )
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(count = landmarks.size) { index ->
                val landmark = landmarks[index]
                LandmarkRow(landmark, modifier = Modifier.clickable {
                    navController.navigate("${Destinations.DETAILS}/${landmark.id}")
                })
                if (index != landmarks.lastIndex) {
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
                    bitmap = imageResource(id = R.drawable.passarela),
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
    val imageId = AmbientContext.current.run {
        resources.getIdentifier(landmark.imageName, "drawable", packageName)
    }
    Image(
        bitmap = imageResource(id = imageId),
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
    val imageId = AmbientContext.current.run {
        resources.getIdentifier(landmark.imageName, "drawable", packageName)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = imageResource(id = imageId),
            contentDescription = landmark.name,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.weight(weight = 0.05f))
        Text(text = landmark.name)
        Spacer(modifier = Modifier.weight(weight = 1f))
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Arrow forward")
    }
}