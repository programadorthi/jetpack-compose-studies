package com.example.jetpackcompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ContentView()
                }
            }
        }
    }
}

@Composable
fun ContentView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Replace Image with Google Maps using AndroidView
        Image(
            bitmap = imageResource(id = R.drawable.passarela),
            contentDescription = "Isso aqui e uma imagem",
            modifier = Modifier.fillMaxWidth()
        )
        CircleImage(
            modifier = Modifier
                .offset(y = (-75).dp)
        )
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                style = MaterialTheme.typography.h4,
                text = "Turtle Rock"
            )
            Row {
                Text(
                    style = MaterialTheme.typography.subtitle1,
                    text = "Joshua Tree National Park"
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                Text(
                    style = MaterialTheme.typography.subtitle1,
                    text = "California"
                )
            }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ContentView()
}