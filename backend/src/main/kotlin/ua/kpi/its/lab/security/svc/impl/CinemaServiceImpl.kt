package ua.kpi.its.lab.security.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.security.entity.Cinema
import ua.kpi.its.lab.security.repo.CinemaRepository
import ua.kpi.its.lab.security.svc.CinemaService

@Service
class CinemaServiceImpl @Autowired constructor(
    private val cinemaRepository: CinemaRepository
) : CinemaService {
    override fun create(cinema: Cinema): Cinema {
        return cinemaRepository.save(cinema)
    }

    override fun getById(id: Long): Cinema? {
        return cinemaRepository.findById(id).orElse(null)
    }

    override fun update(cinema: Cinema): Cinema {
        return cinemaRepository.save(cinema)
    }

    override fun deleteById(id: Long) {
        cinemaRepository.deleteById(id)
    }

    override fun getAll(): List<Cinema> {
        return cinemaRepository.findAll()
    }
}
