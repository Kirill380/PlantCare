package com.redkite.plantcare.service.impl;

import com.redkite.plantcare.common.dto.UserList;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.convertors.UserConverter;
import com.redkite.plantcare.dao.RoleDao;
import com.redkite.plantcare.dao.UserDao;
import com.redkite.plantcare.model.Role;
import com.redkite.plantcare.model.User;
import com.redkite.plantcare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

  private static final String REGULAR_USER_ROLE = "regularUser";
  private static final String ADMIN_ROLE = "admin";

  private PasswordEncoder passwordEncoder;

  private UserDao userDao;

  private RoleDao roleDao;

  private UserConverter userConverter;

  private PlatformTransactionManager txManager;


  //CHECKSTYLE:OFF
  @Autowired
  public UserServiceImpl(PasswordEncoder passwordEncoder,
                         UserDao userDao,
                         RoleDao roleDao,
                         UserConverter userConverter,
                         @Qualifier("transactionManager") PlatformTransactionManager txManager) {
    this.passwordEncoder = passwordEncoder;
    this.userDao = userDao;
    this.roleDao = roleDao;
    this.userConverter = userConverter;
    this.txManager = txManager;
  }
  //CHECKSTYLE:ON


  /**
   * Create default admin in database add two roles -- regularUser and admin.
   */
  @PostConstruct
  public void init() {
    TransactionTemplate tmpl = new TransactionTemplate(txManager);
    tmpl.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
        Role regularUser = new Role();
        regularUser.setName(REGULAR_USER_ROLE);
        regularUser.setLimitOfPlants(10);
        regularUser.setLimitOfPrecessingData(1000);
        roleDao.save(regularUser);

        Role adminUser = new Role();
        adminUser.setName(ADMIN_ROLE);
        adminUser.setLimitOfPlants(-1);
        adminUser.setLimitOfPrecessingData(-1);
        roleDao.save(adminUser);

        UserRequest user = new UserRequest();
        user.setEmail("admin@gmail.com");
        user.setPassword("admin123");
        createUser(user, ADMIN_ROLE);
      }
    });
  }


  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public UserResponse createUser(UserRequest userRequest) {
    return userConverter.toDto(createUser(userRequest, REGULAR_USER_ROLE));
  }

  private User createUser(UserRequest userRequest, String roleName) {
    User user = userConverter.toModel(userRequest);
    user.setCreationDate(LocalDateTime.now());
    user.setRole(roleDao.findByName(roleName));
    return userDao.save(user);
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
    User user = userDao.findByEmail(email);
    return passwordEncoder.matches(password, user.getPasswordHash());
  }
}
