package com.airport.postgres.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airport.postgres.entity.TicketVoo;

@Repository
public interface TicketVooRepository extends JpaRepository<TicketVoo, Long> {

    Optional<TicketVoo> findByPassagemId(Long passagemId);
    List<TicketVoo> findByStatusEmbarque(String statusEmbarque);

    // Busca tickets cujo voo tem o aeroporto como origem OU destino
    @Query("SELECT t FROM TicketVoo t " +
           "JOIN t.passagem p " +
           "JOIN p.voo v " +
           "WHERE v.origem.iata = :iata OR v.destino.iata = :iata")
    List<TicketVoo> findByAeroportoIata(@Param("iata") String iata);
}

