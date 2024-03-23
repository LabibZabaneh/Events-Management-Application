package com.event.management.analytics.kafka.consumers;

import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.domain.User;
import com.event.management.analytics.dto.UserDTO;
import com.event.management.analytics.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class UsersConsumer {

    final String USER_CREATED_TOPIC = "user-created";
    final String USER_DELETED_TOPIC = "user-deleted";

    @Inject
    UsersRepository repo;

    @Topic(USER_CREATED_TOPIC)
    public void createdUser(@KafkaKey Long id, UserDTO dto){
        Optional<User> oUser = repo.findById(id);
        if (oUser.isEmpty()){
            User user = new User();
            user.setId(id);
            user.setFirstName(dto.getFirstName());
            user.setRegistrations(0);
            user.setDateOfBirth(dto.getDateOfBirth());
            user.setGender(dto.getGender());
            repo.save(user);

            System.out.println("User added with id " + id);
        }
    }

    @Topic(USER_DELETED_TOPIC)
    public void deletedUser(@KafkaKey Long id, UserDTO dto){
        Optional<User> oUser = repo.findById(id);
        if (oUser.isEmpty()){
            repo.deleteById(id);

            System.out.println("User deleted with id " + id);
        }
    }
}
