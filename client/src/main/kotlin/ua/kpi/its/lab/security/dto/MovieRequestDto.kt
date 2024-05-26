package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieRequestDto(
    val title: String,
    val country: String,
    val productionCompany: String,
    val duration: Int,
    val budget: Double,
    val premiereDate: String,
    val ageRestriction: Boolean,
    val cinemaId: Long
)
