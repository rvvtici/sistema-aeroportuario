package postgres.controller;

import postgres.entity.Aeroporto;
import postgres.repository.AeroportoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/aeroportos")
public class AeroportoController {

    private final AeroportoRepository aeroportoRepository;

    public AeroportoController(AeroportoRepository aeroportoRepository) {
        this.aeroportoRepository = aeroportoRepository;
    }

    // GET /api/aeroportos
    @GetMapping
    public List<Aeroporto> listarTodos() {
        return aeroportoRepository.findAll();
    }

    // GET /api/aeroportos/GRU
    @GetMapping("/{iata}")
    public ResponseEntity<Aeroporto> buscarPorIata(@PathVariable String iata) {
        return aeroportoRepository.findById(iata)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/aeroportos
    @PostMapping
    public Aeroporto criar(@RequestBody Aeroporto aeroporto) {
        return aeroportoRepository.save(aeroporto);
    }

    // DELETE /api/aeroportos/GRU
    @DeleteMapping("/{iata}")
    public ResponseEntity<Void> deletar(@PathVariable String iata) {
        if (!aeroportoRepository.existsById(iata)) {
            return ResponseEntity.notFound().build();
        }
        aeroportoRepository.deleteById(iata);
        return ResponseEntity.noContent().build();
    }
}
