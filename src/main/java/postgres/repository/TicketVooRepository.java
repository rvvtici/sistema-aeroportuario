package postgres.repository;

import postgres.entity.TicketVoo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketVooRepository extends JpaRepository<TicketVoo, Long> {

    // Busca o ticket vinculado a uma passagem
    Optional<TicketVoo> findByPassagemId(Long passagemId);

    // Todos os tickets com determinado status de embarque
    List<TicketVoo> findByStatusEmbarque(String statusEmbarque);
}

