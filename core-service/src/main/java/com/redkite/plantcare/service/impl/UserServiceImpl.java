package com.redkite.plantcare.service.impl;

import com.redkite.plantcare.PlantCareException;
import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.PasswordUpdateDto;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.controllers.filters.UserFilter;
import com.redkite.plantcare.convertors.UserConverter;
import com.redkite.plantcare.dao.RoleDao;
import com.redkite.plantcare.dao.UserDao;
import com.redkite.plantcare.model.Role;
import com.redkite.plantcare.model.User;
import com.redkite.plantcare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

  private static final String REGULAR_USER_ROLE = "regularUser";
  private static final String ADMIN_ROLE = "admin";

  // @Autowired
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Autowired
  private UserDao userDao;

  @Autowired
  private RoleDao roleDao;

  @Autowired
  private UserConverter userConverter;

  @Autowired
  @Qualifier("transactionManager")
  private PlatformTransactionManager txManager;

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String createDefaults;


  //TODO move defaults creation to separate SQL script

  /**
   * Create default admin in database add two roles -- regularUser and admin.
   */
  @PostConstruct
  public void init() {
    if (!createDefaults.equals("create-drop")) {
      return;
    }

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
    UserResponse userResponse = userConverter.toDto(createUser(userRequest, REGULAR_USER_ROLE));
    userResponse.setEmail(null);
    userResponse.setFirstName(null);
    userResponse.setLastName(null);
    return userResponse;
  }

  private User createUser(UserRequest userRequest, String roleName) {
    if (userDao.existsByEmail(userRequest.getEmail())) {
      throw new PlantCareException("User with email [" + userRequest.getEmail() + "] already exists", HttpStatus.CONFLICT);
    }
    User user = userConverter.toModel(userRequest);
    user.setCreationDate(LocalDateTime.now());
    user.setRole(roleDao.findByName(roleName));
    return userDao.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public ItemList<UserResponse> findUsers(UserFilter filter) {
    Page<User> users = userDao.findUserByFilter(filter.getEmail(), filter);
    List<UserResponse> userResponses = userConverter.toDtoList(users.getContent());
    return new ItemList<>(userResponses, users.getTotalElements());
  }

  @Override
  public User getUserByEmail(String email) {
    return userDao.findByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse getUser(Long userId) {
    checkExistence(userId);
    return userConverter.toDto(userDao.getOne(userId));
  }

  @Override
  @Transactional(readOnly = true)
  public User getFullUser(Long userId) {
    checkExistence(userId);
    return userDao.getOne(userId);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void editUser(Long userId, UserRequest userRequest) {
    checkExistence(userId);
    User user = userDao.getOne(userId);
    user.merge(userRequest);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void deleteUser(Long userId) {
    checkExistence(userId);
    userDao.delete(userId);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void changePassword(Long userId, PasswordUpdateDto passwordUpdateDto) {
    checkExistence(userId);
    User user = userDao.getOne(userId);
    if (!passwordEncoder.matches(passwordUpdateDto.getOldPassword(), user.getPasswordHash())) {
      throw new PlantCareException("Password mismatch", HttpStatus.FORBIDDEN);
    }
    user.setPasswordHash(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
  }

  @Override
  public boolean checkPasswordMatching(String email, String password) {
    User user = userDao.findByEmail(email);
    return passwordEncoder.matches(password, user.getPasswordHash());
  }

  private void checkExistence(Long userId) {
    if (!userDao.exists(userId)) {
      throw new PlantCareException("User with id [" + userId + "] does not exist", HttpStatus.NOT_FOUND);
    }
  }
}
