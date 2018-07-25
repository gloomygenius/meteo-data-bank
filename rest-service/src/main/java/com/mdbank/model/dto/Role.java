package com.mdbank.model.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Role {
    USER,
    ADMIN(USER);

    @Getter
    private Role[] includedRoles;

    Role(Role... included) {
        List<Role> roles = new ArrayList<>(Arrays.asList(included));
        roles.add(this);
        includedRoles = roles.toArray(new Role[0]);
    }
}