//MovieStorage.kt
package com.example.peliculas.storage

import android.content.Context
import com.example.peliculas.models.Movie
import com.example.peliculas.models.Estado
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MovieStorage {
    private const val PREFS_NAME = "movie_prefs"
    private const val KEY_MOVIES = "movies"

    fun saveMovies(context: Context, movies: List<Movie>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(movies)
        prefs.edit().putString(KEY_MOVIES, json).apply()
    }

    fun loadMovies(context: Context): MutableList<Movie> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_MOVIES, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Movie>>() {}.type
            val lista = Gson().fromJson<MutableList<Movie>>(json, type)

            // Verificación defensiva por si hay campos 'estado' nulos
            lista.forEach { movie ->
                if (movie.estado == null) {
                    movie.estado = Estado.PENDIENTE
                }
            }

            lista
        } else {
            mutableListOf()
        }
    }
}
