package com.example.peliculas.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.peliculas.databinding.ActivityEditMovieBinding
import com.example.peliculas.models.Genero
import com.example.peliculas.models.Movie

class EditMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMovieBinding
    private var movieIndex = -1
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos de la película a editar
        @Suppress("DEPRECATION")
        movie = intent.getSerializableExtra("movie") as Movie
        movieIndex = intent.getIntExtra("index", -1)

        // Preparar lista de géneros con formato bonito
        val generos = Genero.values().map {
            it.name.lowercase().replace("_", " ").replaceFirstChar { c -> c.uppercase() }
        }

        // Adaptador para AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, generos)
        binding.autoCompleteGenero.setAdapter(adapter)

        // Mostrar datos actuales en los controles
        binding.editTextTitulo.setText(movie.titulo)
        binding.editTextAnio.setText(movie.anio)
        binding.editTextResena.setText(movie.resena)
        binding.autoCompleteGenero.setText(generos[movie.genero.ordinal], false)
        binding.ratingBar.rating = movie.rating

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            val nuevoTitulo = binding.editTextTitulo.text.toString().trim()
            val nuevoAnio = binding.editTextAnio.text.toString().trim()
            val nuevaResena = binding.editTextResena.text.toString().trim()
            val generoSeleccionado = binding.autoCompleteGenero.text.toString()
            val generoIndex = generos.indexOf(generoSeleccionado)

            // Validar género
            if (generoIndex == -1) {
                binding.autoCompleteGenero.error = "Seleccione un género válido"
                return@setOnClickListener
            }

            val nuevoGenero = Genero.values()[generoIndex]
            val nuevoRating = binding.ratingBar.rating

            // Validaciones título
            when {
                nuevoTitulo.isEmpty() -> {
                    binding.editTextTitulo.error = "El título no puede estar vacío"
                    return@setOnClickListener
                }
                nuevoTitulo.length < 2 -> {
                    binding.editTextTitulo.error = "El título es muy corto"
                    return@setOnClickListener
                }
                nuevoTitulo.length > 100 -> {
                    binding.editTextTitulo.error = "El título es demasiado largo"
                    return@setOnClickListener
                }
                nuevoTitulo.contains("\n") -> {
                    binding.editTextTitulo.error = "No se permiten saltos de línea"
                    return@setOnClickListener
                }
            }

            // Validación año
            if (nuevoAnio.isEmpty()) {
                binding.editTextAnio.error = "El año es obligatorio"
                return@setOnClickListener
            }

            val anioInt = nuevoAnio.toIntOrNull()
            if (anioInt == null || anioInt < 1900 || anioInt > 2050) {
                binding.editTextAnio.error = "Año inválido (1900 - 2050)"
                return@setOnClickListener
            }

            // Crear película editada
            val movieEditada = Movie(movie.id, nuevoTitulo, nuevoAnio, nuevaResena, nuevoGenero, nuevoRating, movie.estado)
            val resultIntent = Intent().apply {
                putExtra("movie", movieEditada)
                putExtra("index", movieIndex)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Botón Eliminar
        binding.btnEliminar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que querés eliminar esta película?")
                .setPositiveButton("Sí") { _, _ ->
                    val resultIntent = Intent().apply {
                        putExtra("delete_index", movieIndex)
                    }
                    setResult(Activity.RESULT_FIRST_USER, resultIntent)
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Botón Volver
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
