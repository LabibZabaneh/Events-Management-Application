package com.event.management.registrations;

import com.event.management.registrations.clients.FollowingClient;
import com.event.management.registrations.domain.Organizer;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.OrganizersRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static com.event.management.registrations.RegistrationControllerTest.createUser;
import static com.event.management.registrations.RegistrationControllerTest.iterableToList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(transactional = false)
public class FollowingControllerTest {

    @Inject
    FollowingClient client;

    @Inject
    UsersRepository usersRepo;

    @Inject
    OrganizersRepository organizersRepo;

    @BeforeEach
    public void clean(){
        usersRepo.deleteAll();
        organizersRepo.deleteAll();
    }

    @Test
    public void noFollowers(){
        User user = createUser();
        usersRepo.save(user);

        Organizer organizer = createOrganizer();
        organizersRepo.save(organizer);

        assertFalse(client.getUserFollowing(user.getId()).iterator().hasNext(), "There should not be any followed organizers");
        assertFalse(client.getOrganizerFollowers(organizer.getId()).iterator().hasNext(), "There should not be any followed users");
    }

    @Test
    public void oneFollower(){
        User user = createUser();
        usersRepo.save(user);

        Organizer organizer = createOrganizer();
        organizersRepo.save(organizer);

        user.getFollowedOrganizers().add(organizer);
        organizer.getFollowers().add(user);

        usersRepo.update(user);
        organizersRepo.update(organizer);

        List<Organizer> organizers = iterableToList(client.getUserFollowing(user.getId()));
        assertEquals(organizers.size(), 1, "There should be only one followed organizer");
        assertEquals(organizers.get(0).getId(), organizer.getId(), "The organizer ids should be the same");

        List<User> users = iterableToList(client.getOrganizerFollowers(organizer.getId()));
        assertEquals(users.size(), 1, "There should be only one user follower");
        assertEquals(users.get(0).getId(), user.getId(), "The user ids should be the same");
    }

    private Organizer createOrganizer(){
        Organizer organizer = new Organizer();
        organizer.setId(1L);
        organizer.setName("Test Organizer");
        organizer.setFollowers(new HashSet<>());
        return organizer;
    }


}
