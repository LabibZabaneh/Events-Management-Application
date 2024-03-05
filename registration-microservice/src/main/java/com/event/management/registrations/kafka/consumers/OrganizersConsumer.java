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

    @Topic("organizer-created")
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

}
