package redis.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void salvar(String tipo, String id, Map<String, String> dados) {
        String key = tipo + ":" + id;
        redisTemplate.opsForHash().putAll(key, dados);
    }

    public Map<Object, Object> buscar(String tipo, String id) {
        String key = tipo + ":" + id;
        return redisTemplate.opsForHash().entries(key);
    }
}