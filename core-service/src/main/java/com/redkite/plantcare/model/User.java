package com.redkite.plantcare.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import static com.redkite.plantcare.constants.DabConstants.UserTable;

@Entity
@Data
@ToString(of = {"id", "email"})
@EqualsAndHashCode(of = {"email"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = UserTable.EMAIL, unique = true, nullable = false)
    private String email;

    @Column(name = UserTable.FIRST_NAME)
    private String firstName;

    @Column(name = UserTable.LAST_NAME)
    private String lastName;

    @Column(name = UserTable.PASSWORD_HASH, nullable = false)
    private String passwordHash;

}
