package ua.kpi.its.lab.security.repo

import org.springframework.data.jpa.repository.JpaRepository
import ua.kpi.its.lab.security.entity.Cinema

interface CinemaRepository : JpaRepository<Cinema, Long>
