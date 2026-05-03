package postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
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
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
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
}
