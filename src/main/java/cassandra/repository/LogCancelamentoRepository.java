package cassandra.repository;

import cassandra.entity.LogCancelamento;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogCancelamentoRepository extends CassandraRepository<LogCancelamento, String> {

    List<LogCancelamento> findByData(LocalDate data);
    List<LogCancelamento> findByEntidadeTipo(String entidadeTipo);
}