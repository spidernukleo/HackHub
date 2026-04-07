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
        if (start.isBefore(LocalDateTime.now()) || start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid time slot.");
        }

        if (appointmentRepository.checkAvailability(mentor, start, end)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The mentor already has a scheduled call in this time slot.");
        }

        Appointment appointment = new Appointment();
        appointment.setMentor(mentor);
        appointment.setTeam(team);
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        String meetingId = UUID.randomUUID().toString().substring(0, 8);
        appointment.setUrl("https://mock-meet.com/call/" + meetingId);

        System.out.println("Call schedulata con successo per l'hackathon "+sender.getHackathon().getName()+"\nMentor: "+mentorId+"\nTeam: "+sender.getTeam().getName()+"\nLink: "+ meetingUrl);

        return appointmentRepository.save(appointment);
    }
}
