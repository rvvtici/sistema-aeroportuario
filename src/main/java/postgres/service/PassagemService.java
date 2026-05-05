package postgres.service;

import postgres.entity.Passagem;
import postgres.repository.PassagemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PassagemService {

    private final PassagemRepository passagemRepository;

    public PassagemService(PassagemRepository passagemRepository) {
        this.passagemRepository = passagemRepository;
    }

    public List<Passagem> listarTodas() {
        return passagemRepository.findAll();
    }

    public Passagem buscarPorId(Long id) {
        return passagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passagem não encontrada: " + id));
    }

    public List<Passagem> buscarPorPassageiro(String cpf) {
        return passagemRepository.findByPassageiroCpf(cpf);
    }

    public List<Passagem> buscarPorVoo(Long vooId) {
        return passagemRepository.findByVooId(vooId);
    }

    @Transactional
    public Passagem criar(Passagem passagem) {
        return passagemRepository.save(passagem);
    }

    @Transactional
    public Passagem atualizarStatus(Long id, String novoStatus) {
        Passagem passagem = buscarPorId(id);
        passagem.setStatus(novoStatus);
        return passagemRepository.save(passagem);
    }

    @Transactional
    public Passagem confirmarPagamento(Long id) {
        Passagem passagem = buscarPorId(id);
        passagem.setStatusPagamento("PAGO");
        return passagemRepository.save(passagem);
    }

    @Transactional
    public void deletar(Long id) {
        buscarPorId(id);
        passagemRepository.deleteById(id);
    }
}
