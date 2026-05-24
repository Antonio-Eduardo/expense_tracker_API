package com.eduardo.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;


@Entity
@Table(name = "tb_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iD;

    private String name;
    private String email;
    private String password;
    private String cpf;
    private String phone;
    private Instant birthDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Local local;

    @OneToMany(mappedBy = "user")
    private Set<ContaBancaria> contas;
}
