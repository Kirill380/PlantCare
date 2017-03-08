package com.redkite.plantcare.common.dto;

import com.redkite.plantcare.common.constraint.Email;
import com.redkite.plantcare.common.constraint.Password;
import lombok.Data;

@Data
public class UserRequest {

    @Email
    private String email;

    private String firstName;

    private String lastName;

    @Password
    private String password;
}
