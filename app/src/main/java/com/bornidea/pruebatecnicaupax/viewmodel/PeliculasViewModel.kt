package com.bornidea.pruebatecnicaupax.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.bornidea.pruebatecnicaupax.model.data.Pelicula
import com.bornidea.pruebatecnicaupax.model.database.entities.PeliculaEntity
import com.bornidea.pruebatecnicaupax.model.network.PeliculasRepository
import com.bornidea.pruebatecnicaupax.model.network.isInternetAvailable
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalArgumentException

class PeliculasViewModel(private val repository: PeliculasRepository) : ViewModel() {

    /**RETROFIT*/

    fun getAllFilms(context: Context): LiveData<List<Pelicula>> {
        return if (!isInternetAvailable(context)) {
            repository.getEmpty()
        } else {
            repository.getAllFilmsAPI()
        }
    }

    /**ROOM*/
    val peliculasDB = repository.peliculas

    fun insertAll(peliculas: List<PeliculaEntity>) = viewModelScope.launch {
        try {
            repository.insertAllDatabase(peliculas)
        } catch (e: Exception) {
            Log.d("MyTag", e.message.toString())
        }

    }

    fun deleteAll() = viewModelScope.launch {
        try {
            repository.deleteAllDatabase()
        } catch (e: Exception) {
            Log.d("MyTag", e.message.toString())
        }
    }
}

class PeliculasViewModelFactory(private val repository: PeliculasRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeliculasViewModel::class.java))
            return PeliculasViewModel(repository) as T
        throw IllegalArgumentException("Se desconoce el ViewModel")
    }
}