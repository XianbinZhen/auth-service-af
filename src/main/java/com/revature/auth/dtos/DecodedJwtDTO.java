package com.revature.auth.dtos;

import com.auth0.jwt.interfaces.DecodedJWT;

public class DecodedJwtDTO {

    private int id;

    private String email;

    private String role;

    public DecodedJwtDTO() {
    }

    public DecodedJwtDTO(int id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public DecodedJwtDTO(DecodedJWT decodedJWT) {
        this.id = decodedJWT.getClaim("id").asInt();
        this.email = decodedJWT.getClaim("email").asString();
        this.role = decodedJWT.getClaim("role").asString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "DecodedJwtDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
