package com.airport.redis.controller;

import org.springframework.web.bind.annotation.*;
import com.airport.redis.service.StatusVooService;

import java.util.Map;

@RestController
@RequestMapping("/api/status/voos")
public class StatusVooController {

    private final StatusVooService statusVooService;

    public StatusVooController(StatusVooService statusVooService) {
        this.statusVooService = statusVooService;
    }

    // GET /api/status/voos/1
    // Retorna: { "status": "EMBARCANDO", "portao": "A12" }
    @GetMapping("/{id}")
    public Map<Object, Object> buscarStatus(@PathVariable Long id) {
        return statusVooService.buscarStatus(id);
    }

    // PATCH /api/status/voos/1/status?valor=CANCELADO
    @PatchMapping("/{id}/status")
    public void atualizarStatus(@PathVariable Long id,
                                 @RequestParam String valor) {
        statusVooService.atualizarStatus(id, valor);
    }

    // PATCH /api/status/voos/1/portao?valor=B03
    @PatchMapping("/{id}/portao")
    public void atualizarPortao(@PathVariable Long id,
                                 @RequestParam String valor) {
        statusVooService.atualizarPortao(id, valor);
    }
}
