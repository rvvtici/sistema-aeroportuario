package com.airport.postgres.controller;

import com.airport.postgres.entity.Passageiro;
import com.airport.postgres.service.PassageiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/passageiros")
public class PassageiroController {

    private final PassageiroService passageiroService;

    public PassageiroController(PassageiroService passageiroService) {
        this.passageiroService = passageiroService;
    }

    // GET /api/passageiros
    @GetMapping
    public List<Passageiro> listarTodos() {
        return passageiroService.listarTodos();
    }

    // GET /api/passageiros/12345678901
    @GetMapping("/{cpf}")
    public ResponseEntity<Passageiro> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(passageiroService.buscarPorCpf(cpf));
    }

    // POST /api/passageiros
    @PostMapping
    public Passageiro criar(@RequestBody Passageiro passageiro) {
        return passageiroService.criar(passageiro);
    }

    // PUT /api/passageiros/12345678901
    @PutMapping("/{cpf}")
    public ResponseEntity<Passageiro> atualizar(
            @PathVariable String cpf,
            @RequestBody Passageiro passageiro) {
        return ResponseEntity.ok(passageiroService.atualizar(cpf, passageiro));
    }

    // DELETE /api/passageiros/12345678901
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletar(@PathVariable String cpf) {
        passageiroService.deletar(cpf);
        return ResponseEntity.noContent().build();
    }
}

