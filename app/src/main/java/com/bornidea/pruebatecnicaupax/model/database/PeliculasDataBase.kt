package com.bornidea.pruebatecnicaupax.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bornidea.pruebatecnicaupax.model.database.dao.PeliculaDAO
import com.bornidea.pruebatecnicaupax.model.database.entities.PeliculaEntity

@Database(entities = [PeliculaEntity::class], version = 1)
abstract class PeliculasDataBase : RoomDatabase() {

    abstract val peliculaDAO: PeliculaDAO

    companion object {
        @Volatile
        private var INSTANCE: PeliculasDataBase? = null

        fun getInstance(context: Context): PeliculasDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PeliculasDataBase::class.java,
                        "peliculas_database"
                    ).build()
                }
                return instance
            }
        }
    }
}