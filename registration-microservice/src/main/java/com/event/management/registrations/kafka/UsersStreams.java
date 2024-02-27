package com.event.management.registrations.kafka;

import com.event.management.registrations.dto.UserDTO;
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
public class UsersStreams {

    @Inject
    private SerdeRegistry serdeRegistry;

    @Singleton
    public KStream<Long, UserDTO> createdUsers(ConfiguredStreamBuilder builder){
        Properties props = builder.getConfiguration();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-created");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

        KStream<Long, UserDTO> usersStream = builder.stream("user-created", Consumed.with(Serdes.Long(), serdeRegistry.getSerde(UserDTO.class)));

        return usersStream;
    }
}
