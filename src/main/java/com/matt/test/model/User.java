package com.matt.test.model;

import com.matt.test.enums.Role;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username",unique = true)
    private String username;
    private String password;
    private double deposit;
    @Enumerated(EnumType.ORDINAL)
    private Role role;

}
