package com.bornidea.pruebatecnicaupax.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bornidea.pruebatecnicaupax.model.database.entities.PeliculaEntity

@Dao
interface PeliculaDAO {

    @Query("SELECT * FROM peliculas_table")
    @Throws(Exception::class)
    fun getAllFilmsDataBase(): LiveData<List<PeliculaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Throws(Exception::class)
    suspend fun insertPeliculas(peliculas: List<PeliculaEntity>)

    @Query("DELETE FROM peliculas_table")
    @Throws(Exception::class)
    suspend fun deleteAll()

}