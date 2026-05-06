package cassandra.entity;

import org.springframework.data.cassandra.core.mapping.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

@Table("log_confirmacao")
public class LogConfirmacao {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id; // formato: 1D-00000001

    @Column("data")
    private LocalDate data;

    @Column("horario")
    private LocalTime horario;

    @Column("entidade_id")
    private String entidadeId;

    // Tipo da entidade confirmada: VOO, BAGAGEM, PORTAO, PASSAGEM
    @Column("entidade_tipo")
    private String entidadeTipo;

    public LogConfirmacao() {}

    public LogConfirmacao(String id, LocalDate data, LocalTime horario,
                          String entidadeId, String entidadeTipo) {
        this.id = id;
        this.data = data;
        this.horario = horario;
        this.entidadeId = entidadeId;
        this.entidadeTipo = entidadeTipo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public String getEntidadeId() { return entidadeId; }
    public void setEntidadeId(String entidadeId) { this.entidadeId = entidadeId; }

    public String getEntidadeTipo() { return entidadeTipo; }
    public void setEntidadeTipo(String entidadeTipo) { this.entidadeTipo = entidadeTipo; }
}