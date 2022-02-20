package com.bornidea.pruebatecnicaupax.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bornidea.pruebatecnicaupax.model.data.Pelicula

@Entity(tableName = "peliculas_table")
data class PeliculaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "peliculaId")
    val peliculaId: Int = 0,
    @ColumnInfo(name = "adult")
    val adult: Boolean,
    @ColumnInfo(name = "backdrop_path")
    val backdrop_path: String,
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "original_language")
    val original_language: String,
    @ColumnInfo(name = "original_title")
    val original_title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "popularity")
    val popularity: Double,
    @ColumnInfo(name = "poster_path")
    val poster_path: String,
    @ColumnInfo(name = "release_date")
    val release_date: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "video")
    val video: Boolean,
    @ColumnInfo(name = "vote_average")
    val vote_average: Double,
    @ColumnInfo(name = "vote_count")
    val vote_count: Int
)

fun Pelicula.toDataBase() = PeliculaEntity(
    adult = adult,
    backdrop_path = backdrop_path,
    id = id,
    original_language = original_language,
    original_title = original_title,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    video = video,
    vote_average = vote_average,
    vote_count = vote_count
)