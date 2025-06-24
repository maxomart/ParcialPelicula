//MovieListActivity.kt
package com.example.peliculas.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peliculas.adapters.MovieAdapter
import com.example.peliculas.databinding.ActivityMovieListBinding
import com.example.peliculas.models.Genero
import com.example.peliculas.models.Movie
import com.example.peliculas.models.Estado

class MovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapter

    private var movies = ArrayList<Movie>()           // Lista que se muestra
    private var originalMovies = ArrayList<Movie>()   // Lista completa de respaldo

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar películas desde MainActivity
        movies = intent.getSerializableExtra("movies") as? ArrayList<Movie> ?: ArrayList()
        originalMovies = ArrayList(movies) // Copia para restaurar luego

        adapter = MovieAdapter(movies,
            onEdit = { movie, position ->
                val intent = Intent(this, EditMovieActivity::class.java)
                intent.putExtra("movie", movie)
                intent.putExtra("index", position)
                startActivityForResult(intent, 1001)
            },
            onDelete = { position ->
                movies.removeAt(position)
                originalMovies.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        )

        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMovies.adapter = adapter

        // BOTONES
        binding.btnVolver.setOnClickListener {
            devolverListaActualizada()
        }

        binding.btnRestaurarLista.setOnClickListener {
            restaurarLista()
        }

        binding.btnBuscar.setOnClickListener {
            buscarPeliculas()
        }

        binding.btnFiltrar.setOnClickListener {
            filtrarPorGenero()
        }

        binding.btnOrdenarRating.setOnClickListener {
            val ordenadas = movies.sortedByDescending { it.rating }
            adapter.updateList(ordenadas)
        }

        binding.btnFiltrarEstado.setOnClickListener {
            val estados = Estado.values()
            val opciones = estados.map { it.name.lowercase().replaceFirstChar(Char::uppercase) }.toTypedArray()

            AlertDialog.Builder(this)
                .setTitle("Filtrar por Estado")
                .setItems(opciones) { _, which ->
                    val estadoSeleccionado = estados[which]
                    val resultado = originalMovies.filter { it.estado == estadoSeleccionado }
                    movies.clear()
                    movies.addAll(resultado)
                    adapter.updateList(movies)
                }
                .show()
        }
    }

    private fun devolverListaActualizada() {
        val resultIntent = Intent()
        resultIntent.putExtra("movies", originalMovies)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun restaurarLista() {
        movies.clear()
        movies.addAll(originalMovies)
        adapter.updateList(movies)
    }

    private fun buscarPeliculas() {
        val input = EditText(this)
        input.hint = "Título de película"

        AlertDialog.Builder(this)
            .setTitle("Buscar Película")
            .setView(input)
            .setPositiveButton("Buscar") { _, _ ->
                val query = input.text.toString().trim()
                val resultado = originalMovies.filter {
                    it.titulo.contains(query, ignoreCase = true)
                }
                movies.clear()
                movies.addAll(resultado)
                adapter.updateList(movies)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun filtrarPorGenero() {
        val generos = Genero.values()
        val nombresMostrar = generos.map {
            it.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Filtrar por Género")
            .setItems(nombresMostrar) { _, which ->
                val generoSeleccionado = generos[which]
                val resultado = originalMovies.filter { it.genero == generoSeleccionado }
                movies.clear()
                movies.addAll(resultado)
                adapter.updateList(movies)
            }
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        devolverListaActualizada()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && data != null) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val movieEditada = data.getSerializableExtra("movie") as Movie
                    val index = data.getIntExtra("index", -1)
                    if (index != -1) {
                        movies[index] = movieEditada
                        originalMovies[index] = movieEditada
                        adapter.notifyItemChanged(index)
                    }
                }

                Activity.RESULT_FIRST_USER -> {
                    val deleteIndex = data.getIntExtra("delete_index", -1)
                    if (deleteIndex != -1) {
                        movies.removeAt(deleteIndex)
                        originalMovies.removeAt(deleteIndex)
                        adapter.notifyItemRemoved(deleteIndex)
                    }
                }
            }
        }
    }
}
