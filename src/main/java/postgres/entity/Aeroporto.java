package postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "aeroporto")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Aeroporto {

    @Id
    @Column(name = "iata", length = 3)
    private String iata;

    @NotBlank
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "uf", length = 2)
    private String uf;

    @NotBlank
    @Column(name = "pais", nullable = false, length = 60)
    private String pais;

    @Column(name = "fuso_horario", length = 50)
    private String fusoHorario;
}

