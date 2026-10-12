package com.maggiemunch.userService.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    CUSTOMER("ROLE_CUSTOMER");

    @JsonValue
    private final String authority;
}
