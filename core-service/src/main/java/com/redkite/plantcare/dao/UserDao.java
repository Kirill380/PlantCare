package com.redkite.plantcare.dao;


import com.redkite.plantcare.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
