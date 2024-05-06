package com.example.airlineproject.service.impl;

import com.example.airlineproject.entity.Flight;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.Status;
import com.example.airlineproject.repository.FlightRepository;
import com.example.airlineproject.repository.UserRepository;
import com.example.airlineproject.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final FlightRepository flightRepository;


    @Scheduled(cron = "0 0 0 * * *")
    public void sendBirthdayGreetings() {
        List<User> users = userRepository.findUsersByBirthdayToday();
        users.forEach(mailService::sendBirthdayMail);
    }


    @Scheduled(fixedRate = 3600000)
    public void updateFlightStatus() {
        List<Flight> flights = flightRepository.findAllByStatusNot(Status.ARRIVED);
        LocalDateTime currentTime = LocalDateTime.now();
        for (Flight flight : flights) {
            if (currentTime.isAfter(flight.getScheduledTime())) {
                if (flight.getArrivalTime() != null && currentTime.isAfter(flight.getArrivalTime())) {
                    flight.setStatus(Status.ARRIVED);
                } else if (flight.getEstimatedTime() != null && currentTime.isAfter(flight.getEstimatedTime())) {
                    flight.setStatus(Status.PREMATURE);
                } else {
                    flight.setStatus(Status.ON_TIME);
                }
                flightRepository.save(flight);
            }
        }
    }

}
