package com.airport.postgres.controller;

import com.airport.postgres.entity.Voo;
import com.airport.postgres.service.VooService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping
    public List<Voo> listarTodos(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        String aeroportoIata = (String) request.getAttribute("aeroportoIata");

        if (role == null || role.equals("ADMIN")) {
            return vooService.listarTodos();
        }
        return vooService.listarPorAeroporto(aeroportoIata);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vooService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public List<Voo> buscarPorStatus(@PathVariable String status) {
        return vooService.buscarPorStatus(status);
    }

    @GetMapping("/origem/{iata}")
    public List<Voo> buscarPorOrigem(@PathVariable String iata) {
        return vooService.buscarPorOrigem(iata);
    }

    @GetMapping("/periodo")
    public List<Voo> buscarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return vooService.buscarPorPeriodo(inicio, fim);
    }

    @PostMapping
    public Voo criar(@RequestBody Voo voo) {
        return vooService.criar(voo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voo> atualizar(@PathVariable Long id, @RequestBody Voo voo) {
        return ResponseEntity.ok(vooService.atualizar(id, voo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vooService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}