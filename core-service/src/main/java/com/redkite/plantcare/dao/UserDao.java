package com.redkite.plantcare.dao;


import com.redkite.plantcare.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDao extends JpaRepository<User, Long> {

  User findByEmail(String email);

  @Query(value = "SELECT u FROM User u where u.email like %:email% OR :email = NULL",
          countQuery = "SELECT count(u.id) FROM User u where u.email like %:email% OR :email = NULL")
  Page<User> findUserByFilter(@Param("email") String email, Pageable pageable);
}
