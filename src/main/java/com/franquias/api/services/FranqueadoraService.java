package com.franquias.api.services;

import com.franquias.api.dtos.FranqueadoraCreateRequest;
import com.franquias.api.exceptions.BadRequestException;
import com.franquias.api.exceptions.ConflictException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Franqueadora;
import com.franquias.api.repositories.FranqueadoraRepository;
import com.franquias.api.validation.ValidationUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FranqueadoraService {

    private final FranqueadoraRepository franqueadoraRepository = new FranqueadoraRepository();

    public Franqueadora cadastrar(FranqueadoraCreateRequest dto) {
        ValidationUtil.validar(dto);

        franqueadoraRepository.buscarPorCnpj(dto.getCnpj()).ifPresent(f -> {
            throw new ConflictException("Já existe uma franqueadora cadastrada com este CNPJ.");
        });

        Franqueadora franqueadora = new Franqueadora();
        franqueadora.setRazaoSocial(dto.getRazaoSocial());
        franqueadora.setCnpj(dto.getCnpj());
        franqueadora.setAtiva(true);

        if (dto.getDataFundacao() != null && !dto.getDataFundacao().isBlank()) {
            try {
                franqueadora.setDataFundacao(LocalDate.parse(dto.getDataFundacao()));
            } catch (DateTimeParseException e) {
                throw new BadRequestException("dataFundacao deve estar no formato AAAA-MM-DD.");
            }
        }

        return franqueadoraRepository.salvar(franqueadora);
    }

    public List<Franqueadora> listarTodas() {
        return franqueadoraRepository.listarTodas();
    }

    public Franqueadora buscarPorId(Long id) {
        return franqueadoraRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Franqueadora não encontrada."));
    }
}
