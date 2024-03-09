package com.event.management.analytics.kafka.consumers;

import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.domain.User;
import com.event.management.analytics.repositories.OrganizersRepository;
import com.event.management.analytics.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class FollowingConsumer {

    final String ORGANIZER_FOLLOWED_TOPIC = "organizer-followed";
    final String ORGANIZER_UNFOLLOWED_TOPIC = "organizer-unfollowed";

    @Inject
    UsersRepository usersRepo;

    @Inject
    OrganizersRepository organizersRepo;

    @Topic(ORGANIZER_FOLLOWED_TOPIC)
    public void followedOrganizer(@KafkaKey Long organizerId, Long userId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Organizer> oOrganizer = organizersRepo.findById(organizerId);
        if (oOrganizer.isPresent() && oUser.isPresent()){
            Organizer organizer = oOrganizer.get();
            organizer.addFollower();
            organizersRepo.update(organizer);

            System.out.println("Added a follower to Organizer with id " + organizerId);
        }
    }

    @Topic(ORGANIZER_UNFOLLOWED_TOPIC)
    public void unfollowedOrganizer(@KafkaKey Long organizerId, Long userId){
        Optional<User> oUser = usersRepo.findById(userId);
        Optional<Organizer> oOrganizer = organizersRepo.findById(organizerId);
        if (oOrganizer.isPresent() && oUser.isPresent()){
            Organizer organizer = oOrganizer.get();
            organizer.removeFollower();
            organizersRepo.update(organizer);

            System.out.println("Added a follower to Organizer with id " + organizerId);
        }
    }
}
