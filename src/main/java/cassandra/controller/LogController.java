package cassandra.controller;

import cassandra.service.LogService;
import cassandra.entity.*;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    // CRIACAO
    @PostMapping("/criacao")
    public LogCriacao criarLogCriacao(@RequestParam String entidadeId,
                                      @RequestParam String entidadeTipo) {
        return service.registrarCriacao(entidadeId, entidadeTipo);
    }

    // CANCELAMENTO
    @PostMapping("/cancelamento")
    public LogCancelamento criarLogCancelamento(@RequestParam String entidadeId,
                                                @RequestParam String entidadeTipo) {
        return service.registrarCancelamento(entidadeId, entidadeTipo);
    }

    // CONFIRMAÇÃO
    @PostMapping("/confirmacao")
    public LogConfirmacao criarLogConfirmacao(@RequestParam String entidadeId,
                                              @RequestParam String entidadeTipo) {
        return service.registrarConfirmacao(entidadeId, entidadeTipo);
    }

    // MUDANÇA
    @PostMapping("/mudanca")
    public LogMudanca criarLogMudanca(@RequestParam String entidadeId,
                                      @RequestParam String de,
                                      @RequestParam String para,
                                      @RequestParam String motivo) {
        return service.registrarMudanca(entidadeId, de, para, motivo);
    }

    // CONSULTAS
    @GetMapping("/criacao/data")
    public List<LogCriacao> buscarCriacoes(@RequestParam String data) {
        return service.buscarCriacoesPorData(LocalDate.parse(data));
    }

    @GetMapping("/confirmacao/data")
    public List<LogConfirmacao> buscarConfirmacoes(@RequestParam String data) {
        return service.buscarConfirmacoesPorData(LocalDate.parse(data));
    }
}