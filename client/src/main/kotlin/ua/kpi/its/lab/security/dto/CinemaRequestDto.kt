package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable
data class CinemaRequestDto(
    val name: String,
    val address: String,
    val openingDate: String,
    val seats: Int,
    val screens: Int,
    val soundTechnology: String,
    val has3D: Boolean
)
