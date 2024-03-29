package com.event.management.analytics.dto;

import com.event.management.analytics.domain.Gender;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public class UserDTO {

    private String firstName;
    private int registrations;
    private LocalDate dateOfBirth;
    private Gender gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getRegistrations() {
        return registrations;
    }

    public void setRegistrations(int registrations) {
        this.registrations = registrations;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
