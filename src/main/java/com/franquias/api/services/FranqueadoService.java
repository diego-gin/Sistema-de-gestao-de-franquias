package com.franquias.api.services;

import com.franquias.api.dtos.FranqueadoCreateRequest;
import com.franquias.api.exceptions.ConflictException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Franqueado;
import com.franquias.api.repositories.FranqueadoRepository;
import com.franquias.api.validation.ValidationUtil;

import java.util.List;

public class FranqueadoService {

    private final FranqueadoRepository franqueadoRepository = new FranqueadoRepository();

    public Franqueado cadastrar(FranqueadoCreateRequest dto) {
        ValidationUtil.validar(dto);

        franqueadoRepository.buscarPorCpfCnpj(dto.getCpfCnpj()).ifPresent(f -> {
            throw new ConflictException("Já existe um franqueado cadastrado com este CPF/CNPJ.");
        });

        Franqueado franqueado = new Franqueado();
        franqueado.setNome(dto.getNome());
        franqueado.setCpfCnpj(dto.getCpfCnpj());
        franqueado.setEmail(dto.getEmail());
        franqueado.setTelefone(dto.getTelefone());

        return franqueadoRepository.salvar(franqueado);
    }

    public List<Franqueado> listarTodos() {
        return franqueadoRepository.listarTodos();
    }

    public Franqueado buscarPorId(Long id) {
        return franqueadoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Franqueado não encontrado."));
    }
}
