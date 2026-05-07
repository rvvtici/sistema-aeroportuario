package com.airport.postgres.repository;

import com.airport.postgres.entity.Passageiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PassageiroRepository extends JpaRepository<Passageiro, String> {
    // String porque a PK é o CPF

    // Busca por e-mail — útil para login ou validação de duplicidade
    Optional<Passageiro> findByEmail(String email);
}
