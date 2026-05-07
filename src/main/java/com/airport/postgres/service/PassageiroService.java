package com.airport.postgres.service;

import com.airport.postgres.entity.Passageiro;
import com.airport.postgres.repository.PassageiroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PassageiroService {

    private final PassageiroRepository passageiroRepository;

    public PassageiroService(PassageiroRepository passageiroRepository) {
        this.passageiroRepository = passageiroRepository;
    }

    public List<Passageiro> listarTodos() {
        return passageiroRepository.findAll();
    }

    public Passageiro buscarPorCpf(String cpf) {
        return passageiroRepository.findById(cpf)
                .orElseThrow(() -> new RuntimeException("Passageiro não encontrado: " + cpf));
    }

    @Transactional
    public Passageiro criar(Passageiro passageiro) {
        if (passageiroRepository.existsById(passageiro.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + passageiro.getCpf());
        }
        return passageiroRepository.save(passageiro);
    }

    @Transactional
    public Passageiro atualizar(String cpf, Passageiro dadosNovos) {
        Passageiro passageiro = buscarPorCpf(cpf);
        passageiro.setNomeCompleto(dadosNovos.getNomeCompleto());
        passageiro.setDataNascimento(dadosNovos.getDataNascimento());
        passageiro.setTelefone(dadosNovos.getTelefone());
        passageiro.setEmail(dadosNovos.getEmail());
        passageiro.setEndereco(dadosNovos.getEndereco());
        return passageiroRepository.save(passageiro);
    }

    @Transactional
    public void deletar(String cpf) {
        buscarPorCpf(cpf);
        passageiroRepository.deleteById(cpf);
    }
}
