package com.bornidea.pruebatecnicaupax.model.data


data class Tmdb(
    val page: Int,
    val results: List<Pelicula>,
    val total_pages: Int,
    val total_results: Int
)