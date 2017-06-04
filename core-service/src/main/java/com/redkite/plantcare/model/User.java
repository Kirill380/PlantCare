package com.redkite.plantcare.model;


import static com.redkite.plantcare.constants.DabConstants.UserTable;

import com.redkite.plantcare.common.dto.UserRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  private List<Plant> plants;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Role role;


  public User merge(UserRequest userRequest) {
    if (userRequest.getFirstName() != null) {
      this.setFirstName(userRequest.getFirstName());
    }
    if(userRequest.getLastName() != null) {
      this.setLastName(userRequest.getLastName());
    }
    return this;
  }
}
