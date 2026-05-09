package com.airport.postgres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    @JsonProperty("login")
    private String login;

    @JsonProperty("senha")
    private String senha;

    public LoginRequest() {}

    public String getLogin() { return login; }
    public String getSenha() { return senha; }
}