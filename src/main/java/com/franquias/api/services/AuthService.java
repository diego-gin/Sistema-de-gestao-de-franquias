package com.franquias.api.services;

import com.franquias.api.dtos.LoginRequest;
import com.franquias.api.dtos.LoginResponse;
import com.franquias.api.exceptions.UnauthorizedException;
import com.franquias.api.models.Usuario;
import com.franquias.api.repositories.UsuarioRepository;
import com.franquias.api.security.JwtUtil;
import com.franquias.api.security.PasswordUtil;
import com.franquias.api.validation.ValidationUtil;

public class AuthService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public LoginResponse autenticar(LoginRequest dto) {
        ValidationUtil.validar(dto);

        // Mensagem de erro genérica de propósito: não revelamos se o problema
        // foi o e-mail ou a senha, para não facilitar enumeração de usuários.
        Usuario usuario = usuarioRepository.buscarPorEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("E-mail ou senha inválidos."));

        if (!usuario.isAtivo()) {
            throw new UnauthorizedException("Usuário inativo. Contate o administrador da franqueadora.");
        }

        if (!PasswordUtil.verificar(dto.getSenha(), usuario.getSenhaHash())) {
            throw new UnauthorizedException("E-mail ou senha inválidos.");
        }

        Long unidadeId = usuario.getUnidade() != null ? usuario.getUnidade().getId() : null;
        String token = JwtUtil.gerarToken(usuario.getId(), usuario.getPerfil(), unidadeId);

        return new LoginResponse(token, usuario.getNome(), usuario.getPerfil());
    }
}
