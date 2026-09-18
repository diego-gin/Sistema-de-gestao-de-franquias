package com.franquias.api.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class VendaCreateRequest {

    @NotNull(message = "unidadeId é obrigatório")
    private Long unidadeId;

    @NotEmpty(message = "A venda deve ter pelo menos um item")
    @Valid
    private List<ItemVendaRequest> itens;

    public Long getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Long unidadeId) { this.unidadeId = unidadeId; }
    public List<ItemVendaRequest> getItens() { return itens; }
    public void setItens(List<ItemVendaRequest> itens) { this.itens = itens; }
}
