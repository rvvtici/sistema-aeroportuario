package postgres.service;

import postgres.entity.Voo;
import postgres.repository.VooRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VooService {

    private final VooRepository vooRepository;

    public VooService(VooRepository vooRepository) {
        this.vooRepository = vooRepository;
    }

    public List<Voo> listarTodos() {
        return vooRepository.findAll();
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
        return vooRepository.save(voo);
    }

  
  // VooService.atualizar() — retira status e portão daqui
    @Transactional
    public Voo atualizar(Long id, Voo dadosNovos) {
      Voo voo = buscarPorId(id);
      voo.setCompanhiaAerea(dadosNovos.getCompanhiaAerea());
      voo.setOrigem(dadosNovos.getOrigem());
      voo.setDestino(dadosNovos.getDestino());
      voo.setAeronave(dadosNovos.getAeronave());
      voo.setTerminal(dadosNovos.getTerminal());
    // portao e status saem daqui — gerenciados pelo StatusVooService
      voo.setHorarioPartida(dadosNovos.getHorarioPartida());
      voo.setHorarioChegada(dadosNovos.getHorarioChegada());
      voo.setPrevisaoPartida(dadosNovos.getPrevisaoPartida());
      voo.setPrevisaoChegada(dadosNovos.getPrevisaoChegada());
      return vooRepository.save(voo);
    }

    
    @Transactional
    public void deletar(Long id) {
        buscarPorId(id); // garante que existe antes de deletar
        vooRepository.deleteById(id);
    }
}
