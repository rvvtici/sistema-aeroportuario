package com.airport.cassandra.repository;

import com.airport.cassandra.entity.LogCriacao;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogCriacaoRepository extends CassandraRepository<LogCriacao, String> {

    List<LogCriacao> findByData(LocalDate data);
    List<LogCriacao> findByEntidadeTipo(String entidadeTipo);
}