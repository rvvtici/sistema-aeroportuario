package com.airport.postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "passageiro")
public class Passageiro {

    public Passageiro(String cpf, String nomeCompleto, LocalDate dataNascimento, String telefone, String email, String endereco) {
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public Passageiro() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    // CPF como chave natural (definido no DER como PK)
    @Id
    @Column(name = "cpf", length = 11)
    private String cpf;

    @NotBlank
    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Email
    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "endereco", length = 255)
    private String endereco;
}
