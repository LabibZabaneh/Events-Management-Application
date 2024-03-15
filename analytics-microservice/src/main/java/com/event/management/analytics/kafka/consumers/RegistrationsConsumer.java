package com.event.management.analytics.kafka.consumers;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.User;
import com.event.management.analytics.repositories.EventsRepository;
import com.event.management.analytics.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@KafkaListener
public class RegistrationsConsumer {

    public static final String REGISTRATIONS_TOPIC = "registrations";
    public static final String UNREGISTRATIONS_TOPIC = "unregistrations";

    @Inject
    UsersRepository usersRepo;

    @Inject
    EventsRepository eventsRepo;

    @Topic(REGISTRATIONS_TOPIC)
    public void registerUser(@KafkaKey Long userId, Long eventId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        if (oUser.isPresent() && oEvent.isPresent()){
            User user = oUser.get();
            user.addRegistration();
            usersRepo.update(user);

            Event event = oEvent.get();
            event.addRegistration(dateOfBirthToAge(user.getDateOfBirth()));
            eventsRepo.update(event);

            System.out.println("User with id " + userId + "registered to event with id " + eventId);
        }
    }

    @Topic(UNREGISTRATIONS_TOPIC)
    public void unregisterUser(@KafkaKey Long userId, Long eventId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        if (oUser.isPresent() && oEvent.isPresent()){
            User user = oUser.get();
            user.removeRegistration();
            usersRepo.update(user);

            Event event = oEvent.get();
            event.deleteRegistration(dateOfBirthToAge(user.getDateOfBirth()));
            eventsRepo.update(event);

            System.out.println("User with id " + userId + "unregistered to event with id " + eventId);
        }
    }

    private int dateOfBirthToAge(LocalDate dateOfBirth){
        LocalDate today = LocalDate.now();
        Period period = Period.between(dateOfBirth, today);
        return period.getYears();
    }
}
