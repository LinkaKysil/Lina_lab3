package ua.kpi.its.lab.security.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.security.entity.Movie
import ua.kpi.its.lab.security.repo.MovieRepository
import ua.kpi.its.lab.security.svc.MovieService

@Service
class MovieServiceImpl @Autowired constructor(
    private val movieRepository: MovieRepository
) : MovieService {
    override fun create(movie: Movie): Movie {
        return movieRepository.save(movie)
    }

    override fun getById(id: Long): Movie? {
        return movieRepository.findById(id).orElse(null)
    }

    override fun update(movie: Movie): Movie {
        return movieRepository.save(movie)
    }

    override fun deleteById(id: Long) {
        movieRepository.deleteById(id)
    }

    override fun getAll(): List<Movie> {
        return movieRepository.findAll()
    }
}
