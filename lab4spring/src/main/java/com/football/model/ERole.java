package com.football.model;

public enum  ERole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    ERole(String roleName) {
        this.name = roleName;
    }

    public String getName() {
        return name;
    }
}
