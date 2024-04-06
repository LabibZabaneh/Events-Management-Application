package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.Event;
import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.domain.TicketCategory;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.EventsRepository;
import com.event.management.registrations.repositories.TicketCategoriesRepository;
import com.event.management.registrations.repositories.TicketsRepository;
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
    final String PAYMENT_UNSUCCESSFUL_TOPIC = "payment-unsuccessful";

    @Inject
    TicketsRepository ticketsRepo;

    @Inject
    UsersRepository usersRepo;

    @Inject
    TicketCategoriesRepository ticketCategoriesRepo;

    @Transactional
    @Topic(PAYMENT_SUCCESSFUL_TOPIC)
    public void paymentSuccessful(@KafkaKey Long ticketId, String message){
        Optional<Ticket> oTicket = ticketsRepo.findById(ticketId);
        if (oTicket.isPresent()){
            Ticket ticket = oTicket.get();
            User user = ticket.getUser();
            TicketCategory ticketCategory = ticket.getTicketCategory();

            if (ticketCategory.getReservedTickets().contains(ticket)){
                // Change ticket to paid
                ticketCategory.getReservedTickets().remove(ticket);
                ticketCategory.getSoldTickets().add(ticket);
                ticketCategoriesRepo.update(ticketCategory);

                // Add ticket to user
                user.getTickets().add(ticket);
                usersRepo.update(user);

                System.out.println("Ticket with id " + ticketId + " has changed status from reserved to paid");
            } else {
                System.out.println("Ticket not found in reserved tickets");
            }
        }
    }
}
