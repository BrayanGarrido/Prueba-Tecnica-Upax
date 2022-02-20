package com.bornidea.pruebatecnicaupax.model.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bornidea.pruebatecnicaupax.model.data.Pelicula
import com.bornidea.pruebatecnicaupax.model.database.dao.PeliculaDAO
import com.bornidea.pruebatecnicaupax.model.database.entities.PeliculaEntity
import com.bornidea.pruebatecnicaupax.util.Constants.API_KEY
import com.bornidea.pruebatecnicaupax.util.Constants.LANGUAGE
import com.bornidea.pruebatecnicaupax.util.Constants.NUMBER_PAGE

class PeliculasRepository(private val dao: PeliculaDAO) {

    fun getAllFilmsAPI(): LiveData<List<Pelicula>> {
        val refService = RetrofitInstance.getRetrofitInstance()
            .create(PeliculasService::class.java)

        val responseLiveData: LiveData<List<Pelicula>> = liveData {
            val response =
                refService.getPopularFilms(API_KEY, LANGUAGE, NUMBER_PAGE)
            val bodyResponse = response.body()?.results
            emit(bodyResponse ?: emptyList())
        }
        return responseLiveData
    }

    fun getEmpty(): LiveData<List<Pelicula>> {
        val responseLiveData: LiveData<List<Pelicula>> = liveData {
            emit(emptyList())
        }
        return responseLiveData
    }

    val peliculas = dao.getAllFilmsDataBase()

    suspend fun insertAllDatabase(peliculas: List<PeliculaEntity>) =
        dao.insertPeliculas(peliculas)

    suspend fun deleteAllDatabase() = dao.deleteAll()
}