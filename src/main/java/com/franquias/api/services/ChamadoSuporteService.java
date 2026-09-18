package com.franquias.api.services;

import com.franquias.api.dtos.ChamadoCreateRequest;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.ChamadoSuporte;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.Usuario;
import com.franquias.api.models.enums.PrioridadeChamado;
import com.franquias.api.models.enums.StatusChamado;
import com.franquias.api.repositories.ChamadoSuporteRepository;
import com.franquias.api.repositories.UnidadeFranqueadaRepository;
import com.franquias.api.repositories.UsuarioRepository;
import com.franquias.api.validation.ValidationUtil;

import java.util.List;

public class ChamadoSuporteService {

    private final ChamadoSuporteRepository chamadoRepository = new ChamadoSuporteRepository();
    private final UnidadeFranqueadaRepository unidadeRepository = new UnidadeFranqueadaRepository();
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public ChamadoSuporte abrir(ChamadoCreateRequest dto, Long usuarioId) {
        ValidationUtil.validar(dto);

        UnidadeFranqueada unidade = unidadeRepository.buscarPorId(dto.getUnidadeId())
                .orElseThrow(() -> new NotFoundException("Unidade informada não existe."));
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário autenticado não encontrado."));

        ChamadoSuporte chamado = new ChamadoSuporte();
        chamado.setUnidade(unidade);
        chamado.setUsuarioAbertura(usuario);
        chamado.setCategoria(dto.getCategoria());
        chamado.setPrioridade(dto.getPrioridade());
        chamado.setDescricao(dto.getDescricao());
        chamado.setStatus(StatusChamado.ABERTO);

        return chamadoRepository.salvar(chamado);
    }

    public ChamadoSuporte atualizarStatus(Long id, StatusChamado novoStatus) {
        ChamadoSuporte chamado = buscarPorId(id);

        if (novoStatus == StatusChamado.FECHADO) {
            chamado.encerrar();
        } else {
            chamado.setStatus(novoStatus);
        }

        return chamadoRepository.salvar(chamado);
    }

    public ChamadoSuporte buscarPorId(Long id) {
        return chamadoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Chamado não encontrado."));
    }

    public List<ChamadoSuporte> buscar(Long unidadeId, StatusChamado status, PrioridadeChamado prioridade) {
        return chamadoRepository.buscar(unidadeId, status, prioridade);
    }
}
