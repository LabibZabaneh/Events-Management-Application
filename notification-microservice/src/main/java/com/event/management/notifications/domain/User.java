package com.event.management.notifications.domain;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Serdeable
@Entity
public class User {

    @Id
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
