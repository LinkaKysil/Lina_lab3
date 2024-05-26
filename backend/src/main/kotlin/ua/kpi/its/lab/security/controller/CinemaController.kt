package ua.kpi.its.lab.security.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.CinemaService
import ua.kpi.its.lab.security.entity.Cinema

@RestController
@RequestMapping("/api/cinemas")
class CinemaController(private val cinemaService: CinemaService) {

    @PostMapping
    fun createCinema(@RequestBody requestDto: CinemaRequestDto): ResponseEntity<CinemaResponseDto> {
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
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/{id}")
    fun getCinemaById(@PathVariable id: Long): ResponseEntity<CinemaResponseDto> {
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
            ResponseEntity.ok(responseDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateCinema(@PathVariable id: Long, @RequestBody requestDto: CinemaRequestDto): ResponseEntity<CinemaResponseDto> {
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
        return ResponseEntity.ok(responseDto)
    }

    @DeleteMapping("/{id}")
    fun deleteCinema(@PathVariable id: Long): ResponseEntity<Void> {
        cinemaService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllCinemas(): ResponseEntity<List<CinemaResponseDto>> {
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
        return ResponseEntity.ok(cinemas)
    }
}
