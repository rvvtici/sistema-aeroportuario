package cassandra.entity;

import org.springframework.data.cassandra.core.mapping.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;

@Table("log_mudanca")
public class LogMudanca {

    // Partition key: distribui os dados entre os nós do Cassandra
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id; // formato: 1A-00000001

    @Column("data")
    private LocalDate data;

    @Column("horario")
    private LocalTime horario;

    @Column("entidade_id")
    private String entidadeId; // ID do voo, portão etc.

    @Column("de")
    private String de;

    @Column("para")
    private String para;

    @Column("motivo")
    private String motivo;

    public LogMudanca() {}

    public LogMudanca(String id, LocalDate data, LocalTime horario,
                      String entidadeId, String de, String para, String motivo) {
        this.id = id;
        this.data = data;
        this.horario = horario;
        this.entidadeId = entidadeId;
        this.de = de;
        this.para = para;
        this.motivo = motivo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public String getEntidadeId() { return entidadeId; }
    public void setEntidadeId(String entidadeId) { this.entidadeId = entidadeId; }

    public String getDe() { return de; }
    public void setDe(String de) { this.de = de; }

    public String getPara() { return para; }
    public void setPara(String para) { this.para = para; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
