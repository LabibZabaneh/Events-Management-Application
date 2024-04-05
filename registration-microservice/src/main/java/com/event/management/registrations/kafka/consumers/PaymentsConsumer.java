package com.event.management.registrations.kafka.consumers;

import com.event.management.registrations.domain.Ticket;
import com.event.management.registrations.repositories.PaidTicketsRepository;
import com.event.management.registrations.repositories.ReservedTicketsRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;

import java.util.Optional;

@KafkaListener
public class PaymentsConsumer {

    final String PAYMENT_SUCCESSFUL_TOPIC = "payment-successful";
    final String PAYMENT_UNSUCCESSFUL_TOPIC = "payment-un-successful";

    @Inject
    PaidTicketsRepository paidTicketsRepo;

    @Inject
    ReservedTicketsRepository reservedTicketsRepo;

    @Topic(PAYMENT_SUCCESSFUL_TOPIC)
    public void paymentSuccessful(@KafkaKey Long ticketId, String message){
        Optional<Ticket> oTicket = reservedTicketsRepo.findById(ticketId);
        if (oTicket.isPresent()){
            Ticket ticket = oTicket.get();
            reservedTicketsRepo.delete(ticket);
            paidTicketsRepo.save(ticket);

            System.out.println("Ticket with id " + ticketId + " has changed status from reserved to paid");
        }
    }
}
