package com.airport.postgres.controller;

import com.airport.postgres.entity.Voo;
import com.airport.postgres.service.VooService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/voos")
public class VooController {

    private final VooService vooService;

    public VooController(VooService vooService) {
        this.vooService = vooService;
    }

    // GET /api/voos
    @GetMapping
    public List<Voo> listarTodos() {
        return vooService.listarTodos();
    }

    // GET /api/voos/1
    @GetMapping("/{id}")
    public ResponseEntity<Voo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vooService.buscarPorId(id));
    }

    // GET /api/voos/status/EMBARCANDO
    @GetMapping("/status/{status}")
    public List<Voo> buscarPorStatus(@PathVariable String status) {
        return vooService.buscarPorStatus(status);
    }

    // GET /api/voos/origem/GRU
    @GetMapping("/origem/{iata}")
    public List<Voo> buscarPorOrigem(@PathVariable String iata) {
        return vooService.buscarPorOrigem(iata);
    }

    // GET /api/voos/periodo?inicio=2025-08-10T00:00:00&fim=2025-08-10T23:59:59
    @GetMapping("/periodo")
    public List<Voo> buscarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return vooService.buscarPorPeriodo(inicio, fim);
    }

    // POST /api/voos
    @PostMapping
    public Voo criar(@RequestBody Voo voo) {
        return vooService.criar(voo);
    }

    // PUT /api/voos/1
    @PutMapping("/{id}")
    public ResponseEntity<Voo> atualizar(@PathVariable Long id, @RequestBody Voo voo) {
        return ResponseEntity.ok(vooService.atualizar(id, voo));
    }

    // DELETE /api/voos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vooService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
