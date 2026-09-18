package com.franquias.api.services;

import com.franquias.api.dtos.RoyaltyCalcularRequest;
import com.franquias.api.dtos.RoyaltyPagamentoRequest;
import com.franquias.api.exceptions.BadRequestException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Royalty;
import com.franquias.api.models.enums.SituacaoPagamento;
import com.franquias.api.repositories.RoyaltyRepository;
import com.franquias.api.validation.ValidationUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RoyaltyService {

    private final RoyaltyRepository royaltyRepository = new RoyaltyRepository();

    public Royalty calcular(RoyaltyCalcularRequest dto) {
        ValidationUtil.validar(dto);
        return royaltyRepository.calcular(dto.getUnidadeId(), dto.getPeriodoReferencia(), dto.getPercentual());
    }

    public Royalty buscarPorId(Long id) {
        return royaltyRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Royalty não encontrado."));
    }

    public List<Royalty> buscar(Long unidadeId, String periodo, SituacaoPagamento situacao) {
        return royaltyRepository.buscar(unidadeId, periodo, situacao);
    }

    public Royalty registrarPagamento(Long id, RoyaltyPagamentoRequest dto) {
        ValidationUtil.validar(dto);

        LocalDate dataPagamento;
        if (dto.getDataPagamento() != null && !dto.getDataPagamento().isBlank()) {
            try {
                dataPagamento = LocalDate.parse(dto.getDataPagamento());
            } catch (DateTimeParseException e) {
                throw new BadRequestException("dataPagamento deve estar no formato AAAA-MM-DD.");
            }
        } else if (dto.getSituacaoPagamento() == SituacaoPagamento.PAGO) {
            dataPagamento = LocalDate.now();
        } else {
            dataPagamento = null;
        }

        return royaltyRepository.registrarPagamento(id, dto.getSituacaoPagamento(), dataPagamento);
    }
}
