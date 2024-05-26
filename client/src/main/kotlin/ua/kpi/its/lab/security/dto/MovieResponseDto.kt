package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseDto(
    val id: Long,
    val title: String,
    val country: String,
    val productionCompany: String,
    val duration: Int,
    val budget: Double,
    val premiereDate: String,
    val ageRestriction: Boolean,
    val cinemaId: Long
)
