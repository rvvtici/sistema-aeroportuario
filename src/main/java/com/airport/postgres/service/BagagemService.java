package com.airport.postgres.service;

import com.airport.cassandra.service.LogService;
import com.airport.postgres.entity.Bagagem;
import com.airport.postgres.repository.BagagemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BagagemService {

    private final BagagemRepository bagagemRepository;
    private final LogService logService;

    public BagagemService(BagagemRepository bagagemRepository, LogService logService) {
        this.bagagemRepository = bagagemRepository;
        this.logService = logService;
    }

    public Bagagem buscarPorId(Long id) {
        return bagagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bagagem não encontrada: " + id));
    }

    public List<Bagagem> listarPorTicket(Long ticketId) {
        return bagagemRepository.findByTicketId(ticketId);
    }

    public List<Bagagem> listarPorStatus(String status) {
        return bagagemRepository.findByStatus(status);
    }

    @Transactional
    public Bagagem criar(Bagagem bagagem) {
        Bagagem salva = bagagemRepository.save(bagagem);
        logService.registrarCriacao(String.valueOf(salva.getId()), "BAGAGEM");
        return salva;
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        bagagemRepository.deleteById(id);
        logService.registrarCancelamento(String.valueOf(id), "BAGAGEM");
    }
}