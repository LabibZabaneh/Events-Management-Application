package com.event.management.events.kafka;

import com.event.management.events.dto.EventDTO;
import io.micronaut.configuration.kafka.serde.SerdeRegistry;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

@Factory
public class EventsStreams {

    @Inject
    private SerdeRegistry serdeRegistry;

    @Singleton
    public KStream<Long, EventDTO> postedEvents(ConfiguredStreamBuilder builder) {
        Properties props = builder.getConfiguration();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "event-posted-streams");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

        KStream<Long, EventDTO> postedEventsStream = builder.stream(EventsProducer.EVENT_POSTED_TOPIC, Consumed.with(Serdes.Long(), serdeRegistry.getSerde(EventDTO.class)));

        return postedEventsStream;
    }
}
