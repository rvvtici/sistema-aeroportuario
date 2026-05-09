package com.airport.postgres.service;

import com.airport.config.JwtUtil;
import com.airport.postgres.dto.LoginRequest;
import com.airport.postgres.dto.LoginResponse;
import com.airport.postgres.entity.Usuario;
import com.airport.postgres.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtUtil.gerar(usuario.getLogin(), usuario.getRole(), usuario.getAeroportoIata());

        return new LoginResponse(token, usuario.getNome(), usuario.getRole(), usuario.getAeroportoIata());
    }
}