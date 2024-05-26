package ua.kpi.its.lab.security.entity

import jakarta.persistence.*

@Entity
data class Movie(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,
    val country: String,
    val productionCompany: String,
    val duration: Int,
    val budget: Double,
    val premiereDate: String,
    val ageRestriction: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    val cinema: Cinema
)
