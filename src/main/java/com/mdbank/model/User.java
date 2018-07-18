package com.mdbank.model;

import com.mdbank.model.dto.Role;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_user_id_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false, unique = true)
    String email;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
}
