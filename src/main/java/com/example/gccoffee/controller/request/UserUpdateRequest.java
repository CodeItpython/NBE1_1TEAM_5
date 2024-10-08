package com.example.gccoffee.controller.request;

import com.example.gccoffee.model.user.Password;

public record UserUpdateRequest(
        Password password
) {
    public static UserUpdateRequest of(Password password) {
        return new UserUpdateRequest(password);
    }

}
