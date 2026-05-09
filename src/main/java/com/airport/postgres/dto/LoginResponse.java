package com.airport.postgres.dto;

public class LoginResponse {
    private String token;
    private String nome;
    private String role;
    private String aeroportoIata;

    public LoginResponse(String token, String nome, String role, String aeroportoIata) {
        this.token = token;
        this.nome = nome;
        this.role = role;
        this.aeroportoIata = aeroportoIata;
    }

    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getRole() { return role; }
    public String getAeroportoIata() { return aeroportoIata; }
}