package com.example.jetpackcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.domain.Landmark
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object Destinations {
    const val DETAILS = "details"
    const val LANDMARKS = "landmarks"
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FIXME: avoid load heavy resources on main thread
        val inputStream = resources.openRawResource(R.raw.landmark_data)
        val json = inputStream.reader().readText()
        val landmarks = Json {
            ignoreUnknownKeys = true
        }.decodeFromString(ListSerializer(Landmark.serializer()), json)

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
                            composable(Destinations.DETAILS) {
                                DetailsView(navController)
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
                    navController.navigate(Destinations.DETAILS)
                })
                if (index != landmarks.lastIndex) {
                    Divider()
                }
            }
        }
    }
}

@Composable
fun DetailsView(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            // TODO: Replace Image with Google Maps using AndroidView
            Image(
                bitmap = imageResource(id = R.drawable.passarela),
                contentDescription = "Isso aqui e uma imagem",
                modifier = Modifier.fillMaxWidth()
            )
            CircleImage(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 75.dp)
            )
        }
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
                text = "Turtle Rock"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    style = MaterialTheme.typography.subtitle1,
                    text = "Joshua Tree National Park"
                )
                Text(
                    style = MaterialTheme.typography.subtitle1,
                    text = "California"
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                style = MaterialTheme.typography.h6,
                text = "About Turtle Rock"
            )
            Text(
                style = MaterialTheme.typography.subtitle1,
                text = "Descriptive text goes here."
            )
        }
    }
}

@Composable
fun CircleImage(modifier: Modifier = Modifier) {
    Image(
        bitmap = imageResource(id = R.drawable.turtlerock),
        contentDescription = "Isso aqui e uma imagem",
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //ContentView(navController, listOf())
}