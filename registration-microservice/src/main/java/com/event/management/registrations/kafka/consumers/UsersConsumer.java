package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.User;
import com.event.management.registrations.dto.UserDTO;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class UsersConsumer {

    @Inject
    UsersRepository usersRepo;

    final String USER_CREATED_TOPIC = "user-created";
    final String USER_DELETED_TOPIC = "user-deleted";

    @Topic(USER_CREATED_TOPIC)
    public void createdUser(@KafkaKey Long id, UserDTO dto){
        Optional<User> oUser = usersRepo.findById(id);
        if (oUser.isEmpty()){
           User user = new User();
           user.setId(id);
           user.setFirstName(dto.getFirstName());
           user.setEmail(dto.getEmail());
           usersRepo.save(user);

           System.out.println("User created with id" + id);
        }
    }

    @Topic(USER_DELETED_TOPIC)
    public void deletedUser(@KafkaKey Long id, UserDTO dto){
        Optional<User> oUser = usersRepo.findById(id);
        if (oUser.isPresent()){
            usersRepo.deleteById(id);

            System.out.println("User deleted with id" + id);
        }
    }
}
