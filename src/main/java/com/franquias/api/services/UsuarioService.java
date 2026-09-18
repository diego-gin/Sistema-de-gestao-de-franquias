package com.franquias.api.services;

import com.franquias.api.dtos.UsuarioCreateRequest;
import com.franquias.api.exceptions.BadRequestException;
import com.franquias.api.exceptions.ConflictException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.Usuario;
import com.franquias.api.models.enums.Perfil;
import com.franquias.api.repositories.UnidadeFranqueadaRepository;
import com.franquias.api.repositories.UsuarioRepository;
import com.franquias.api.security.PasswordUtil;
import com.franquias.api.validation.ValidationUtil;

import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final UnidadeFranqueadaRepository unidadeRepository = new UnidadeFranqueadaRepository();

    public Usuario cadastrar(UsuarioCreateRequest dto) {
        ValidationUtil.validar(dto);

        usuarioRepository.buscarPorEmail(dto.getEmail()).ifPresent(u -> {
            throw new ConflictException("Já existe um usuário cadastrado com este e-mail.");
        });

        // ADMIN_FRANQUEADORA não pertence a uma unidade; os demais perfis precisam
        if (dto.getPerfil() == Perfil.ADMIN_FRANQUEADORA && dto.getUnidadeId() != null) {
            throw new BadRequestException("Usuário ADMIN_FRANQUEADORA não deve estar vinculado a uma unidade.");
        }
        if (dto.getPerfil() != Perfil.ADMIN_FRANQUEADORA && dto.getUnidadeId() == null) {
            throw new BadRequestException("Este perfil exige o vínculo com uma unidade (unidadeId).");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenhaHash(PasswordUtil.hash(dto.getSenha()));
        usuario.setPerfil(dto.getPerfil());
        usuario.setAtivo(true);

        if (dto.getUnidadeId() != null) {
            UnidadeFranqueada unidade = unidadeRepository.buscarPorId(dto.getUnidadeId())
                    .orElseThrow(() -> new NotFoundException("Unidade informada não existe."));
            usuario.setUnidade(unidade);
        }

        return usuarioRepository.salvar(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    public void alterarStatus(Long id, boolean ativo) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(ativo);
        usuarioRepository.salvar(usuario);
    }
}
