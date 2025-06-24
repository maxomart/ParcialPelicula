//MovieViewModel.kt
package com.example.peliculas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.peliculas.models.Movie


class MovieViewModel : ViewModel() {
    val movies = mutableListOf<Movie>()


}
