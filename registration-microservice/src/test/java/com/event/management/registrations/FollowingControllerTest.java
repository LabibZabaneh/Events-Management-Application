package com.event.management.registrations;

import com.event.management.registrations.clients.FollowingClient;
import com.event.management.registrations.domain.Organizer;
import com.event.management.registrations.domain.User;
import com.event.management.registrations.repositories.OrganizersRepository;
import com.event.management.registrations.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.List;

import static com.event.management.registrations.RegistrationControllerTest.createUser;
import static com.event.management.registrations.RegistrationControllerTest.iterableToList;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void getInvalids(){
        assertNull(client.getOrganizerFollowers(0), "Should return null on an Invalid organizer id");
        assertNull(client.getUserFollowing(0), "Should return null on an Invalid organizer id");
    }

    @Test
    public void addOrganizerFollower(){
        User user = createUser();
        usersRepo.save(user);

        Organizer organizer = createOrganizer();
        organizersRepo.save(organizer);

        HttpResponse<Void> resp = client.addFollower(organizer.getId(), user.getId());
        assertEquals(resp.getStatus(), HttpStatus.OK, "Adding a follower should be successful");

        User u = usersRepo.findById(user.getId()).get();
        assertEquals(1, u.getFollowedOrganizers().size(), "There should be one followed organizer");
        assertEquals(organizer.getName(), u.getFollowedOrganizers().iterator().next().getName(), "The name of the organizations should be the same" );

        Organizer o = organizersRepo.findById(organizer.getId()).get();
        assertEquals(1, o.getFollowers().size(), "The organizer should have one follower");
        assertEquals(user.getEmail(), o.getFollowers().iterator().next().getEmail(), "The emails of the users should be the same");
    }

    @Test
    public void deleteOrganizerFollower(){
        User user = createUser();
        usersRepo.save(user);

        Organizer organizer = createOrganizer();
        organizersRepo.save(organizer);

        user.getFollowedOrganizers().add(organizer);
        organizer.getFollowers().add(user);

        usersRepo.update(user);
        organizersRepo.update(organizer);

        HttpResponse<Void> resp = client.deleteFollower(organizer.getId(), user.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "deleting a follower should be successful");

        User u = usersRepo.findById(user.getId()).get();
        assertEquals(0, u.getFollowedOrganizers().size(), "The user should have zero followed organizers");

        Organizer o = organizersRepo.findById(organizer.getId()).get();
        assertEquals(0, o.getFollowers().size(), "The organizer should have zero followers");
    }

    private Organizer createOrganizer(){
        Organizer organizer = new Organizer();
        organizer.setId(1L);
        organizer.setName("Test Organizer");
        organizer.setFollowers(new HashSet<>());
        return organizer;
    }


}
