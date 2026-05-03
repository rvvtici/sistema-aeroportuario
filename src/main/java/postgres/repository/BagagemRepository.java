package postgres.repository;

import postgres.entity.Bagagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BagagemRepository extends JpaRepository<Bagagem, Long> {

    // Todas as bagagens de um ticket
    List<Bagagem> findByTicketId(Long ticketId);

    // Bagagens por status — ex: todas "EXTRAVIADAS"
    List<Bagagem> findByStatus(String status);
}
