package ua.kpi.its.lab.security.svc

import ua.kpi.its.lab.security.entity.Movie

interface MovieService {
    fun create(movie: Movie): Movie
    fun getById(id: Long): Movie?
    fun update(movie: Movie): Movie
    fun deleteById(id: Long)
    fun getAll(): List<Movie>
}
