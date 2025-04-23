package com.pictspace.back.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Set<User> users;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Set<Room> rooms;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Set<BookingRequest> bookingRequests;
}