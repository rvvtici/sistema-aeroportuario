package com.airport.postgres.controller;

import com.airport.postgres.entity.Bagagem;
import com.airport.postgres.service.BagagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bagagens")
public class BagagemController {

    private final BagagemService bagagemService;

    public BagagemController(BagagemService bagagemService) {
        this.bagagemService = bagagemService;
    }

    // GET /api/bagagens/ticket/1
    @GetMapping("/ticket/{ticketId}")
    public List<Bagagem> listarPorTicket(@PathVariable Long ticketId) {
        return bagagemService.listarPorTicket(ticketId);
    }

    // GET /api/bagagens/1
    @GetMapping("/{id}")
    public ResponseEntity<Bagagem> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bagagemService.buscarPorId(id));
    }

    // POST /api/bagagens
    @PostMapping
    public ResponseEntity<Bagagem> criar(@RequestBody Bagagem bagagem) {
        return ResponseEntity.ok(bagagemService.criar(bagagem));
    }

    // DELETE /api/bagagens/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bagagemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}