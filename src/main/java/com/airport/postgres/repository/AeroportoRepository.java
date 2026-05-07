package com.airport.postgres.repository;

import com.airport.postgres.entity.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, String> {
    // JpaRepository<Entidade, TipoDaChavePrimária>
    // String porque a PK de Aeroporto é o iata (CHAR(3))
    // CRUD gerado automaticamente: save, findById, findAll, deleteById...
}