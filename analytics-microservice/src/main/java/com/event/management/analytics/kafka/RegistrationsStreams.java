package com.event.management.analytics.kafka;

import com.event.management.analytics.kafka.consumers.RegistrationsConsumer;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

@Factory
public class RegistrationsStreams {

    @Singleton
    public KStream<Long, Long> registrations(ConfiguredStreamBuilder builder){
        Properties properties = builder.getConfiguration();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "registrations-streams");
        properties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

        KStream<Long, Long> registrationsStream = builder.stream(RegistrationsConsumer.REGISTRATIONS_TOPIC);

        return registrationsStream;
    }
}
