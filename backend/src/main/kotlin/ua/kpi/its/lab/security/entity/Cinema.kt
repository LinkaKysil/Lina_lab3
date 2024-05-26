package ua.kpi.its.lab.security.entity

import jakarta.persistence.*
import kotlin.Comparator

@Entity
data class Cinema(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,
    val address: String,
    val openingDate: String,
    val seats: Int,
    val screens: Int,
    val soundTechnology: String,
    val has3D: Boolean,

    @OneToMany(mappedBy = "cinema", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val movies: List<Movie> = emptyList()
) : Comparable<Cinema> {
    override fun compareTo(other: Cinema): Int {
        val nameComparison = name.compareTo(other.name)
        return if (nameComparison != 0) {
            nameComparison
        } else {
            id.compareTo(other.id)
        }
    }

    override fun toString(): String {
        return "Cinema(id=$id, name='$name', address='$address', openingDate='$openingDate', seats=$seats, screens=$screens, soundTechnology='$soundTechnology', has3D=$has3D)"
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
