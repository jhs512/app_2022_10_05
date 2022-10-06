package com.ll.exam.app__2022_10_05.app.member.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "username 을 입력해주세요.")
    private String username;
    @NotEmpty(message = "password 를 입력해주세요.")
    private String password;
}
