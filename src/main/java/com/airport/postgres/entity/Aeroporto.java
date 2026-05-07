package com.airport.postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "aeroporto")
public class Aeroporto {
    
    public Aeroporto() {
    }

    public Aeroporto(String iata, String nome, String cidade, String uf, String pais, String fusoHorario) {
        this.iata = iata;
        this.nome = nome;
        this.cidade = cidade;
        this.uf = uf;
        this.pais = pais;
        this.fusoHorario = fusoHorario;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFusoHorario() {
        return fusoHorario;
    }

    public void setFusoHorario(String fusoHorario) {
        this.fusoHorario = fusoHorario;
    }

    @Id
    @Column(name = "iata", length = 3)
    private String iata;
    
    @NotBlank
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "uf", length = 2)
    private String uf;

    @NotBlank
    @Column(name = "pais", nullable = false, length = 60)
    private String pais;

    @Column(name = "fuso_horario", length = 50)
    private String fusoHorario;
}

