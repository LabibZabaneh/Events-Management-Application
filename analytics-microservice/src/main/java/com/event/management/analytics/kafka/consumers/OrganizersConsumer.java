package com.event.management.analytics.kafka.consumers;

import com.event.management.analytics.domain.Organizer;
import com.event.management.analytics.dto.OrganizerDTO;
import com.event.management.analytics.repositories.OrganizersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class OrganizersConsumer {

    final String ORGANIZER_CREATED_TOPIC = "organizer-created";
    final String ORGANIZER_DELETED_TOPIC = "organizer-deleted";

    @Inject
    OrganizersRepository repo;

    @Topic(ORGANIZER_CREATED_TOPIC)
    public void createdOrganizer(@KafkaKey Long id, OrganizerDTO dto){
        Optional<Organizer> oOrganizer = repo.findById(id);
        if (oOrganizer.isEmpty()) {
            Organizer organizer = new Organizer();
            organizer.setId(id);
            organizer.setName(dto.getName());
            organizer.setFollowers(0);
            repo.save(organizer);
            System.out.println("Organizer created with id " + id);
        }
    }

    @Topic(ORGANIZER_DELETED_TOPIC)
    public void deletedOrganizer(@KafkaKey Long id, OrganizerDTO dto){
        Optional<Organizer> oOrganizer = repo.findById(id);
        if (oOrganizer.isPresent()){
            repo.deleteById(id);
            System.out.println("Organizer deleted with id " + id);
        }
    }
}
