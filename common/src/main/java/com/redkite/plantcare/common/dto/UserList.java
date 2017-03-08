package com.redkite.plantcare.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserList {

    private List<UserResponse> users;

    private Long count;
}
