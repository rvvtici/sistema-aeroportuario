package com.airport.postgres.repository;

import com.airport.postgres.entity.Voo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VooRepository extends JpaRepository<Voo, Long> {

    // Busca voos por status — ex: todos os voos "EMBARCANDO"
    List<Voo> findByStatus(String status);

    // Busca voos por origem (IATA) — ex: todos saindo de "GRU"
    List<Voo> findByOrigemIata(String iata);

    // Busca voos por destino (IATA)
    List<Voo> findByDestinoIata(String iata);

    // Busca voos em uma janela de horário — ex: voos do dia
    List<Voo> findByHorarioPartidaBetween(LocalDateTime inicio, LocalDateTime fim);

    // Busca voos onde o aeroporto é origem OU destino
    List<Voo> findByOrigemIataOrDestinoIata(String origem, String destino);
}
