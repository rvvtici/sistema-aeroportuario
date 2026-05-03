package postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "voo",
    // Garante no banco que origem e destino nunca podem ser iguais
    uniqueConstraints = {},
    indexes = {
        @Index(name = "idx_voo_origem",   columnList = "origem"),
        @Index(name = "idx_voo_destino",  columnList = "destino"),
        @Index(name = "idx_voo_partida",  columnList = "horario_partida")
    }
)
// Check constraint — origem <> destino (definida no SQL do Flyway, anotada aqui para documentar)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Voo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "companhia_aerea", nullable = false, length = 100)
    private String companhiaAerea;

    // Relacionamento com Aeroporto via IATA (chave natural)
    @ManyToOne(optional = false)
    @JoinColumn(name = "origem", referencedColumnName = "iata")
    private Aeroporto origem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destino", referencedColumnName = "iata")
    private Aeroporto destino;

    @Column(name = "aeronave", length = 60)
    private String aeronave;

    @Column(name = "terminal", length = 10)
    private String terminal;

    // Espelhado no Redis para leitura em tempo real
    @Column(name = "portao", length = 10)
    private String portao;

    @NotNull
    @Column(name = "horario_partida", nullable = false)
    private LocalDateTime horarioPartida;

    @NotNull
    @Column(name = "horario_chegada", nullable = false)
    private LocalDateTime horarioChegada;

    @Column(name = "previsao_partida")
    private LocalDateTime previsaoPartida;

    @Column(name = "previsao_chegada")
    private LocalDateTime previsaoChegada;

    // Espelhado no Redis para leitura em tempo real
    // Valores: PROGRAMADO | EMBARCANDO | ATRASADO | CANCELADO | CONCLUIDO
    @NotBlank
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PROGRAMADO";
}
