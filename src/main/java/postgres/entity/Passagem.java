package postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "passagem",
    indexes = {
        @Index(name = "idx_passagem_voo", columnList = "voo_id"),
        @Index(name = "idx_passagem_cpf", columnList = "cpf_passageiro")
    }
)

public class Passagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cpf_passageiro", referencedColumnName = "cpf")
    private Passageiro passageiro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "voo_id")
    private Voo voo;

    @NotBlank
    @Column(name = "numero_assento", nullable = false, length = 6)
    private String numeroAssento;

    // Valores: ECONOMICA | EXECUTIVA | PRIMEIRA
    @NotBlank
    @Column(name = "classe_assento", nullable = false, length = 20)
    private String classeAssento;

    @NotNull
    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao = LocalDateTime.now();

    // Valores: PENDENTE | PAGO | CANCELADO
    @Column(name = "status_pagamento", nullable = false, length = 20)
    private String statusPagamento = "PENDENTE";

    // Valores: ATIVA | CANCELADA | USADA
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ATIVA";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public Voo getVoo() {
        return voo;
    }

    public void setVoo(Voo voo) {
        this.voo = voo;
    }

    public String getNumeroAssento() {
        return numeroAssento;
    }

    public void setNumeroAssento(String numeroAssento) {
        this.numeroAssento = numeroAssento;
    }

    public String getClasseAssento() {
        return classeAssento;
    }

    public void setClasseAssento(String classeAssento) {
        this.classeAssento = classeAssento;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Passagem(Long id, Passageiro passageiro, Voo voo, String numeroAssento, String classeAssento, BigDecimal preco) {
        this.id = id;
        this.passageiro = passageiro;
        this.voo = voo;
        this.numeroAssento = numeroAssento;
        this.classeAssento = classeAssento;
        this.preco = preco;
    }

    public Passagem() {
    }
}
