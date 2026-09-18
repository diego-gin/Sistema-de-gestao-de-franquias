package com.franquias.api.dtos;

import com.franquias.api.models.Royalty;
import com.franquias.api.models.enums.SituacaoPagamento;

import java.math.BigDecimal;

public class RoyaltyResponse {

    private Long id;
    private Long unidadeId;
    private String unidadeNome;
    private String periodoReferencia;
    private BigDecimal percentualAplicado;
    private BigDecimal faturamentoBase;
    private BigDecimal valorCalculado;
    private SituacaoPagamento situacaoPagamento;
    private String dataPagamento;

    public static RoyaltyResponse fromEntity(Royalty r) {
        RoyaltyResponse dto = new RoyaltyResponse();
        dto.id = r.getId();
        dto.unidadeId = r.getUnidade().getId();
        dto.unidadeNome = r.getUnidade().getNomeFantasia();
        dto.periodoReferencia = r.getPeriodoReferencia();
        dto.percentualAplicado = r.getPercentualAplicado();
        dto.faturamentoBase = r.getFaturamentoBase();
        dto.valorCalculado = r.getValorCalculado();
        dto.situacaoPagamento = r.getSituacaoPagamento();
        dto.dataPagamento = r.getDataPagamento() != null ? r.getDataPagamento().toString() : null;
        return dto;
    }

    public Long getId() { return id; }
    public Long getUnidadeId() { return unidadeId; }
    public String getUnidadeNome() { return unidadeNome; }
    public String getPeriodoReferencia() { return periodoReferencia; }
    public BigDecimal getPercentualAplicado() { return percentualAplicado; }
    public BigDecimal getFaturamentoBase() { return faturamentoBase; }
    public BigDecimal getValorCalculado() { return valorCalculado; }
    public SituacaoPagamento getSituacaoPagamento() { return situacaoPagamento; }
    public String getDataPagamento() { return dataPagamento; }
}
