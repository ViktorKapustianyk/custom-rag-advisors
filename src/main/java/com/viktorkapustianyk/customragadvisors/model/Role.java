package com.viktorkapustianyk.customragadvisors.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Role {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    private final String role;

    public static Role getRole(String roleName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getRole().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + roleName));
    }
}
