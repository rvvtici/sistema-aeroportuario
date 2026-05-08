package com.airport.postgres.repository;

import com.airport.postgres.entity.Passagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PassagemRepository extends JpaRepository<Passagem, Long> {

    // Todas as passagens de um passageiro
    List<Passagem> findByPassageiroCpf(String cpf);

    // Todas as passagens de um voo
    List<Passagem> findByVooId(Long vooId);

    // Passagens de um voo com status específico — ex: só as ATIVAS
    List<Passagem> findByVooIdAndStatus(Long vooId, String status);
}
