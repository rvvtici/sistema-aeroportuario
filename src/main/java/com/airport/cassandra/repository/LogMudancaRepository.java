package com.airport.cassandra.repository;

import com.airport.cassandra.entity.LogMudanca;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogMudancaRepository extends CassandraRepository<LogMudanca, String> {

    List<LogMudanca> findByData(LocalDate data);
    List<LogMudanca> findByEntidadeId(String entidadeId);
}
