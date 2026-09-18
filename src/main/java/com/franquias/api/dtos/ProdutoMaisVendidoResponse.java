package com.franquias.api.dtos;

public class ProdutoMaisVendidoResponse {

    private Long produtoId;
    private String produtoNome;
    private Long quantidadeTotal;

    public ProdutoMaisVendidoResponse(Long produtoId, String produtoNome, Long quantidadeTotal) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.quantidadeTotal = quantidadeTotal;
    }

    public Long getProdutoId() { return produtoId; }
    public String getProdutoNome() { return produtoNome; }
    public Long getQuantidadeTotal() { return quantidadeTotal; }
}
