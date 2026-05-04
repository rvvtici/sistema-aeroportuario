package postgres.controller;

import postgres.entity.Passagem;
import postgres.service.PassagemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/passagens")
public class PassagemController {

    private final PassagemService passagemService;

    public PassagemController(PassagemService passagemService) {
        this.passagemService = passagemService;
    }

    // GET /api/passagens
    @GetMapping
    public List<Passagem> listarTodas() {
        return passagemService.listarTodas();
    }

    // GET /api/passagens/1
    @GetMapping("/{id}")
    public ResponseEntity<Passagem> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(passagemService.buscarPorId(id));
    }

    // GET /api/passagens/passageiro/12345678901
    @GetMapping("/passageiro/{cpf}")
    public List<Passagem> buscarPorPassageiro(@PathVariable String cpf) {
        return passagemService.buscarPorPassageiro(cpf);
    }

    // GET /api/passagens/voo/1
    @GetMapping("/voo/{vooId}")
    public List<Passagem> buscarPorVoo(@PathVariable Long vooId) {
        return passagemService.buscarPorVoo(vooId);
    }

    // POST /api/passagens
    @PostMapping
    public Passagem criar(@RequestBody Passagem passagem) {
        return passagemService.criar(passagem);
    }

    // PATCH /api/passagens/1/pagamento
    @PatchMapping("/{id}/pagamento")
    public ResponseEntity<Passagem> confirmarPagamento(@PathVariable Long id) {
        return ResponseEntity.ok(passagemService.confirmarPagamento(id));
    }

    // PATCH /api/passagens/1/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Passagem> atualizarStatus(
            @PathVariable Long id,
            @RequestParam String novoStatus) {
        return ResponseEntity.ok(passagemService.atualizarStatus(id, novoStatus));
    }

    // DELETE /api/passagens/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        passagemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
