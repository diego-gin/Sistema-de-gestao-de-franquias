package com.franquias.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemVendaRequest {

    @NotNull(message = "produtoServicoId é obrigatório")
    private Long produtoServicoId;

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 1, message = "quantidade deve ser maior que zero")
    private Integer quantidade;

    public Long getProdutoServicoId() { return produtoServicoId; }
    public void setProdutoServicoId(Long produtoServicoId) { this.produtoServicoId = produtoServicoId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
