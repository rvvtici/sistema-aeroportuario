package com.airport.redis.service;

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

    public StatusVooService(RedisTemplate<String, Object> redisTemplate,
                            VooRepository vooRepository) {
        this.redisTemplate = redisTemplate;
        this.vooRepository = vooRepository;
    }

    private String chave(Long vooId) {
        return "voo:" + vooId;
    }

    // Leitura — Redis primeiro, fallback no Postgres
    public Map<Object, Object> buscarStatus(Long vooId) {
        Map<Object, Object> dados = redisTemplate.opsForHash().entries(chave(vooId));

        if (dados == null || dados.isEmpty()) {
            Voo voo = vooRepository.findById(vooId).orElse(null);
            if (voo == null) return Map.of();
            // Popula o Redis com o que está no Postgres
            _espelharNoRedis(vooId, voo.getStatus(), voo.getPortao());
            dados = redisTemplate.opsForHash().entries(chave(vooId));
        }

        return dados;
    }

    // Atualiza status: Redis → Postgres
    @Transactional
    public void atualizarStatus(Long vooId, String novoStatus) {
        // 1. Redis (leitura em tempo real)
        redisTemplate.opsForHash().put(chave(vooId), "status", novoStatus);

        // 2. Postgres (persistência)
        Voo voo = vooRepository.findById(vooId)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + vooId));
        voo.setStatus(novoStatus);
        vooRepository.save(voo);
    }

    // Atualiza portão: Redis → Postgres
    @Transactional
    public void atualizarPortao(Long vooId, String novoPortao) {
        // 1. Redis
        redisTemplate.opsForHash().put(chave(vooId), "portao", novoPortao);

        // 2. Postgres
        Voo voo = vooRepository.findById(vooId)
                .orElseThrow(() -> new RuntimeException("Voo não encontrado: " + vooId));
        voo.setPortao(novoPortao);
        vooRepository.save(voo);
    }

    // Uso interno — só popula o Redis sem tocar no Postgres
    private void _espelharNoRedis(Long vooId, String status, String portao) {
        Map<String, String> dados = new HashMap<>();
        dados.put("status", status != null ? status : "PROGRAMADO");
        dados.put("portao", portao != null ? portao : "");
        redisTemplate.opsForHash().putAll(chave(vooId), dados);
    }
}
