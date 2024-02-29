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

    @Topic("user-created")
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
}
