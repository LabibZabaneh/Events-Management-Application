package com.event.management.events;

import com.event.management.events.clients.UsersClient;
import com.event.management.events.domain.Gender;
import com.event.management.events.domain.User;
import com.event.management.events.dto.UserDTO;
import com.event.management.events.repositories.UsersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(transactional = false)
public class UserControllerTest {

    @Inject
    UsersClient client;

    @Inject
    UsersRepository repo;

    @BeforeEach
    public void clean(){
        repo.deleteAll();
    }

    @Test
    public void noUsers() {
        Iterable<User> users = client.list();
        assertFalse(users.iterator().hasNext(), "Service should not list any users initially");
    }

    @Test
    public void addUser(){
        UserDTO dto = createUserDTO("Joe", "Doe", "joe.doe@example.com", "+44 1111111111", LocalDate.of(2003, 5, 10 ), Gender.MALE);

        HttpResponse<Void> resp = client.addUser(dto);
        assertEquals(HttpStatus.CREATED, resp.getStatus(), "Creation should be successful");

        List<User> users = iterableToList(repo.findAll());
        assertEquals(1, users.size(), "There should only be one total user");

        User u = users.get(0);
        compareUsers(u, dto);
    }

    @Test
    public void getUser(){
        User u = createUser("Smith", "Dan", "smith.dan@example.com", "+44 0000000000", LocalDate.of(2003, 5, 10 ), Gender.MALE);
        repo.save(u);

        User repoUser =  client.getUser(u.getId());
        compareUsers(u, repoUser);
    }

    @Test
    public void updateUser(){
        User u = createUser("Smith", "Dan", "smith.dan@example.com", "+44 0000000000", LocalDate.of(2003, 5, 10 ), Gender.MALE);
        repo.save(u);

        UserDTO dto = createUserDTO("Joe", "Doe", "joe.doe@example.com", "+44 1111111111", LocalDate.of(2005, 2, 22 ), Gender.FEMALE);

        HttpResponse<Void> resp = client.updateUser(u.getId(), dto);
        assertEquals(HttpStatus.OK, resp.getStatus(), "Update should be successful");

        User repoUser = repo.findById(u.getId()).get();
        compareUsers(repoUser, dto);
    }

    @Test
    public void deleteUser(){
        User u = createUser("Smith", "Dan", "smith.dan@example.com", "+44 0000000000", LocalDate.of(2003, 5, 10 ), Gender.MALE);
        repo.save(u);

        HttpResponse<Void> resp = client.deleteUser(u.getId());
        assertEquals(HttpStatus.OK, resp.getStatus(), "Deletion should be successful");

        assertFalse(repo.existsById(u.getId()));
    }

    private void compareUsers(User u1, User u2){
        assertEquals(u1.getFirstName(), u2.getFirstName(), "First names should match");
        assertEquals(u1.getLastName(), u2.getLastName(), "Last names should match");
        assertEquals(u1.getEmail(), u2.getEmail(), "Emails should match");
        assertEquals(u1.getMobileNumber(), u2.getMobileNumber(), "Mobile numbers should match");
        assertEquals(u1.getDateOfBirth(), u2.getDateOfBirth(), "Dates of birth should match");
    }

    private  void compareUsers(User u, UserDTO dto){
        assertEquals(dto.getFirstName(), u.getFirstName(), "The first name should match the one in the dto");
        assertEquals(dto.getLastName(), u.getLastName(), "The last name should match the one in the dto");
        assertEquals(dto.getEmail(), u.getEmail(), "The email should match the one in the dto");
        assertEquals(dto.getMobileNumber(), u.getMobileNumber(), "The mobile number should match the one in the dto");
        assertEquals(dto.getDateOfBirth(), u.getDateOfBirth(), "The date of birth should match the one in the dto");
        assertEquals(dto.getGender(), u.getGender(), "The gender should match the one in the dto");
    }

    private UserDTO createUserDTO(String firstName, String lastName, String email, String mobileNumber, LocalDate dateOfBirth, Gender gender){
        UserDTO dto = new UserDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setMobileNumber(mobileNumber);
        dto.setDateOfBirth(dateOfBirth);
        dto.setGender(gender);
        return dto;
    }

    private User createUser(String firstName, String lastName, String email, String mobileNumber, LocalDate dateOfBirth, Gender gender){
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setMobileNumber(mobileNumber);
        u.setDateOfBirth(dateOfBirth);
        u.setGender(gender);
        return u;
    }

    protected static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> l = new ArrayList<>();
        iterable.forEach(l::add);
        return l;
    }
}
