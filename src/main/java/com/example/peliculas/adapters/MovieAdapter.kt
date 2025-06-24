//MovieAdapter.kt
package com.example.peliculas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.peliculas.R
import com.example.peliculas.databinding.ItemMovieBinding
import com.example.peliculas.models.Movie
import com.example.peliculas.models.Estado

class MovieAdapter(
    private val movieList: ArrayList<Movie>,
    private val onEdit: (movie: Movie, position: Int) -> Unit,
    private val onDelete: (position: Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.textViewTitulo.text = movie.titulo
            binding.textViewAnio.text = movie.anio
            binding.textViewGenero.text = movie.genero.name
            binding.textViewResena.text = movie.resena
            binding.ratingBarItem.rating = movie.rating
            binding.textViewEstado.text = "Estado: ${movie.estado.name.lowercase().replaceFirstChar { it.uppercase() }}"


            binding.btnMenu.setOnClickListener {
                val popup = PopupMenu(binding.root.context, binding.btnMenu)
                popup.inflate(R.menu.menu_item)

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit -> {
                            onEdit(movie, adapterPosition)
                            true
                        }
                        R.id.action_delete -> {
                            androidx.appcompat.app.AlertDialog.Builder(binding.root.context)
                                .setTitle("Confirmar eliminación")
                                .setMessage("¿Estás seguro de que querés eliminar esta película?")
                                .setPositiveButton("Sí") { _, _ ->
                                    onDelete(adapterPosition)
                                }
                                .setNegativeButton("Cancelar", null)
                                .show()
                            true
                        }
                        R.id.action_marcar_vista -> {
                            movie.estado = Estado.VISTA
                            notifyItemChanged(adapterPosition)
                            true
                        }
                        R.id.action_marcar_pendiente -> {
                            movie.estado = Estado.PENDIENTE
                            notifyItemChanged(adapterPosition)
                            true
                        }
                        R.id.action_marcar_favorita -> {
                            movie.estado = Estado.FAVORITA
                            notifyItemChanged(adapterPosition)
                            true
                        }
                        else -> false
                    }
                }


                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size

    fun updateList(nuevaLista: List<Movie>) {
        val copia = ArrayList(nuevaLista) //hace una copia segura
        movieList.clear()
        movieList.addAll(copia)
        notifyDataSetChanged()
    }
}
