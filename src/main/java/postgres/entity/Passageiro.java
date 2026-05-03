package postgres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "passageiro")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Passageiro {

    // CPF como chave natural (definido no DER como PK)
    @Id
    @Column(name = "cpf", length = 11)
    private String cpf;

    @NotBlank
    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @NotNull
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Email
    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "endereco", length = 255)
    private String endereco;
}
