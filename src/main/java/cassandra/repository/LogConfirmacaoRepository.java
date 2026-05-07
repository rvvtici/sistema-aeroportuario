package cassandra.repository;

import cassandra.entity.LogConfirmacao;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogConfirmacaoRepository extends CassandraRepository<LogConfirmacao, String> {
    List<LogConfirmacao> findByData(LocalDate data);
    List<LogConfirmacao> findByEntidadeTipo(String entidadeTipo);
}
