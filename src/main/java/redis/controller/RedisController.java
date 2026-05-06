package redis.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.service.RedisService;

@RestController
@RequestMapping("/api")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/{tipo}/{id}")
    public void salvar(
        @PathVariable String tipo,
        @PathVariable String id,
        @RequestBody Map<String, String> dados
    ) {
        redisService.salvar(tipo, id, dados);
    }

    @GetMapping("/{tipo}/{id}")
    public Map<Object, Object> buscar(
        @PathVariable String tipo,
        @PathVariable String id
    ) {
        return redisService.buscar(tipo, id);
    }
}