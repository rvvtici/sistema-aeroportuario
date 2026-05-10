package com.airport.postgres.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airport.postgres.entity.Passagem;
import com.airport.postgres.entity.TicketVoo;
import com.airport.postgres.repository.TicketVooRepository;

@Service
public class TicketService {

    private final TicketVooRepository ticketRepository;
    private final PassagemService passagemService;

    public TicketService(TicketVooRepository ticketRepository, PassagemService passagemService) {
        this.ticketRepository = ticketRepository;
        this.passagemService = passagemService;
    }

    public TicketVoo buscarPorId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado: " + id));
    }

    public List<TicketVoo> listarTodos() {
        return ticketRepository.findAll();
    }

    public List<TicketVoo> listarPorAeroporto(String iata) {
        return ticketRepository.findByAeroportoIata(iata);
    }

    @Transactional
    public TicketVoo gerarTicket(Long passagemId, Boolean possuiBagagem) {
        Passagem passagem = passagemService.buscarPorId(passagemId);

        // Regra de negócio: ticket só é gerado se o pagamento foi confirmado
        if (!"PAGO".equals(passagem.getStatusPagamento())) {
            throw new RuntimeException("Pagamento não confirmado para a passagem: " + passagemId);
        }

        // Garante que não existe ticket duplicado para a mesma passagem
        if (ticketRepository.findByPassagemId(passagemId).isPresent()) {
            throw new RuntimeException("Ticket já gerado para a passagem: " + passagemId);
        }

        TicketVoo ticket = new TicketVoo();
        ticket.setPassagem(passagem);
        ticket.setPossuiBagagem(possuiBagagem);
        ticket.setStatusEmbarque("AGUARDANDO");
        return ticketRepository.save(ticket);
    }

    @Transactional
    public TicketVoo atualizarStatusEmbarque(Long id, String novoStatus) {
        TicketVoo ticket = buscarPorId(id);
        ticket.setStatusEmbarque(novoStatus);
        return ticketRepository.save(ticket);
    }
}
