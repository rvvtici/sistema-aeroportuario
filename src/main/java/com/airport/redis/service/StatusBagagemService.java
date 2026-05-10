package com.airport.redis.service;

import com.airport.cassandra.service.LogService;
import com.airport.postgres.entity.Bagagem;
import com.airport.postgres.repository.BagagemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatusBagagemService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BagagemRepository bagagemRepository;
    private final LogService logService;

    public StatusBagagemService(RedisTemplate<String, Object> redisTemplate,
                                BagagemRepository bagagemRepository,
                                LogService logService) {
        this.redisTemplate = redisTemplate;
        this.bagagemRepository = bagagemRepository;
        this.logService = logService;
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

    @PostConstruct
    public void sincronizarBagagensNoRedis() {
        bagagemRepository.findAll()
            .forEach(b -> _espelharNoRedis(b.getId(), b.getStatus()));
        System.out.println(">>> Redis sincronizado com bagagens");
    }

    @Transactional
    public void atualizarStatus(Long bagagemId, String novoStatus) {
        Bagagem bagagem = bagagemRepository.findById(bagagemId)
                .orElseThrow(() -> new RuntimeException("Bagagem não encontrada: " + bagagemId));
        String statusAnterior = bagagem.getStatus();

        redisTemplate.opsForHash().put(chave(bagagemId), "status", novoStatus);
        bagagem.setStatus(novoStatus);
        bagagemRepository.save(bagagem);

        logService.registrarMudanca(
            String.valueOf(bagagemId),
            statusAnterior,
            novoStatus,
            "mudança de status da bagagem"
        );

        if ("RETIRADA".equals(novoStatus)) {
            logService.registrarConfirmacao(
                String.valueOf(bagagemId),
                "BAGAGEM"
            );
        }
    }

    private void _espelharNoRedis(Long bagagemId, String status) {
        Map<String, String> dados = new HashMap<>();
        dados.put("status", status != null ? status : "CHECK_IN");
        redisTemplate.opsForHash().putAll(chave(bagagemId), dados);
    }
}