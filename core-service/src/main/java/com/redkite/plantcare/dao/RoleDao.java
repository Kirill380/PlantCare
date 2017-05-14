package com.redkite.plantcare.dao;

import com.redkite.plantcare.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleDao extends JpaRepository<Role, Long> {

    public Role findByName(String name);
}
