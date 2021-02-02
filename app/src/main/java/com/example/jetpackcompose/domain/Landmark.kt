package com.example.jetpackcompose.domain

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class Landmark(
    val id: Int,
    val name: String,
    val park: String,
    val state: String,
    val description: String,
    val imageName: String,
    val isFavorite: Boolean,
    val coordinates: Coordinates
)