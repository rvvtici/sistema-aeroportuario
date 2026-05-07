package com.airport.redis.controller;

import org.springframework.web.bind.annotation.*;
import com.airport.redis.service.StatusBagagemService;

import java.util.Map;

@RestController
@RequestMapping("/api/status/bagagens")
public class StatusBagagemController {

    private final StatusBagagemService statusBagagemService;

    public StatusBagagemController(StatusBagagemService statusBagagemService) {
        this.statusBagagemService = statusBagagemService;
    }

    // GET /api/status/bagagens/1
    @GetMapping("/{id}")
    public Map<Object, Object> buscarStatus(@PathVariable Long id) {
        return statusBagagemService.buscarStatus(id);
    }

    // PATCH /api/status/bagagens/1/status?valor=DESPACHADA
    @PatchMapping("/{id}/status")
    public void atualizarStatus(@PathVariable Long id,
                                 @RequestParam String valor) {
        statusBagagemService.atualizarStatus(id, valor);
    }
}
