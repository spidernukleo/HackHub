package it.unicam.hackhub.application.service;

import it.unicam.hackhub.domain.Appointment;
import it.unicam.hackhub.domain.Team;
import it.unicam.hackhub.domain.User;
import it.unicam.hackhub.infrastructure.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MockCalendarService {
    private final AppointmentRepository appointmentRepository;

    public Appointment scheduleCall(User mentor, Team team, LocalDateTime start, LocalDateTime end) {
        if (start.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Starting date in past.");
        }
        if (start.isAfter(end) || start.isEqual(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ending date must be after starting date.");
        }

        boolean isAvailable = appointmentRepository.checkAvailability(mentor, start, end);
        if (!isAvailable) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have a call in that time slot");
        }

        Appointment appointment = new Appointment();
        appointment.setMentor(mentor);
        appointment.setTeam(team);
        appointment.setHackathon(team.getCurrentHackathon());
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        appointment.setUrl("https://mock-meet.com/call/" + UUID.randomUUID().toString().substring(0, 8));

        System.out.println("Call scheduled for hackathon: "+team.getCurrentHackathon().getName()+"\nMentor: "+mentor.getUsername()+"\nTeam: "+team.getName()+"\nLink: "+ appointment.getUrl());

        return appointmentRepository.save(appointment);
    }
}
