package postgres.controller;

import postgres.entity.TicketVoo;
import postgres.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // GET /api/tickets
    @GetMapping
    public List<TicketVoo> listarTodos() {
        return ticketService.listarTodos();
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