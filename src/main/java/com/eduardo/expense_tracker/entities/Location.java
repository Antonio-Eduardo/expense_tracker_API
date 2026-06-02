package com.eduardo.expense_tracker.entities;

import com.eduardo.expense_tracker.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "tb_location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String state;
    private String address1;
    private String address2;
    private String zipCode;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private java.util.Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location location)) return false;
        return Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
