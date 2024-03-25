package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.Organizer;
import com.event.management.registrations.dto.OrganizerDTO;
import com.event.management.registrations.repositories.OrganizersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class OrganizersConsumer {

    @Inject
    OrganizersRepository organizersRepo;

    final String ORGANIZER_CREATED_TOPIC = "organizer-created";
    final String ORGANIZER_DELETED_TOPIC = "organizer-deleted";

    @Topic(ORGANIZER_CREATED_TOPIC)
    public void createdOrganizer(@KafkaKey Long id, OrganizerDTO dto){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isEmpty()){
            Organizer organizer = new Organizer();
            organizer.setId(id);
            organizer.setName(dto.getName());
            organizersRepo.save(organizer);

            System.out.println("Organizer added with id" + id);
        }
    }

    @Topic(ORGANIZER_DELETED_TOPIC)
    public void deletedOrganizer(@KafkaKey Long id, OrganizerDTO dto){
        Optional<Organizer> oOrganizer = organizersRepo.findById(id);
        if (oOrganizer.isPresent()){
            organizersRepo.deleteById(id);

            System.out.println("Organizer deleted with id " + id);
        }
    }

}
