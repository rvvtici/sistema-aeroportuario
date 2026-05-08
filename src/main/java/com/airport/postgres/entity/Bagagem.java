package com.airport.postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "bagagem",
    indexes = {
        @Index(name = "idx_bagagem_ticket", columnList = "ticket_id")
    }
)

public class Bagagem {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketVoo getTicket() {
        return ticket;
    }

    public void setTicket(TicketVoo ticket) {
        this.ticket = ticket;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bagagem(Long id, TicketVoo ticket, BigDecimal peso) {
        this.id = id;
        this.ticket = ticket;
        this.peso = peso;
    }

    public Bagagem() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private TicketVoo ticket;

    @NotNull
    @Column(name = "peso", nullable = false, precision = 5, scale = 2)
    private BigDecimal peso;

    // Espelhado no Redis para leitura em tempo real
    // Valores: CHECK_IN | DESPACHADA | EM_VOO | ENTREGUE | EXTRAVIADA
    @Column(name = "status", nullable = false, length = 30)
    private String status = "CHECK_IN";
}
