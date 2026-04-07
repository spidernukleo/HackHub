package it.unicam.hackhub.infrastructure.repository;

import it.unicam.hackhub.domain.Appointment;
import it.unicam.hackhub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a WHERE a.mentor = :mentor " +
            "AND a.startTime < :endTime AND a.endTime > :startTime")
    boolean checkAvailability(@Param("mentor") User mentor,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);
}
