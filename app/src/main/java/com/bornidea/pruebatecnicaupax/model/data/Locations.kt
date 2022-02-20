package com.bornidea.pruebatecnicaupax.model.data

import com.google.gson.annotations.SerializedName

data class Locations(
    @SerializedName("Fecha")
    val Fecha:String,
    @SerializedName("Hora")
    val Hora:String,
    @SerializedName("Latitud")
    val Latitud:String,
    @SerializedName("Longitud")
    val Longitud:String
)
