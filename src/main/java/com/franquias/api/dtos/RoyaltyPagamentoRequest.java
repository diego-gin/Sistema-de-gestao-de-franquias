package com.franquias.api.dtos;

import com.franquias.api.models.enums.SituacaoPagamento;
import jakarta.validation.constraints.NotNull;

public class RoyaltyPagamentoRequest {

    @NotNull(message = "situacaoPagamento é obrigatória")
    private SituacaoPagamento situacaoPagamento;

    // Formato AAAA-MM-DD
    private String dataPagamento;

    public SituacaoPagamento getSituacaoPagamento() { return situacaoPagamento; }
    public void setSituacaoPagamento(SituacaoPagamento situacaoPagamento) { this.situacaoPagamento = situacaoPagamento; }
    public String getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(String dataPagamento) { this.dataPagamento = dataPagamento; }
}
