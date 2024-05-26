package ua.kpi.its.lab.security.svc

import ua.kpi.its.lab.security.entity.Cinema

interface CinemaService {
    fun create(cinema: Cinema): Cinema
    fun getById(id: Long): Cinema?
    fun update(cinema: Cinema): Cinema
    fun deleteById(id: Long)
    fun getAll(): List<Cinema>
}
