package com.franquias.api.dtos;

import java.math.BigDecimal;

public class FaturamentoPorUnidadeResponse {

    private Long unidadeId;
    private String unidadeNome;
    private BigDecimal faturamento;

    public FaturamentoPorUnidadeResponse(Long unidadeId, String unidadeNome, BigDecimal faturamento) {
        this.unidadeId = unidadeId;
        this.unidadeNome = unidadeNome;
        this.faturamento = faturamento;
    }

    public Long getUnidadeId() { return unidadeId; }
    public String getUnidadeNome() { return unidadeNome; }
    public BigDecimal getFaturamento() { return faturamento; }
}
