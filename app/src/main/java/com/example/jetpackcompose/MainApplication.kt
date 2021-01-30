package com.example.jetpackcompose

import android.app.Activity
import android.app.Application
import com.example.jetpackcompose.domain.Landmark
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class MainApplication : Application() {

    lateinit var landmarks: List<Landmark>

    override fun onCreate() {
        super.onCreate()
        val inputStream = resources.openRawResource(R.raw.landmark_data)
        val json = inputStream.reader().readText()
        landmarks = Json {
            ignoreUnknownKeys = true
        }.decodeFromString(ListSerializer(Landmark.serializer()), json)
    }
}

fun Activity.landmarks() = (application as MainApplication).landmarks