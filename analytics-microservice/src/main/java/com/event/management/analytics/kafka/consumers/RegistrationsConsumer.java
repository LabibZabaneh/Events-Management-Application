package com.event.management.analytics.kafka;

import com.event.management.analytics.domain.Event;
import com.event.management.analytics.domain.User;
import com.event.management.analytics.repositories.EventsRepository;
import com.event.management.analytics.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class RegistrationsConsumer {

    static final String USER_REGISTERED_TOPIC = "user-registered";
    static final String USER_UNREGISTERED_TOPIC = "user-unregistered";

    @Inject
    UsersRepository usersRepo;

    @Inject
    EventsRepository eventsRepo;

    @Topic(USER_REGISTERED_TOPIC)
    public void registerUser(@KafkaKey Long userId, Long eventId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        if (oUser.isPresent() && oEvent.isPresent()){
            User user = oUser.get();
            user.addRegistration();
            usersRepo.update(user);

            Event event = oEvent.get();
            event.addRegistration();
            eventsRepo.update(event);

            System.out.println("User with id " + userId + "registered to event with id " + eventId);
        }
    }

    @Topic(USER_UNREGISTERED_TOPIC)
    public void unregisterUser(@KafkaKey Long userId, Long eventId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Event> oEvent = eventsRepo.findById(eventId);
        if (oUser.isPresent() && oEvent.isPresent()){
            User user = oUser.get();
            user.removeRegistration();
            usersRepo.update(user);

            Event event = oEvent.get();
            event.deleteRegistration();
            eventsRepo.update(event);

            System.out.println("User with id " + userId + "unregistered to event with id " + eventId);
        }
    }
}
