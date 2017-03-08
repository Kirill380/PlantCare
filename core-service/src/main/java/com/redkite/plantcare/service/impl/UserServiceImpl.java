package com.redkite.plantcare.service.impl;

import com.redkite.plantcare.common.dto.UserList;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.convertors.UserConverter;
import com.redkite.plantcare.dao.UserDao;
import com.redkite.plantcare.model.User;
import com.redkite.plantcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConverter userConverter;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserResponse createUser(UserRequest userRequest) {
        User user = userConverter.toModel(userRequest);
        userDao.save(user);
        return userConverter.toDto(user);
    }

    @Override
    public UserList getUsers() {
        return null;
    }

    @Override
    public UserResponse getUser(Long userId) {
        return null;
    }

    @Override
    public void editUser(UserRequest userRequest) {

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteUser(UserRequest userRequest) {

    }

    @Override
    public boolean checkPasswordMatching(String email, String password) {
        return false;
    }
}
