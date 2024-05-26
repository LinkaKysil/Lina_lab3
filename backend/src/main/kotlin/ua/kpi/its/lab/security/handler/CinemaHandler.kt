package ua.kpi.its.lab.security.handler

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.CinemaService
import ua.kpi.its.lab.security.entity.Cinema

@Component
class CinemaHandler(private val cinemaService: CinemaService) {

    fun createCinemaHandler(request: ServerRequest): ServerResponse {
        val requestDto = request.body(CinemaRequestDto::class.java)
        val cinema = cinemaService.create(
            Cinema(
                name = requestDto.name,
                address = requestDto.address,
                openingDate = requestDto.openingDate,
                seats = requestDto.seats,
                screens = requestDto.screens,
                soundTechnology = requestDto.soundTechnology,
                has3D = requestDto.has3D
            )
        )
        val responseDto = CinemaResponseDto(
            id = cinema.id,
            name = cinema.name,
            address = cinema.address,
            openingDate = cinema.openingDate,
            seats = cinema.seats,
            screens = cinema.screens,
            soundTechnology = cinema.soundTechnology,
            has3D = cinema.has3D
        )
        return ServerResponse.ok().body(responseDto)
    }

    fun getCinemaByIdHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val cinema = cinemaService.getById(id)
        return if (cinema != null) {
            val responseDto = CinemaResponseDto(
                id = cinema.id,
                name = cinema.name,
                address = cinema.address,
                openingDate = cinema.openingDate,
                seats = cinema.seats,
                screens = cinema.screens,
                soundTechnology = cinema.soundTechnology,
                has3D = cinema.has3D
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.notFound().build()
        }
    }

    fun updateCinemaHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val requestDto = request.body(CinemaRequestDto::class.java)
        val cinema = cinemaService.update(
            Cinema(
                id = id,
                name = requestDto.name,
                address = requestDto.address,
                openingDate = requestDto.openingDate,
                seats = requestDto.seats,
                screens = requestDto.screens,
                soundTechnology = requestDto.soundTechnology,
                has3D = requestDto.has3D
            )
        )
        val responseDto = CinemaResponseDto(
            id = cinema.id,
            name = cinema.name,
            address = cinema.address,
            openingDate = cinema.openingDate,
            seats = cinema.seats,
            screens = cinema.screens,
            soundTechnology = cinema.soundTechnology,
            has3D = cinema.has3D
        )
        return ServerResponse.ok().body(responseDto)
    }

    fun deleteCinemaHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        cinemaService.deleteById(id)
        return ServerResponse.noContent().build()
    }

    fun getAllCinemasHandler(request: ServerRequest): ServerResponse {
        val cinemas = cinemaService.getAll().map { cinema ->
            CinemaResponseDto(
                id = cinema.id,
                name = cinema.name,
                address = cinema.address,
                openingDate = cinema.openingDate,
                seats = cinema.seats,
                screens = cinema.screens,
                soundTechnology = cinema.soundTechnology,
                has3D = cinema.has3D
            )
        }
        return ServerResponse.ok().body(cinemas)
    }
}
