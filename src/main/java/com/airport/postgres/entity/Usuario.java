package com.airport.postgres.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(nullable = false)
    private String role;

    @Column(name = "aeroporto_iata", length = 3)
    private String aeroportoIata;

    // Getters e Setters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getLogin() { return login; }
    public String getSenhaHash() { return senhaHash; }
    public String getRole() { return role; }
    public String getAeroportoIata() { return aeroportoIata; }
}