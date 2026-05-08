package com.airport.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.airport.postgres.entity.Bagagem;
import com.airport.postgres.repository.BagagemRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatusBagagemService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BagagemRepository bagagemRepository;

    public StatusBagagemService(RedisTemplate<String, Object> redisTemplate,
                                BagagemRepository bagagemRepository) {
        this.redisTemplate = redisTemplate;
        this.bagagemRepository = bagagemRepository;
    }

    private String chave(Long bagagemId) {
        return "bagagem:" + bagagemId;
    }

    public Map<Object, Object> buscarStatus(Long bagagemId) {
        Map<Object, Object> dados = redisTemplate.opsForHash().entries(chave(bagagemId));

        if (dados == null || dados.isEmpty()) {
            Bagagem bagagem = bagagemRepository.findById(bagagemId).orElse(null);
            if (bagagem == null) return Map.of();
            _espelharNoRedis(bagagemId, bagagem.getStatus());
            dados = redisTemplate.opsForHash().entries(chave(bagagemId));
        }

        return dados;
    }

    @Transactional
    public void atualizarStatus(Long bagagemId, String novoStatus) {
        // 1. Redis
        redisTemplate.opsForHash().put(chave(bagagemId), "status", novoStatus);

        // 2. Postgres
        Bagagem bagagem = bagagemRepository.findById(bagagemId)
                .orElseThrow(() -> new RuntimeException("Bagagem não encontrada: " + bagagemId));
        bagagem.setStatus(novoStatus);
        bagagemRepository.save(bagagem);
    }

    private void _espelharNoRedis(Long bagagemId, String status) {
        Map<String, String> dados = new HashMap<>();
        dados.put("status", status != null ? status : "CHECK_IN");
        redisTemplate.opsForHash().putAll(chave(bagagemId), dados);
    }
}
