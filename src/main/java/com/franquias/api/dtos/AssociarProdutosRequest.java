package com.franquias.api.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AssociarProdutosRequest {

    /** Lista completa dos IDs de produtos que o fornecedor deve ter associados (substitui a lista anterior). */
    @NotNull(message = "produtoIds é obrigatório (pode ser uma lista vazia)")
    private List<Long> produtoIds;

    public List<Long> getProdutoIds() { return produtoIds; }
    public void setProdutoIds(List<Long> produtoIds) { this.produtoIds = produtoIds; }
}
