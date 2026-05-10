package com.airport.redis.service;

import com.airport.cassandra.service.LogService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.airport.postgres.entity.Voo;
import com.airport.postgres.repository.VooRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatusVooService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final VooRepository vooRepository;
    private final LogService logService;

    public StatusVooService(RedisTemplate<String, Object> redisTemplate,
                            VooRepository vooRepository,
                            LogService logService) {
        this.redisTemplate = redisTemplate;
        this.vooRepository = vooRepository;
        this.logService = logService;
    }

    private String chave(Long vooId) {
        return "voo:" + vooId;
    }

    public Map<Object, Object> buscarStatus(Long vooId) {
        Map<Object, Object> dados = redisTemplate.opsForHash().entries(chave(vooId));
        if (dados == null || dados.isEmpty()) {
            Voo voo = vooRepository.findById(vooId).orElse(null);
            if (voo == null) return Map.of();
            _espelharNoRedis(vooId, voo.getStatus(), voo.getPortao());
            dados = redisTemplate.opsForHash().entries(chave(vooId));
        }
        return dados;
    }

    @Transactional
    public void atualizarStatus(Long vooId, String novoStatus) {
        Voo voo = vooRepository.findById(vooId)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + vooId));
        String statusAnterior = voo.getStatus();

        redisTemplate.opsForHash().put(chave(vooId), "status", novoStatus);
        voo.setStatus(novoStatus);
        vooRepository.save(voo);

        logService.registrarMudanca(
            String.valueOf(vooId),
            statusAnterior,
            novoStatus,
            "mudança de status do voo"
        );
    }

    @Transactional
    public void atualizarPortao(Long vooId, String novoPortao) {
        Voo voo = vooRepository.findById(vooId)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + vooId));
        String portaoAnterior = voo.getPortao() != null ? voo.getPortao() : "—";

        redisTemplate.opsForHash().put(chave(vooId), "portao", novoPortao);
        voo.setPortao(novoPortao);
        vooRepository.save(voo);

        logService.registrarMudanca(
            String.valueOf(vooId),
            portaoAnterior,
            novoPortao,
            "mudança de portão do voo"
        );
    }

    private void _espelharNoRedis(Long vooId, String status, String portao) {
        Map<String, String> dados = new HashMap<>();
        dados.put("status", status != null ? status : "PROGRAMADO");
        dados.put("portao", portao != null ? portao : "");
        redisTemplate.opsForHash().putAll(chave(vooId), dados);
    }
}