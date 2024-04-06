package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.PaidTicketsRepository;
import com.event.management.registrations.repositories.ReservedTicketsRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import javax.transaction.Transactional;
import java.util.Optional;

@KafkaListener
public class PaymentsConsumer {

    final String PAYMENT_SUCCESSFUL_TOPIC = "payment-successful";
    final String PAYMENT_UNSUCCESSFUL_TOPIC = "payment-un-successful";

    @Inject
    PaidTicketsRepository paidTicketsRepo;

    @Inject
    ReservedTicketsRepository reservedTicketsRepo;

    @Inject
    UsersRepository usersRepo;

    @Inject
    EventsRepository eventsRepo;

    @Transactional
    @Topic(PAYMENT_SUCCESSFUL_TOPIC)
    public void paymentSuccessful(@KafkaKey Long ticketId, String message){
        Optional<Ticket> oTicket = reservedTicketsRepo.findById(ticketId);
        if (oTicket.isPresent()){
            Ticket ticket = oTicket.get();
            User user = ticket.getUser();
            Event event = ticket.getEvent();

            user.getReservedTickets().remove(ticket);
            user.getTickets().add(ticket);
            usersRepo.update(user);

            event.getReservedTickets().remove(ticket);
            event.getSoldTickets().add(ticket);

            eventsRepo.update(event);



            reservedTicketsRepo.delete(ticket);
            paidTicketsRepo.save(ticket);

            System.out.println("Ticket with id " + ticketId + " has changed status from reserved to paid");
        }
    }
}
