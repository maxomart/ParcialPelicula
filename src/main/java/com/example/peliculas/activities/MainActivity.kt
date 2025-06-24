package com.example.peliculas.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.peliculas.databinding.ActivityMainBinding
import com.example.peliculas.models.Genero
import com.example.peliculas.models.Movie
import com.example.peliculas.storage.MovieStorage
import com.example.peliculas.viewmodels.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieViewModel: MovieViewModel by viewModels()
    private var movieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar películas guardadas
        val savedMovies = MovieStorage.loadMovies(this)
        movieViewModel.movies.addAll(savedMovies)
        if (savedMovies.isNotEmpty()) {
            movieId = savedMovies.maxOf { it.id } + 1
        }

        // Lista de géneros formateada
        val generos = Genero.values().map {
            it.name.lowercase().replace("_", " ").replaceFirstChar { c -> c.uppercase() }
        }

        // Adaptador para AutoCompleteTextView (dropdown estilo Material)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, generos)
        binding.autoCompleteGenero.setAdapter(adapter)

        binding.btnRegistrar.setOnClickListener {
            try {
                val titulo = binding.editTextTitulo.text.toString().trim()
                val anio = binding.editTextAnio.text.toString().trim()
                val resena = binding.editTextResena.text.toString().trim()

                // Validación de título
                when {
                    titulo.isEmpty() -> {
                        binding.editTextTitulo.error = "El título no puede estar vacío"
                        return@setOnClickListener
                    }
                    titulo.length < 2 -> {
                        binding.editTextTitulo.error = "El título es muy corto"
                        return@setOnClickListener
                    }
                    titulo.length > 100 -> {
                        binding.editTextTitulo.error = "El título es demasiado largo"
                        return@setOnClickListener
                    }
                    titulo.contains("\n") -> {
                        binding.editTextTitulo.error = "No se permiten saltos de línea"
                        return@setOnClickListener
                    }
                }

                // Validación de año
                if (anio.isEmpty()) {
                    binding.editTextAnio.error = "El año es obligatorio"
                    return@setOnClickListener
                }

                val anioInt = anio.toIntOrNull()
                if (anioInt == null || anioInt < 1900 || anioInt > 2050) {
                    binding.editTextAnio.error = "Año inválido (1900 - 2050)"
                    return@setOnClickListener
                }

                // Obtener género seleccionado
                val generoSeleccionado = binding.autoCompleteGenero.text.toString()
                val generoIndex = generos.indexOf(generoSeleccionado)
                if (generoIndex == -1) {
                    Toast.makeText(this, "Seleccione un género válido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val genero = Genero.values()[generoIndex]

                // Obtener rating
                val rating = binding.ratingBar.rating

                // Crear y guardar película
                val movie = Movie(movieId++, titulo, anio, resena, genero, rating)
                movieViewModel.movies.add(movie)
                MovieStorage.saveMovies(this, movieViewModel.movies)

                // Limpiar formulario
                binding.editTextTitulo.text.clear()
                binding.editTextAnio.text.clear()
                binding.editTextResena.text.clear()
                binding.autoCompleteGenero.setText("", false)
                binding.ratingBar.rating = 0f

                Toast.makeText(this, "Película registrada", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MovieListActivity::class.java)
                intent.putExtra("movies", ArrayList(movieViewModel.movies))
                startActivity(intent)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnVerPeliculas.setOnClickListener {
            val intent = Intent(this, MovieListActivity::class.java)
            intent.putExtra("movies", ArrayList(movieViewModel.movies))
            startActivityForResult(intent, 1002)
        }

        binding.btnCerrarApp.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == RESULT_OK && data != null) {
            val updatedMovies = data.getSerializableExtra("movies") as? ArrayList<Movie>
            if (updatedMovies != null) {
                movieViewModel.movies.clear()
                movieViewModel.movies.addAll(updatedMovies)
                MovieStorage.saveMovies(this, movieViewModel.movies)
            }
        }
    }
}
