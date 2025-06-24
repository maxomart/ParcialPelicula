//Movie.kt
package com.example.peliculas.models

import java.io.Serializable

data class Movie(
    val id: Int,
    val titulo: String,
    val anio: String,
    val resena: String,
    val genero: Genero,
    var rating: Float = 0f,
    var estado: Estado = Estado.PENDIENTE
) : Serializable
