package com.franquias.api.services;

import com.franquias.api.dtos.ChamadosPorStatusResponse;
import com.franquias.api.dtos.FaturamentoPorUnidadeResponse;
import com.franquias.api.dtos.ProdutoMaisVendidoResponse;
import com.franquias.api.dtos.RoyaltiesTotaisResponse;
import com.franquias.api.repositories.RelatorioRepository;

import java.time.LocalDate;
import java.util.List;

public class RelatorioService {

    private final RelatorioRepository relatorioRepository = new RelatorioRepository();

    public List<FaturamentoPorUnidadeResponse> faturamentoPorUnidade(Long unidadeId, LocalDate dataInicio,
                                                                       LocalDate dataFim) {
        return relatorioRepository.faturamentoPorUnidade(unidadeId, dataInicio, dataFim, null);
    }

    public List<FaturamentoPorUnidadeResponse> rankingUnidades(LocalDate dataInicio, LocalDate dataFim, Integer limite) {
        return relatorioRepository.faturamentoPorUnidade(null, dataInicio, dataFim, limite);
    }

    public RoyaltiesTotaisResponse royaltiesTotais(Long unidadeId, String periodo) {
        return relatorioRepository.royaltiesTotais(unidadeId, periodo);
    }

    public List<ProdutoMaisVendidoResponse> produtosMaisVendidos(Long unidadeId, LocalDate dataInicio,
                                                                   LocalDate dataFim, Integer limite) {
        return relatorioRepository.produtosMaisVendidos(unidadeId, dataInicio, dataFim, limite);
    }

    public List<ChamadosPorStatusResponse> chamadosPorStatus(Long unidadeId) {
        return relatorioRepository.chamadosPorStatus(unidadeId);
    }
}
