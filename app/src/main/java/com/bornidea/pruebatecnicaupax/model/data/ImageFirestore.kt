package com.bornidea.pruebatecnicaupax.model.data

data class ImageFirestore(
    var isSuccess: Boolean = false,
    var documentId: String? = null,
    var photoUrl: String = ""
)
