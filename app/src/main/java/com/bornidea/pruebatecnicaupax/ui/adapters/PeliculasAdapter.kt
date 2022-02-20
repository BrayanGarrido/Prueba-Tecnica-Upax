package com.bornidea.pruebatecnicaupax.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bornidea.pruebatecnicaupax.R
import com.bornidea.pruebatecnicaupax.databinding.TemplateListFilmBinding
import com.bornidea.pruebatecnicaupax.model.data.Pelicula
import com.bornidea.pruebatecnicaupax.util.Constants.RATING
import com.bornidea.pruebatecnicaupax.util.Constants.URL_IMAGEN
import com.squareup.picasso.Picasso

class PeliculasAdapter(private val clickListener: (Pelicula) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

    private val peliculaList = ArrayList<Pelicula>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: TemplateListFilmBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.template_list_film, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(peliculaList[position], clickListener)
    }

    override fun getItemCount(): Int = peliculaList.size

    fun setList(peliculas: List<Pelicula>) {
        peliculaList.clear()
        peliculaList.addAll(peliculas)
    }
}

class ViewHolder(val binding: TemplateListFilmBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(pelicula: Pelicula, clickListener: (Pelicula) -> Unit) {
        binding.apply {
            Picasso.get().load(URL_IMAGEN + pelicula.poster_path).into(imFilm)
            tvTitleFilm.text = pelicula.title
            val rating = pelicula.vote_average.toString() + RATING
            tvRating.text = rating
            container.setOnClickListener {
                clickListener(pelicula)
            }
        }
    }
}