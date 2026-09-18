package com.franquias.api.services;

import com.franquias.api.dtos.VendaCreateRequest;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Venda;
import com.franquias.api.repositories.ItemVendaInput;
import com.franquias.api.repositories.UnidadeFranqueadaRepository;
import com.franquias.api.repositories.VendaRepository;
import com.franquias.api.validation.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VendaService {

    private final VendaRepository vendaRepository = new VendaRepository();
    private final UnidadeFranqueadaRepository unidadeRepository = new UnidadeFranqueadaRepository();

    public Venda registrarVenda(VendaCreateRequest dto, Long usuarioId) {
        ValidationUtil.validar(dto);

        unidadeRepository.buscarPorId(dto.getUnidadeId())
                .orElseThrow(() -> new NotFoundException("Unidade informada não existe."));

        List<ItemVendaInput> itensInput = dto.getItens().stream()
                .map(i -> new ItemVendaInput(i.getProdutoServicoId(), i.getQuantidade()))
                .collect(Collectors.toList());

        return vendaRepository.registrarVenda(dto.getUnidadeId(), usuarioId, itensInput);
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Venda não encontrada."));
    }

    public List<Venda> buscar(Long unidadeId, LocalDate dataInicio, LocalDate dataFim) {
        return vendaRepository.buscar(unidadeId, dataInicio, dataFim);
    }
}
