package com.airport.postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanhiaAerea() {
        return companhiaAerea;
    }

    public void setCompanhiaAerea(String companhiaAerea) {
        this.companhiaAerea = companhiaAerea;
    }

    public Aeroporto getOrigem() {
        return origem;
    }

    public void setOrigem(Aeroporto origem) {
        this.origem = origem;
    }

    public Aeroporto getDestino() {
        return destino;
    }

    public void setDestino(Aeroporto destino) {
        this.destino = destino;
    }

    public String getAeronave() {
        return aeronave;
    }

    public void setAeronave(String aeronave) {
        this.aeronave = aeronave;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getPortao() {
        return portao;
    }

    public void setPortao(String portao) {
        this.portao = portao;
    }

    public LocalDateTime getHorarioPartida() {
        return horarioPartida;
    }

    public void setHorarioPartida(LocalDateTime horarioPartida) {
        this.horarioPartida = horarioPartida;
    }

    public LocalDateTime getHorarioChegada() {
        return horarioChegada;
    }

    public void setHorarioChegada(LocalDateTime horarioChegada) {
        this.horarioChegada = horarioChegada;
    }

    public LocalDateTime getPrevisaoPartida() {
        return previsaoPartida;
    }

    public void setPrevisaoPartida(LocalDateTime previsaoPartida) {
        this.previsaoPartida = previsaoPartida;
    }

    public LocalDateTime getPrevisaoChegada() {
        return previsaoChegada;
    }

    public void setPrevisaoChegada(LocalDateTime previsaoChegada) {
        this.previsaoChegada = previsaoChegada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Voo(Long id, String companhiaAerea, Aeroporto origem, Aeroporto destino, String aeronave, String terminal, String portao, LocalDateTime horarioPartida, LocalDateTime horarioChegada, LocalDateTime previsaoPartida, LocalDateTime previsaoChegada) {
        this.id = id;
        this.companhiaAerea = companhiaAerea;
        this.origem = origem;
        this.destino = destino;
        this.aeronave = aeronave;
        this.terminal = terminal;
        this.portao = portao;
        this.horarioPartida = horarioPartida;
        this.horarioChegada = horarioChegada;
        this.previsaoPartida = previsaoPartida;
        this.previsaoChegada = previsaoChegada;
    }

    public Voo() {
    }
}
