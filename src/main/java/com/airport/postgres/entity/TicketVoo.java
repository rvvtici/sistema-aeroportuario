package com.airport.postgres.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(
    name = "ticket_de_voo",
    indexes = {
        @Index(name = "idx_ticket_passagem", columnList = "passagem_id")
    }
)

public class TicketVoo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ticket só existe se a passagem foi paga — validação feita no TicketService
    @OneToOne(optional = false)
    @JoinColumn(name = "passagem_id")
    private Passagem passagem;

    @Column(name = "possui_bagagem", nullable = false)
    private Boolean possuiBagagem = false;

    // Espelhado no Redis para leitura em tempo real
    // Valores: AGUARDANDO | EMBARCADO | CANCELADO
    @Column(name = "status_embarque", nullable = false, length = 20)
    private String statusEmbarque = "AGUARDANDO";
    
    @JsonManagedReference
    @OneToMany(mappedBy = "ticket")
    private List<Bagagem> bagagens;

    public List<Bagagem> getBagagens() {
        return bagagens;
    }

    public void setBagagens(List<Bagagem> bagagens) {
        this.bagagens = bagagens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passagem getPassagem() {
        return passagem;
    }

    public void setPassagem(Passagem passagem) {
        this.passagem = passagem;
    }

    public Boolean getPossuiBagagem() {
        return possuiBagagem;
    }

    public void setPossuiBagagem(Boolean possuiBagagem) {
        this.possuiBagagem = possuiBagagem;
    }

    public String getStatusEmbarque() {
        return statusEmbarque;
    }

    public void setStatusEmbarque(String statusEmbarque) {
        this.statusEmbarque = statusEmbarque;
    }

    public TicketVoo(Long id, Passagem passagem) {
        this.id = id;
        this.passagem = passagem;
    }

    public TicketVoo() {
    }
}
