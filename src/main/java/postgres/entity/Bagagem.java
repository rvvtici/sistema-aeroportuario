package postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "bagagem",
    indexes = {
        @Index(name = "idx_bagagem_ticket", columnList = "ticket_id")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Bagagem {

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
