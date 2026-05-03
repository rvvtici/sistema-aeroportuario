package postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
    name = "ticket_voo",
    indexes = {
        @Index(name = "idx_ticket_passagem", columnList = "passagem_id")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
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
}
