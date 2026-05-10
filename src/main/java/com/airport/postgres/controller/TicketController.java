package com.airport.postgres.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airport.postgres.entity.TicketVoo;
import com.airport.postgres.service.TicketService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // GET /api/tickets
    @GetMapping
    public List<TicketVoo> listarTodos(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        String aeroportoIata = (String) request.getAttribute("aeroportoIata");

        if (role == null || role.equals("ADMIN")) {
            return ticketService.listarTodos();
        }
        return ticketService.listarPorAeroporto(aeroportoIata);
    }

    // GET /api/tickets/1
    @GetMapping("/{id}")
    public ResponseEntity<TicketVoo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.buscarPorId(id));
    }

    // POST /api/tickets?passagemId=1&possuiBagagem=true
    @PostMapping
    public ResponseEntity<TicketVoo> gerarTicket(
            @RequestParam Long passagemId,
            @RequestParam Boolean possuiBagagem) {
        return ResponseEntity.ok(ticketService.gerarTicket(passagemId, possuiBagagem));
    }

    // PATCH /api/tickets/1/embarque
    @PatchMapping("/{id}/embarque")
    public ResponseEntity<TicketVoo> atualizarStatusEmbarque(
            @PathVariable Long id,
            @RequestParam String novoStatus) {
        return ResponseEntity.ok(ticketService.atualizarStatusEmbarque(id, novoStatus));
    }
}
