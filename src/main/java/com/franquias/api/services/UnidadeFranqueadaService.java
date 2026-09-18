package com.franquias.api.services;

import com.franquias.api.dtos.UnidadeCreateRequest;
import com.franquias.api.dtos.UnidadeUpdateRequest;
import com.franquias.api.exceptions.BadRequestException;
import com.franquias.api.exceptions.ConflictException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Franqueado;
import com.franquias.api.models.Franqueadora;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.enums.SituacaoUnidade;
import com.franquias.api.repositories.FranqueadoRepository;
import com.franquias.api.repositories.FranqueadoraRepository;
import com.franquias.api.repositories.UnidadeFranqueadaRepository;
import com.franquias.api.validation.ValidationUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class UnidadeFranqueadaService {

    private final UnidadeFranqueadaRepository unidadeRepository = new UnidadeFranqueadaRepository();
    private final FranqueadoraRepository franqueadoraRepository = new FranqueadoraRepository();
    private final FranqueadoRepository franqueadoRepository = new FranqueadoRepository();

    public UnidadeFranqueada cadastrar(UnidadeCreateRequest dto) {
        ValidationUtil.validar(dto);

        unidadeRepository.buscarPorCnpj(dto.getCnpj()).ifPresent(u -> {
            throw new ConflictException("Já existe uma unidade cadastrada com este CNPJ.");
        });

        Franqueadora franqueadora = franqueadoraRepository.buscarPorId(dto.getFranqueadoraId())
                .orElseThrow(() -> new NotFoundException("Franqueadora informada não existe."));
        Franqueado franqueado = franqueadoRepository.buscarPorId(dto.getFranqueadoId())
                .orElseThrow(() -> new NotFoundException("Franqueado informado não existe."));

        LocalDate dataInicio;
        try {
            dataInicio = LocalDate.parse(dto.getDataInicio());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("dataInicio deve estar no formato AAAA-MM-DD.");
        }

        UnidadeFranqueada unidade = new UnidadeFranqueada();
        unidade.setFranqueadora(franqueadora);
        unidade.setFranqueado(franqueado);
        unidade.setNomeFantasia(dto.getNomeFantasia());
        unidade.setCnpj(dto.getCnpj());
        unidade.setEndereco(dto.getEndereco());
        unidade.setCidade(dto.getCidade());
        unidade.setEstado(dto.getEstado());
        unidade.setTelefone(dto.getTelefone());
        unidade.setEmail(dto.getEmail());
        unidade.setDataInicio(dataInicio);
        unidade.setSituacao(SituacaoUnidade.ATIVA);

        return unidadeRepository.salvar(unidade);
    }

    public UnidadeFranqueada atualizar(Long id, UnidadeUpdateRequest dto) {
        ValidationUtil.validar(dto);
        UnidadeFranqueada unidade = buscarPorId(id);

        unidade.setNomeFantasia(dto.getNomeFantasia());
        unidade.setEndereco(dto.getEndereco());
        unidade.setCidade(dto.getCidade());
        unidade.setEstado(dto.getEstado());
        unidade.setTelefone(dto.getTelefone());
        unidade.setEmail(dto.getEmail());

        return unidadeRepository.salvar(unidade);
    }

    public UnidadeFranqueada alterarSituacao(Long id, SituacaoUnidade novaSituacao) {
        UnidadeFranqueada unidade = buscarPorId(id);
        unidade.setSituacao(novaSituacao);
        return unidadeRepository.salvar(unidade);
    }

    public UnidadeFranqueada buscarPorId(Long id) {
        return unidadeRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Unidade não encontrada."));
    }

    public List<UnidadeFranqueada> buscar(String nome, String cidade, String cnpj,
                                           String responsavel, SituacaoUnidade situacao) {
        return unidadeRepository.buscar(nome, cidade, cnpj, responsavel, situacao);
    }
}
