package cassandra.service;

import cassandra.entity.*;
import cassandra.repository.*;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class LogService {

    private final LogCriacaoRepository criacaoRepo;
    private final LogCancelamentoRepository cancelamentoRepo;
    private final LogMudancaRepository mudancaRepo;
    private final LogConfirmacaoRepository confirmacaoRepo;

    public LogService(LogCriacaoRepository criacaoRepo,
                      LogCancelamentoRepository cancelamentoRepo,
                      LogMudancaRepository mudancaRepo,
                      LogConfirmacaoRepository confirmacaoRepo) {
        this.criacaoRepo = criacaoRepo;
        this.cancelamentoRepo = cancelamentoRepo;
        this.mudancaRepo = mudancaRepo;
        this.confirmacaoRepo = confirmacaoRepo;
    }

    // CRIAÇÃO
    public LogCriacao registrarCriacao(String entidadeId, String entidadeTipo) {
        LogCriacao log = new LogCriacao();

        log.setId(gerarId("CR"));
        log.setData(LocalDate.now());
        log.setHorario(LocalTime.now());
        log.setEntidadeId(entidadeId);
        log.setEntidadeTipo(entidadeTipo);

        return criacaoRepo.save(log);
    }

    // CANCELAMENTO
    public LogCancelamento registrarCancelamento(String entidadeId, String entidadeTipo) {
        LogCancelamento log = new LogCancelamento();

        log.setId(gerarId("CA"));
        log.setData(LocalDate.now());
        log.setHorario(LocalTime.now());
        log.setEntidadeId(entidadeId);
        log.setEntidadeTipo(entidadeTipo);

        return cancelamentoRepo.save(log);
    }

    // CONFIRMAÇÃO
    public LogConfirmacao registrarConfirmacao(String entidadeId, String entidadeTipo) {
        LogConfirmacao log = new LogConfirmacao();

        log.setId(gerarId("CO"));
        log.setData(LocalDate.now());
        log.setHorario(LocalTime.now());
        log.setEntidadeId(entidadeId);
        log.setEntidadeTipo(entidadeTipo);

        return confirmacaoRepo.save(log);
    }

    // MUDANÇA
    public LogMudanca registrarMudanca(String entidadeId, String de, String para, String motivo) {
        LogMudanca log = new LogMudanca();

        log.setId(gerarId("MU"));
        log.setData(LocalDate.now());
        log.setHorario(LocalTime.now());
        log.setEntidadeId(entidadeId);
        log.setDe(de);
        log.setPara(para);
        log.setMotivo(motivo);

        return mudancaRepo.save(log);
    }

    // CONSULTAS

    public List<LogCriacao> buscarCriacoesPorData(LocalDate data) {
        return criacaoRepo.findByData(data);
    }

    public List<LogCancelamento> buscarCancelamentosPorTipo(String tipo) {
        return cancelamentoRepo.findByEntidadeTipo(tipo);
    }

    public List<LogMudanca> buscarMudancasPorEntidade(String entidadeId) {
        return mudancaRepo.findByEntidadeId(entidadeId);
    }

    public List<LogConfirmacao> buscarConfirmacoesPorData(LocalDate data) {
        return confirmacaoRepo.findByData(data);
    }

    // UTIL
    private String gerarId(String prefixo) {
        return prefixo + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}