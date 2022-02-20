package com.bornidea.pruebatecnicaupax.model.network

import com.bornidea.pruebatecnicaupax.model.data.Tmdb
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PeliculasService {
    @GET("popular")
    suspend fun getPopularFilms(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<Tmdb>
}