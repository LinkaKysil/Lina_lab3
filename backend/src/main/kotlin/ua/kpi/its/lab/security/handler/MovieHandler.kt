package ua.kpi.its.lab.security.handler

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.MovieService
import ua.kpi.its.lab.security.svc.CinemaService
import ua.kpi.its.lab.security.entity.Movie

@Component
class MovieHandler(
    private val movieService: MovieService,
    private val cinemaService: CinemaService
) {

    fun createMovieHandler(request: ServerRequest): ServerResponse {
        val requestDto = request.body(MovieRequestDto::class.java)
        val cinema = cinemaService.getById(requestDto.cinemaId)
        return if (cinema != null) {
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
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.badRequest().build()
        }
    }

    fun getMovieByIdHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
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
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.notFound().build()
        }
    }

    fun updateMovieHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val requestDto = request.body(MovieRequestDto::class.java)
        val cinema = cinemaService.getById(requestDto.cinemaId)
        return if (cinema != null) {
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
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.badRequest().build()
        }
    }

    fun deleteMovieHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        movieService.deleteById(id)
        return ServerResponse.noContent().build()
    }

    fun getAllMoviesHandler(request: ServerRequest): ServerResponse {
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
        return ServerResponse.ok().body(movies)
    }
}
