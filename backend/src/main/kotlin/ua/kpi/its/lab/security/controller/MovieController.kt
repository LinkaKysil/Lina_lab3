package ua.kpi.its.lab.security.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.MovieService
import ua.kpi.its.lab.security.svc.CinemaService
import ua.kpi.its.lab.security.entity.Movie

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieService: MovieService,
    private val cinemaService: CinemaService
) {

    @PostMapping
    fun createMovie(@RequestBody requestDto: MovieRequestDto): ResponseEntity<MovieResponseDto> {
        val cinema = cinemaService.getById(requestDto.cinemaId)
        if (cinema != null) {
            val movie = movieService.create(
                Movie(
                    title = requestDto.title,
                    country = requestDto.country,
                    productionCompany = requestDto.productionCompany,
                    duration = requestDto.duration,
                    budget = requestDto.budget,
                    premiereDate = requestDto.premiereDate,
                    ageRestriction = requestDto.ageRestriction,
                    cinema = cinema
                )
            )
            val responseDto = MovieResponseDto(
                id = movie.id,
                title = movie.title,
                country = movie.country,
                productionCompany = movie.productionCompany,
                duration = movie.duration,
                budget = movie.budget,
                premiereDate = movie.premiereDate,
                ageRestriction = movie.ageRestriction,
                cinemaId = cinema.id
            )
            return ResponseEntity.ok(responseDto)
        } else {
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    fun getMovieById(@PathVariable id: Long): ResponseEntity<MovieResponseDto> {
        val movie = movieService.getById(id)
        return if (movie != null) {
            val responseDto = MovieResponseDto(
                id = movie.id,
                title = movie.title,
                country = movie.country,
                productionCompany = movie.productionCompany,
                duration = movie.duration,
                budget = movie.budget,
                premiereDate = movie.premiereDate,
                ageRestriction = movie.ageRestriction,
                cinemaId = movie.cinema.id
            )
            ResponseEntity.ok(responseDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateMovie(@PathVariable id: Long, @RequestBody requestDto: MovieRequestDto): ResponseEntity<MovieResponseDto> {
        val cinema = cinemaService.getById(requestDto.cinemaId)
        if (cinema != null) {
            val movie = movieService.update(
                Movie(
                    id = id,
                    title = requestDto.title,
                    country = requestDto.country,
                    productionCompany = requestDto.productionCompany,
                    duration = requestDto.duration,
                    budget = requestDto.budget,
                    premiereDate = requestDto.premiereDate,
                    ageRestriction = requestDto.ageRestriction,
                    cinema = cinema
                )
            )
            val responseDto = MovieResponseDto(
                id = movie.id,
                title = movie.title,
                country = movie.country,
                productionCompany = movie.productionCompany,
                duration = movie.duration,
                budget = movie.budget,
                premiereDate = movie.premiereDate,
                ageRestriction = movie.ageRestriction,
                cinemaId = cinema.id
            )
            return ResponseEntity.ok(responseDto)
        } else {
            return ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteMovie(@PathVariable id: Long): ResponseEntity<Void> {
        movieService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllMovies(): ResponseEntity<List<MovieResponseDto>> {
        val movies = movieService.getAll().map { movie ->
            MovieResponseDto(
                id = movie.id,
                title = movie.title,
                country = movie.country,
                productionCompany = movie.productionCompany,
                duration = movie.duration,
                budget = movie.budget,
                premiereDate = movie.premiereDate,
                ageRestriction = movie.ageRestriction,
                cinemaId = movie.cinema.id
            )
        }
        return ResponseEntity.ok(movies)
    }
}
