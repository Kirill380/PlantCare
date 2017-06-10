package com.redkite.plantcare.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redkite.plantcare.CassandraClient;
import com.redkite.plantcare.common.dto.UserRequest;
import com.redkite.plantcare.common.dto.UserResponse;
import com.redkite.plantcare.dao.UserDao;
import com.redkite.plantcare.service.UserService;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerIT {


  @Autowired
  private WebApplicationContext context;

  @Autowired
  private UserDao userDao;

  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;


  @MockBean
  private CassandraClient cassandraClient;

  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void userCreation_validRequest_ok() throws Exception {
    UserRequest userRequest = new UserRequest();
    userRequest.setEmail("test@gmail.com");
    userRequest.setFirstName("John");
    userRequest.setFirstName("Doe");
    userRequest.setPassword("password123");
    String json = objectMapper.writeValueAsString(userRequest);
    MockHttpServletResponse response = mvc.perform(
            post("/api/users")
                    .content(json)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse();

    UserResponse userResponse = objectMapper.readValue(response.getContentAsString(), UserResponse.class);
    assertThat(userResponse.getId()).isNotNull();
    assertThat(userDao.exists(userResponse.getId())).isTrue();
  }
}
