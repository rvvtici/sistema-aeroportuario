package com.airport.postgres.service;

import com.airport.cassandra.service.LogService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.airport.postgres.entity.Voo;
import com.airport.postgres.repository.VooRepository;

@Service
public class VooService {

    private final VooRepository vooRepository;
    private final LogService logService;

    public VooService(VooRepository vooRepository, LogService logService) {
        this.vooRepository = vooRepository;
        this.logService = logService;
    }

    public List<Voo> listarTodos() {
        return vooRepository.findAll();
    }

    public List<Voo> listarPorAeroporto(String iata) {
        return vooRepository.findByOrigemIataOrDestinoIata(iata, iata);
    }

    public Voo buscarPorId(Long id) {
        return vooRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + id));
    }

    public List<Voo> buscarPorStatus(String status) {
        return vooRepository.findByStatus(status);
    }

    public List<Voo> buscarPorOrigem(String iata) {
        return vooRepository.findByOrigemIata(iata);
    }

    public List<Voo> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vooRepository.findByHorarioPartidaBetween(inicio, fim);
    }

    @Transactional
    public Voo criar(Voo voo) {
        Voo salvo = vooRepository.save(voo);
        logService.registrarCriacao(String.valueOf(salvo.getId()), "VOO");
        return salvo;
    }

    @Transactional
    public Voo atualizar(Long id, Voo dadosNovos) {
        Voo voo = buscarPorId(id);
        voo.setCompanhiaAerea(dadosNovos.getCompanhiaAerea());
        voo.setOrigem(dadosNovos.getOrigem());
        voo.setDestino(dadosNovos.getDestino());
        voo.setAeronave(dadosNovos.getAeronave());
        voo.setTerminal(dadosNovos.getTerminal());
        voo.setHorarioPartida(dadosNovos.getHorarioPartida());
        voo.setHorarioChegada(dadosNovos.getHorarioChegada());
        voo.setPrevisaoPartida(dadosNovos.getPrevisaoPartida());
        voo.setPrevisaoChegada(dadosNovos.getPrevisaoChegada());
        Voo salvo = vooRepository.save(voo);
        logService.registrarMudanca(String.valueOf(id), "dados anteriores", "dados atualizados", "atualização de voo");
        return salvo;
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        vooRepository.deleteById(id);
        logService.registrarCancelamento(String.valueOf(id), "VOO");
    }
}